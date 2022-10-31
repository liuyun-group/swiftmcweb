package com.flyan.swiftmcweb.demo.biz.dal;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.flyan.swiftmcweb.demo.biz.sys.dal.user.UserDO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户 Dao，模拟数据
 *
 * @author flyan
 * @version 1.0
 * @date 10/25/22
 */
@Component
public class UserDao {

    private static long nextId = 2L;
    private final static Map<Long, UserDO> db0 = MapUtil.<Long, UserDO>builder()
            .put(0L, new UserDO(0L, "admin", "1002", "管理员"))
            .put(1L, new UserDO(1L, "flyan", "147258", "辰晓夜"))
            .build();

    public boolean exists(Long id){
        return db0.containsKey(id);
    }

    public UserDO selectById(Long id) {
        return db0.get(id);
    }

    public List<UserDO> list() {
        return new ArrayList<>(db0.values());
    }

    public int insert(UserDO userDO) {
        if(userDO == null) {
            return 0;
        }

        if(userDO.getId() == null) {
            userDO.setId(nextId);
            nextId += 1;
        }

        db0.put(userDO.getId(), userDO);
        return 1;
    }

    public int update(UserDO userDO) {
        if(userDO == null || userDO.getId() == null) {
            return 0;
        }

        UserDO dbUser = db0.get(userDO.getId());
        if (StrUtil.isNotBlank(userDO.getUsername())) {
            dbUser.setUsername(userDO.getUsername());
        }
        if (StrUtil.isNotBlank(userDO.getPassword())) {
            dbUser.setPassword(userDO.getPassword());
        }
        if (StrUtil.isNotBlank(userDO.getNickname())) {
            dbUser.setNickname(userDO.getNickname());
        }
        return 1;
    }

    public int deleteById(Long id) {
        var userDO = db0.remove(id);
        return userDO != null ? 1 : 0;
    }

}
