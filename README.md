# SWIFTMCWEB v1.0.0

`SWIFTMCWEB` 是一个集成自`spring web`的敏捷消息通信`web api`框架。

- 使用统一消息通信，提供通信接口给客户端调用服务。
- 简单高效的开发架构，更敏捷快速的开发`api`接口。
- 前端统一的调用规范，提高前端对接效率。

相关文档：

- 暂无

# 1. 快速开始

- 加入`SWIFTMCWEB`到你的`maven`依赖中

```xml

<dependencies>
  <!-- swiftmcweb setup!!! -->
  <dependency>
    <groupId>com.flyan.swiftmcweb</groupId>
    <artifactId>swiftmcweb</artifactId>
    <version>${swiftmcweb.version}</version>
  </dependency>
</dependencies>
```

- 实现`SWIFTMCWEB`提供的用户验证接口`SwiftmcwebAuthManager`，若未实现，将会导致启动失败。

```java
package com.liuyun.swiftmcweb.core.web.manager;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.liuyun.swiftmcweb.core.annotation.MessageHandleFunc;
import com.liuyun.swiftmcweb.core.annotation.MessageHandleManager;
import com.liuyun.swiftmcweb.core.util.jwt.JwtUtils;

import static com.liuyun.swiftmcweb.core.framework.security.core.enums.JwtConsts.PAYLOAD_UID;
import static com.liuyun.swiftmcweb.core.framework.security.core.enums.JwtConsts.TOKEN_HEAD;

/**
 * swiftmcweb 框架用户验证管理器，必须实现且使用 {@link MessageHandleManager} 注释。
 * 如果未实现该接口，则无法正常使用 swiftmcweb，swiftmcweb 将无法为其提供 {@link MessageHandleFunc}
 * 服务功能注解提供的用户登录校验功能。
 *
 * @author flyan
 */
public interface SwiftmcwebAuthManager {
    /**
     * 校验授权
     *
     * @param authorization 授权
     * @return
     */
    default Boolean validateAuth(String authorization) {
        if (StrUtil.isBlank(authorization) || authorization.length() <= TOKEN_HEAD.length()) {
            return false;
        }

        var token = authorization.substring(TOKEN_HEAD.length());
        return JwtUtils.verifyToken(token) == JwtUtils.TokenVerifyResult.SUCCESS;
    }

    /**
     * 获取授权的用户id
     *
     * @param authorization 授权
     * @return
     */
    default Long getUserId(String authorization) {
        if (StrUtil.isBlank(authorization) || authorization.length() <= TOKEN_HEAD.length()) {
            return null;
        }

        var token = authorization.substring(TOKEN_HEAD.length());
        var verResult = JwtUtils.verifyToken(token);
        if (verResult != JwtUtils.TokenVerifyResult.SUCCESS) {
            return null;
        }

        var jwtPayloads = verResult.getJwt().getPayloads();
        return jwtPayloads.getLong(PAYLOAD_UID);
    }

    /**
     * 颁发授权
     *
     * @param userId    用户id
     * @return
     */
    default String userId2Token(Long userId) {
        return JwtUtils.genToken(MapUtil.<String, Object>builder()
                .put(PAYLOAD_UID, userId)
                .build());
    }
}
```

- 开始编写消息处理服务接口，消息处理接口中的消息处理函数有着统一的签名`ResponseMessage<?> func(Message<?> message)`，一个`demo`服务接口如下

```java
import com.liuyun.swiftmcweb.core.pojo.Message;
import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
import com.liuyun.swiftmcweb.core.web.service.IMessageHandleService;
import com.liuyun.swiftmcweb.demo.biz.demo.api.dto.HiRespDTO;
import com.liuyun.swiftmcweb.demo.biz.demo.api.vo.HiReqVO;

public interface DemoService extends IMessageHandleService {

    /**
     * 说嗨~
     *
     * @param message
     * @return
     */
    ResponseMessage<HiRespDTO> sayHello(Message<HiReqVO> message);

}
```

- 编写消息处理服务实现，为消息消息处理服务使用`@MessageHandleService`注解标注并给予一个唯一的服务名称，为消息处理函数使用`@MessageHandleFunc`
  注解标注并给予一个服务内唯一的消息类型，同时还可以配置注解值`@MessageHandleFunc.auth`来设置该接口是否需要自动认证，默认需要。

