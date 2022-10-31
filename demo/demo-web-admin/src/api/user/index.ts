import { PageResult } from '../common/interface/common';
import { message_api } from '../message';
import { UserDetailVO, UserInfoDTO, UserPageQueryVO } from './interface/user';


/* 用户服务 */
const USER_SERVICE = 'USER';

export const user_api = {

    /**
     * 新增/更新用户
     * 
     * @param data 
     * @returns 
     */
    add_update: (user: UserDetailVO) => {
        return message_api.sendrec<UserDetailVO, number>({
            service: USER_SERVICE,
            messtype: 'add_update',
            data: user,
        });
    },

    /**
     * 删除用户
     * 
     * @param id 
     * @returns 
     */
    del: (id: number) => {
        return message_api.sendrec<number, number>({
            service: USER_SERVICE,
            messtype: 'del',
            data: id,
        });
    },

    /**
     * 获取用户信息
     * 
     * @param id 
     * @returns 
     */
    detail: (id: number) => {
        return message_api.sendrec<number, UserInfoDTO>({
            service: USER_SERVICE,
            messtype: 'detail',
            data: id,
        });
    },

    /**
     * 判断用户是否存在
     * 
     * @param id 
     * @returns 
     */
    exists: (id: number) => {
        return message_api.sendrec<number, boolean>({
            service: USER_SERVICE,
            messtype: 'exists',
            data: id,
        });
    },

    /**
     * 分页查询用户
     * 
     * @param pageVO 
     * @returns 
     */
    page: (pageVO: UserPageQueryVO) => {
        return message_api.sendrec<UserPageQueryVO, PageResult<UserInfoDTO>>({
            service: USER_SERVICE,
            messtype: 'page',
            data: pageVO,
        });
    }


}