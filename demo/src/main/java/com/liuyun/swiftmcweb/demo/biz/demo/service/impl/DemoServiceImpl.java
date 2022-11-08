package com.liuyun.swiftmcweb.demo.biz.demo.service.impl;

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
 * @version 1.0
 * @date 10/19/22
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
