<template>
	<el-form size="large" class="login-content-form" ref="ref_rule_form">
		<el-form-item class="login-animation1">
			<el-input type="text" :placeholder="$t('message.account.accountPlaceholder1')" v-model="ruleForm.userName" clearable autocomplete="off">
				<template #prefix>
					<el-icon class="el-input__icon"><ele-User /></el-icon>
				</template>
			</el-input>
		</el-form-item>
		<el-form-item class="login-animation2">
			<el-input
				:type="isShowPassword ? 'text' : 'password'"
				:placeholder="$t('message.account.accountPlaceholder2')"
				v-model="ruleForm.password"
				autocomplete="off"
			>
				<template #prefix>
					<el-icon class="el-input__icon"><ele-Unlock /></el-icon>
				</template>
				<template #suffix>
					<i
						class="iconfont el-input__icon login-content-password"
						:class="isShowPassword ? 'icon-yincangmima' : 'icon-xianshimima'"
						@click="isShowPassword = !isShowPassword"
					>
					</i>
				</template>
			</el-input>
		</el-form-item>
		<el-form-item class="login-animation3">
			<el-col :span="15">
				<el-input
					type="text"
					minlength="5"
					maxlength="5"
					:placeholder="$t('message.account.accountPlaceholder3')"
					v-model="ruleForm.code"
					clearable
					autocomplete="off"
				>
					<template #prefix>
						<el-icon class="el-input__icon"><ele-Position /></el-icon>
					</template>
				</el-input>
			</el-col>
			<el-col :span="1"></el-col>
			<el-col :span="8">
				<el-image :src="captcha_image.img" fit="contain" class="login-image-code" @click="get_captcha_image" />
			</el-col>
		</el-form-item>
		<el-form-item class="login-animation4">
			<el-button type="primary" class="login-content-submit" round @click="onSignIn" :loading="loading.signIn">
				<span>{{ $t('message.account.accountBtnText') }}</span>
			</el-button>
		</el-form-item>
	</el-form>
</template>

<script lang="ts">
import { toRefs, reactive, defineComponent, computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, FormInstance } from 'element-plus';
import { useI18n } from 'vue-i18n';
import Cookies from 'js-cookie';
import { initFrontEndControlRoutes } from '/@/router/frontEnd';
import { Session } from '/@/utils/storage';
import { formatAxis } from '/@/utils/formatTime';
import { NextLoading } from '/@/utils/loading';
import { login_api } from '/@/api/login';

export default defineComponent({
	name: 'loginAccount',
	setup() {
		const { t } = useI18n();
		const route = useRoute();
		const router = useRouter();
		const state = reactive({
			isShowPassword: false,
			ruleForm: {
				userName: 'admin',
				password: '1002',
				code: '',
			},
			captcha_image: {
				img: '',
				session: '',
			},
			loading: {
				signIn: false,
			},
		});

		const ref_rule_form = ref<FormInstance>();

		// 时间获取
		const currentTime = computed(() => {
			return formatAxis(new Date());
		});

		/* 获取验证码 */
		const get_captcha_image = () => {
			login_api.captcha_image().then((res) => {
				const cap = res.data.data;		
				state.captcha_image = {
					img: cap.base64Image,
					session: cap.session,
				}
			});	
		}

		// 登录
		const onSignIn = async () => {
			if(!ref_rule_form.value) {
				return;
			}

			/* 调用登录接口 */
			state.loading.signIn = true;
			login_api.sign_in({
				username: state.ruleForm.userName,
				password: state.ruleForm.password,
				session: state.captcha_image.session,
				code: state.ruleForm.code,
			}).then(async (res) => {
				const login_result = res.data.data;
				// 存储 token 到浏览器缓存
				Session.set('token', login_result.accessToken);
				// 模拟数据，对接接口时，记得删除多余代码及对应依赖的引入。用于 `/src/stores/userInfo.ts` 中不同用户登录判断（模拟数据）
				Cookies.set('userName', state.ruleForm.userName);
				Cookies.set('userId', login_result.userId);
				await initFrontEndControlRoutes();
				signInSuccess();
			}).catch(() => {
				get_captcha_image();
				state.loading.signIn = false;
			});
		};

		// 登录成功后的跳转
		const signInSuccess = () => {
			// 初始化登录成功时间问候语
			let currentTimeInfo = currentTime.value;
			// 登录成功，跳到转首页
			// 如果是复制粘贴的路径，非首页/登录页，那么登录成功后重定向到对应的路径中
			if (route.query?.redirect) {
				router.push({
					path: <string>route.query?.redirect,
					query: Object.keys(<string>route.query?.params).length > 0 ? JSON.parse(<string>route.query?.params) : '',
				});
			} else {
				router.push('/');
			}
			// 登录成功提示
			// 关闭 loading
			state.loading.signIn = true;
			const signInText = t('message.signInText');
			ElMessage.success(`${currentTimeInfo}，${signInText}`);
			// 添加 loading，防止第一次进入界面时出现短暂空白
			NextLoading.start();
		};

		onMounted(() => {
			get_captcha_image();

			/* listener of `ENTER` for login */
			document.onkeydown = (e) => {
				if (e.code === 'Enter') {
					onSignIn();
				}
			}

		});

		return {
			ref_rule_form,

			onSignIn,
			get_captcha_image,
			...toRefs(state),
		};
	},
});
</script>

<style scoped lang="scss">
.login-content-form {
	margin-top: 20px;
	@for $i from 1 through 4 {
		.login-animation#{$i} {
			opacity: 0;
			animation-name: error-num;
			animation-duration: 0.5s;
			animation-fill-mode: forwards;
			animation-delay: calc($i/10) + s;
		}
	}
	.login-content-password {
		display: inline-block;
		width: 20px;
		cursor: pointer;
		&:hover {
			color: #909399;
		}
	}
	.login-content-code {
		width: 100%;
		padding: 0;
		font-weight: bold;
		letter-spacing: 5px;
	}
	.login-content-submit {
		width: 100%;
		letter-spacing: 2px;
		font-weight: 300;
		margin-top: 15px;
	}
}
</style>
