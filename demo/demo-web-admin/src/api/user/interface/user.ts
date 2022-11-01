/**
 * USER 用户服务相关类型定义
 */

/**
 * 用户详情 VO
 */
export interface UserDetailVO {

    /**
     * id
     */
    id: number | null;

    /**
     * 用户名
     */
    username: string;

    /**
     * 密码
     */
    password: string;

    /**
     * 用户昵称
     */
    nickname: string;

    /**
     * 头像
     */
    headPic: string;

}

/**
 * 用户信息 DTO
 */
export interface UserInfoDTO {
    
    /**
     * id
     */
     id: number | null;

     /**
      * 用户名
      */
     username: string | null;
 
     /**
      * 用户昵称
      */
     nickname: string | null;

    /**
     * 头像
     */
     headPic: string | null;

}

/**
 * 用户分页查询信息 VO
 */
export interface UserPageQueryVO {

    /**
     * 页码，从 1 开始
     */
    pageNo: number | null;

    /**
     * 每页条数，最大值为 100
     */
    pageSize: number | null;

    /**
     * id
     */
     id: number | null;

     /**
      * 用户名
      */
     username: string | null;
 
     /**
      * 用户昵称
      */
     nickname: string | null;

}


