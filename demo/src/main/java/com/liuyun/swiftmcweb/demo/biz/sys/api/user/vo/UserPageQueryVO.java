package com.liuyun.swiftmcweb.demo.biz.sys.api.user.vo;

import com.liuyun.swiftmcweb.demo.biz.common.poj.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户分页查询信息 VO
 *
 * @author flyan
 * @version 1.0
 * @date 10/26/22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserPageQueryVO extends PageParam {

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

}
