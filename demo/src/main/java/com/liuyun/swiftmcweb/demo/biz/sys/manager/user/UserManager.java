package com.liuyun.swiftmcweb.demo.biz.sys.manager.user;


import com.liuyun.swiftmcweb.core.web.manager.IMessageHandleManager;
import com.liuyun.swiftmcweb.demo.biz.common.poj.PageResult;
import com.liuyun.swiftmcweb.demo.biz.sys.api.user.dto.UserInfoDTO;
import com.liuyun.swiftmcweb.demo.biz.sys.api.user.vo.UserDetailVO;
import com.liuyun.swiftmcweb.demo.biz.sys.api.user.vo.UserPageQueryVO;

/**
 * @author flyan
 * @version 1.0
 * @date 10/27/22
 */
public interface UserManager extends IMessageHandleManager {

    Boolean exists(Long userId);

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    UserInfoDTO getUserInfo(Long userId);

    /**
     * 新增/更新用户
     *
     * @param userDetailVO
     * @return
     */
    Long saveOrUpdateUser(UserDetailVO userDetailVO);

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    Long delUser(Long userId);

    /**
     * 分页查询用户
     *
     * @param pageQueryVO
     * @return
     */
    PageResult<UserInfoDTO> page(UserPageQueryVO pageQueryVO);

}
