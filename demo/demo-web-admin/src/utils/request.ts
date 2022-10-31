import axios from 'axios';
import { ElNotification } from 'element-plus';
import { ResponseMessage } from '../api/message/interface/message';
import { Session } from '/@/utils/storage';

// 配置新建一个 axios 实例
const service = axios.create({
	baseURL: import.meta.env.VITE_API_URL as any,
	timeout: 50000,
	headers: { 'Content-Type': 'application/json' },
});

// 添加请求拦截器
service.interceptors.request.use(
	(config) => {
		// 在发送请求之前做些什么 token
		if (Session.get('token')) {
			(<any>config.headers).common['Authorization'] = `Bearer ${Session.get('token')}`;
		}
		return config;
	},
	(error) => {
		// 对请求错误做些什么
		return Promise.reject(error);
	}
);

// 添加响应拦截器
service.interceptors.response.use(
	(response) => {
		// 对响应数据做点什么		
		const res: ResponseMessage<any> = response.data;

		if (res.errcode && res.errcode !== 0) {
			// `token` 过期或者账号已在别处登录
			ElNotification({
				title: '提示',
				message: res.msg,
				type: 'error',
				duration: 1500
			});

			return Promise.reject(service.interceptors.response);
		} else {
			/* 为了 typescript 的提示准确，我们不要做任何处理返回。 */
			return response;
		}
	},
	(error) => {
		if (error.response.data.errcode === 401) {
			Session.clear(); // 清除浏览器全部临时缓存
			ElNotification({
				title: '提示',
				message: '你已被登出，请重新登录',
				type: 'error',
				duration: 1500,
				onClose: function () {
					window.location.href = '/login'; // 去登录页
				}
			})
		}
		return Promise.reject(error);
	}
);

// 导出 axios 实例
export default service;