```java
import com.liuyun.swiftmcweb.core.annotation.MessageHandleFunc;
import com.liuyun.swiftmcweb.core.annotation.MessageHandleService;
import com.liuyun.swiftmcweb.core.pojo.Message;
import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
import com.liuyun.swiftmcweb.demo.biz.demo.api.dto.HiRespDTO;
import com.liuyun.swiftmcweb.demo.biz.demo.api.vo.HiReqVO;
import com.liuyun.swiftmcweb.demo.biz.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.liuyun.swiftmcweb.core.pojo.ResponseMessage.success;

/**
 * 服务 - DEMO服务
 *
 * @apiNote ipc 内部通信
 * @author flyan
 */
@MessageHandleService(service = "DEMO")
@RestController /* 由于 smart-doc 是通过源码生成 api 文档的，它只认识 .java 文件是否包含
 * Controller、RestController 等注解，所以这里为了让 smart-doc 生成文档，
 * 只能这样加上注解了，实际上不加也不会影响系统功能，暂未想到更好的解决方案。
 */
@Slf4j
public class DemoServiceImpl implements DemoService {
    /**
     * 说嗨~
     *
     * @apiNote <p>
     *     {
     *         "service": "DEMO",
     *         "messtype": "SAY_HI",
     *         "data": {
     *             "name": "flyan"
     *         }
     *     }
     * </p>
     * @param message
     * @return
     */
    @MessageHandleFunc(messtype = "SAY_HI")
    @PostMapping("DEMO/SAY_HI")
    @Override
    public ResponseMessage<HiRespDTO> sayHello(@RequestBody Message<HiReqVO> message) {
        String toSay = "hello " + message.getData().getName();
        return success(getServiceName(), "SAY_HI",
                new HiRespDTO().setHi(toSay));
    }
}
```

在这里，我们使用了`smart-doc`来生成`api`文档，看个人喜好可以使用swagger。同上面的`demo`服务，业务简单，故到这里就完成了一个功能的开发。如果需要调用此接口，只需要统一调用`sendrec`
接口既可以，该接口源码如下。

```java
package com.liuyun.swiftmcweb.core.web.controller;

import com.liuyun.swiftmcweb.core.pojo.Message;
import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
import com.liuyun.swiftmcweb.core.web.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 系统 - 消息控制器
 *
 * @apiNote 消息控制器，完成消息通信的处理
 * @author flyan
 */
@Slf4j
@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * 发送一条消息，并接收响应的消息
     *
     * @apiNote 发送一条消息给某服务，请求某功能，并接收其响应的消息
     * @param message
     * @return
     */
    @PostMapping("/sendrec")
    public ResponseMessage<Object> sendrec(@Valid @RequestBody Message<Object> message) {
        return messageService.sendrec(message);
    }
}
```

该接口签名和消息处理函数签名一致，之后所有的`api`接口都通过`sendrec`接口统一处理。调用方只需要查阅文档知悉请求服务和服务功能，分别放入消息里的`Message.service`和`Message.messtype`
即可，接口参数则放入`Message.data`中，消息定义如下。

```java
package com.liuyun.swiftmcweb.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 消息通信-消息
 *
 * @apiNote 消息通信传输的消息
 * @author flyan
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Message<T> implements Serializable {
    /**
     * 服务
     *
     * @mock SERVICE
     */
    protected String service;

    /**
     * 消息类型
     *
     * @mock coffee
     */
    protected String messtype;

    /**
     * 消息体
     */
    protected T data;
}
```

至此，我们快速的开发了一个`api`接口。

# 2. demo

工程下有一个专门编写的前后端`demo`，详见`swiftmcweb/demo`。

- 后端`demo`源码详见`swiftmcweb/demo/src`。

如何运行？

进入`swiftmcweb/demo`目录，控制台输入以下命令打包并运行`jar`包。
```bash
mvn clean package -Dmaven.test.skip=true
cd target/
java -server -jar swiftmcweb-demo.jar
```

- 前端`demo`源码详见`swiftmcweb/demo/demo-web-admin`。

如何运行？

进入`swiftmcweb/demo/demo-web-admin`目录，控制台输入以下命令安装依赖并运行：
```bash
npm install
npm run dev
```

# 3. Q&A

- Q: 你这框架看着好像只能写普通接口，上传文件等特殊功能接口怎么办？
- A: 是的，`SWIFTMCWEB`只为开发`api`，但它基于`springweb`，并且以上的功能都是无侵入的，你完全可以使用原生的`springweb`来完成这些许特殊需求，但这些接口都会被要求使用`token`
  请求头来进行身份验证（如果使用@OriginApi并设置auth=false则可以不进行身份验证，谨慎使用！）。
- Q: `SWIFTMCWEB`的文档看起来非常简单，能否提供更详细的开发文档？
- A: 会有的！工作比较忙，我们会抓紧时间更新！感兴趣的务必先看看`demo`，这些代码很好的展示了`SWIFTMCWEB`的能力！


