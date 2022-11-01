import { message_api } from '../message';
import { CaptchaImageDTO, LoginReqVO, LoginResultDTO } from './interface/auth';
import {services} from "/@/api/common";

/**
 * 登录api接口集合
 */
export const login_api = {
	
	/**
	 * 用户登录
	 * 
	 * @param data 
	 * @returns 
	 */
	sign_in: (data: LoginReqVO) => {
		return message_api.sendrec<LoginReqVO, LoginResultDTO>({
			service: services.auth,
			messtype: "login",
			data: data
		});
	},

	/**
	 * 用户退出登录
	 * 
	 * @param data 
	 * @returns 
	 */
	sign_out: () => {
		return message_api.sendrec<null, boolean>({
			service: services.auth,
			messtype: "logout",
			data: null
		});
	},

	/**
	 * 验证码
	 * 
	 * @returns 
	 */
	captcha_image: () => {
		return message_api.sendrec<null, CaptchaImageDTO>({
			service: services.auth,
			messtype: "captcha_image",
			data: null
		});
	}
}
