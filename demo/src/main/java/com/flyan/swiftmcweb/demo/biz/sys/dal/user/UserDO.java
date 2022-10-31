package com.flyan.swiftmcweb.demo.biz.sys.dal.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author flyan
 * @version 1.0
 * @date 10/25/22
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDO {

    private Long id;

    private String username;

    private String password;

    private String nickname;

}
