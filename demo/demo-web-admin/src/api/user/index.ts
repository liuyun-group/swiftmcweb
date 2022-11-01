import { PageResult } from '../common/interface/common';
import { message_api } from '../message';
import { UserDetailVO, UserInfoDTO, UserPageQueryVO } from './interface/user';
import {services} from "/@/api/common";

export const user_api = {

    /**
     * 新增/更新用户
     * 
     * @param user
     * @returns 
     */
    add_update: (user: UserDetailVO) => {
        return message_api.sendrec<UserDetailVO, number>({
            service: services.user,
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
            service: services.user,
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
            service: services.user,
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
            service: services.user,
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
            service: services.user,
            messtype: 'page',
            data: pageVO,
        });
    }


}