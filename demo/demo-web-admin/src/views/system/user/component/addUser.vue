<template>
	<div class="system-add-user-container">
		<el-dialog title="新增用户" v-model="isShowDialog" width="769px">
			<el-form :model="ruleForm" size="default" label-width="90px">
				<el-row :gutter="35">
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="用户名称">
							<el-input v-model="ruleForm.username" placeholder="请输入账户名称" clearable></el-input>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="用户密码">
							<el-input v-model="ruleForm.password" placeholder="请输入用户密码" clearable></el-input>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="mb20">
						<el-form-item label="用户昵称">
							<el-input v-model="ruleForm.nickname" placeholder="请输入用户昵称" clearable></el-input>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="onCancel" size="default">取 消</el-button>
					<el-button type="primary" @click="onSubmit" size="default">新 增</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script lang="ts">
import { ElMessage } from 'element-plus';
import { reactive, toRefs, onMounted, defineComponent } from 'vue';
import { user_api } from '/@/api/user';
import { UserDetailVO } from '/@/api/user/interface/user';

// 定义接口来定义对象的类型
interface UserState {
	isShowDialog: boolean;
	ruleForm: UserDetailVO;
}

export default defineComponent({
	name: 'systemAddUser',
	setup() {
		const state = reactive<UserState>({
			isShowDialog: false,
			ruleForm: {
				id: null,
				username: '',
				nickname: '',
				password: '',
			},
		});
		let query = () => {};
		// 打开弹窗
		const openDialog = (_query:() => {}) => {
			state.isShowDialog = true;
			query = _query;
		};
		// 关闭弹窗
		const closeDialog = () => {
			state.isShowDialog = false;
		};
		// 取消
		const onCancel = () => {
			closeDialog();
		};
		// 新增
		const onSubmit = () => {
			user_api.add_update(state.ruleForm).then(() => {
				ElMessage.success(`新增用户【${state.ruleForm.username}】成功！`);
				closeDialog();
				query();
			});
		};
		// 页面加载时
		onMounted(() => {
			
		});
		return {
			openDialog,
			closeDialog,
			onCancel,
			onSubmit,
			...toRefs(state),
		};
	},
});
</script>
