package com.liuyun.swiftmcweb.demo.biz.sys.service.user.impl;


import com.liuyun.swiftmcweb.core.annotation.MessageHandleFunc;
import com.liuyun.swiftmcweb.core.annotation.MessageHandleService;
import com.liuyun.swiftmcweb.core.pojo.Message;
import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
import com.liuyun.swiftmcweb.demo.biz.common.poj.PageResult;
import com.liuyun.swiftmcweb.demo.biz.dal.UserDao;
import com.liuyun.swiftmcweb.demo.biz.sys.api.user.dto.UserInfoDTO;
import com.liuyun.swiftmcweb.demo.biz.sys.api.user.vo.UserDetailVO;
import com.liuyun.swiftmcweb.demo.biz.sys.api.user.vo.UserPageQueryVO;
import com.liuyun.swiftmcweb.demo.biz.sys.manager.user.UserManager;
import com.liuyun.swiftmcweb.demo.biz.sys.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.liuyun.swiftmcweb.core.pojo.ResponseMessage.success;

/**
 * 服务 - 用户服务
 *
 * @author flyan
 * @version 1.0
 * @date 10/25/22
 */
@MessageHandleService(service = "USER")
@RestController /* 由于 smart-doc 是通过源码生成 api 文档的，它只认识 .java 文件是否包含
                 * Controller、RestController 等注解，所以这里为了让 smart-doc 生成文档，
                 * 只能这样加上注解了，实际上不加也不会影响系统功能，暂未想到更好的解决方案。
                 */
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private UserManager userManager;

    /**
     * 判断用户是否存在
     *
     * @apiNote <p>
     *     {
     *         "service": "USER",
     *         "messtype": "exists",
     *         "data": 0
     *     }
     * </p>
     * @param message
     * @return  是否存在
     */
    @MessageHandleFunc(messtype = "exists")
    @PostMapping("USER/exists")
    @Override
    public ResponseMessage<Boolean> exists(@RequestBody @Valid Message<Long> message) {
        return success(message, userManager.exists(message.getData()));
    }

    /**
     * 获取用户信息
     *
     * @apiNote
     * <p>
     * {
     *     "service": "USER",
     *     "messtype": "detail",
     *     "data": 0
     * }
     * </p>
     * @param message
     * @return
     */
    @MessageHandleFunc(messtype = "detail")
    @PostMapping("USER/detail")
    @Override
    public ResponseMessage<UserInfoDTO> getUserInfo(@RequestBody @Valid Message<Long> message) {
        return success(message, userManager.getUserInfo(message.getData()));
    }

    /**
     * 新增/更新用户
     *
     * @apiNote
     * <P>
     * {
     *     "service": "USER",
     *     "messtype": "add_update",
     *     "data": {
     *         "username": "flyan",
     *         "password": "123456",
     *         "nickname": "辰晓夜"
     *     }
     * }
     * </P>
     * @param message
     * @return  更新后的用户id
     */
    @MessageHandleFunc(messtype = "add_update")
    @PostMapping("USER/add_update")
    @Override
    public ResponseMessage<Long> saveOrUpdateUser(@RequestBody @Valid Message<UserDetailVO> message) {
        return success(message, userManager.saveOrUpdateUser(message.getData()));
    }

    /**
     * 删除用户
     *
     * @apiNote
     * <p>
     * {
     *     "service": "USER",
     *     "messtype": "del",
     *     "data": 2
     * }
     * </p>
     * @param message
     * @return  删除的用户id
     */
    @MessageHandleFunc(messtype = "del")
    @PostMapping("USER/del")
    @Override
    public ResponseMessage<Long> delUser(@RequestBody @Valid Message<Long> message) {
        userDao.deleteById(message.getData());
        return success(message, message.getData());
    }

    /**
     * 分页查询用户
     *
     * @apiNote
     * <p>
     * {
     *     "service": "USER",
     *     "messtype": "page",
     *     "data": {
     *         "pageNo": 1,
     *         "pageSize": 10,
     *         "id": 1,
     *         "username": "flyan",
     *         "nickname": "辰晓夜"
     *     }
     * }
     * </p>
     * @param message
     * @return  分页结果
     */
    @MessageHandleFunc(messtype = "page")
    @PostMapping("USER/page")
    @Override
    public ResponseMessage<PageResult<UserInfoDTO>> page(@RequestBody @Valid Message<UserPageQueryVO> message) {
        return success(message, userManager.page(message.getData()));
    }

}
