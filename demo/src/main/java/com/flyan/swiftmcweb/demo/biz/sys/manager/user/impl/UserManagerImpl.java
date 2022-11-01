package com.flyan.swiftmcweb.demo.biz.sys.manager.user.impl;

import cn.hutool.core.util.StrUtil;
import com.flyan.swiftmcweb.core.annotation.MessageHandleManager;
import com.flyan.swiftmcweb.demo.biz.common.poj.PageResult;
import com.flyan.swiftmcweb.demo.biz.common.poj.Pager;
import com.flyan.swiftmcweb.demo.biz.dal.UserDao;
import com.flyan.swiftmcweb.demo.biz.sys.api.user.dto.UserInfoDTO;
import com.flyan.swiftmcweb.demo.biz.sys.api.user.vo.UserDetailVO;
import com.flyan.swiftmcweb.demo.biz.sys.api.user.vo.UserPageQueryVO;
import com.flyan.swiftmcweb.demo.biz.sys.dal.user.UserDO;
import com.flyan.swiftmcweb.demo.biz.sys.manager.user.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.flyan.swiftmcweb.core.util.exception.ServiceExceptionUtil.exception;
import static com.flyan.swiftmcweb.demo.biz.common.enums.ErrorCodeConstants.DUPLICATE_USERNAME;


/**
 * @author flyan
 * @version 1.0
 * @date 10/27/22
 */
@MessageHandleManager(name = "userManager")
@Slf4j
public class UserManagerImpl implements UserManager {

    @Resource
    private UserDao userDao;

    @Override
    public Boolean exists(Long userId) {
        return userDao.exists(userId);
    }

    @Override
    public UserInfoDTO getUserInfo(Long userId) {
        UserDO userDO = userDao.selectById(userId);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userDO, userInfoDTO);
        return userInfoDTO;
    }

    @Override
    public Long saveOrUpdateUser(UserDetailVO addUser) {
        var add = addUser.getId() == null;
        var dupUser = userDao.list().stream()
                .filter(x -> x.getUsername().equals(addUser.getUsername()))
                .findFirst().orElse(null);
        if(dupUser != null && (add || !dupUser.getId().equals(addUser.getId()))) {
            throw exception(DUPLICATE_USERNAME);
        }

        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(addUser, userDO);
        if(add) {
            userDao.insert(userDO);
        } else {
            userDao.update(userDO);
        }
        log.info("users: {}", userDao.list());
        return userDO.getId();
    }

    @Override
    public Long delUser(Long userId) {
        userDao.deleteById(userId);
        return userId;
    }

    @Override
    public PageResult<UserInfoDTO> page(UserPageQueryVO pageVO) {
        List<UserInfoDTO> users = userDao.list().stream().filter(x -> {
            var f = true;
            if(pageVO.getId() != null) {
                f = x.getId().equals(pageVO.getId());
            }
            if(StrUtil.isNotBlank(pageVO.getUsername())) {
                f = x.getUsername().contains(pageVO.getUsername());
            }
            if(StrUtil.isNotBlank(pageVO.getNickname())) {
                f = x.getNickname().contains(pageVO.getNickname());
            }
            return f;
        }).map(x -> {
            var y = new UserInfoDTO();
            BeanUtils.copyProperties(x, y);
            return y;
        }).toList();
        Pager<UserInfoDTO> pager = new Pager<>(users, pageVO.getPageSize());
        return new PageResult<>(pager.page(pageVO.getPageNo()),
                (long) users.size(),
                (long) pager.getPageCount());
    }

}
