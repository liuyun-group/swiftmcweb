<template>
	<div class="system-edit-user-container">
		<el-dialog title="修改用户" v-model="isShowDialog" width="769px">
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
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
            <el-upload
                class="avatar-uploader"
                v-bind:action="UPLOAD_FILE_CONFIG.action"
                v-bind:headers="UPLOAD_FILE_CONFIG.headers"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
              <img v-if="headPicUrl" :src="headPicUrl" class="avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon">+</i>
            </el-upload>
          </el-col>
				</el-row>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="onCancel" size="default">取 消</el-button>
					<el-button type="primary" @click="onSubmit" size="default">修 改</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script lang="ts">
import { ElMessage } from 'element-plus';
import { reactive, toRefs, onMounted, defineComponent } from 'vue';
import { user_api } from '/@/api/user';
import {UserDetailVO, UserInfoDTO} from '/@/api/user/interface/user';
import other from "/@/utils/other";
import {dec_static_file_url, gen_static_file_url} from "/@/utils/static-file";
import {Session} from "/@/utils/storage";
import {file_upload_api} from "/@/api/file";

// 定义接口来定义对象的类型
interface UserState {
	isShowDialog: boolean;
	ruleForm: UserDetailVO;
  headPicUrl: string;
}

export default defineComponent({
	name: 'systemEditUser',
	setup() {
		const state = reactive<UserState>({
			isShowDialog: false,
			ruleForm: {
				id: null,
				username: '',
				nickname: '',
				password: '',
        headPic: '',
			},
      headPicUrl: '',
		});
    const UPLOAD_FILE_CONFIG = {
      headers: {
        Authorization: 'Bearer ' + Session.get('token'),
      },
      action: file_upload_api.file_upload_uri
    }
		let query = () => {};
		// 打开弹窗
		const openDialog = (row: UserInfoDTO, _query:() => {}) => {
      if(row.headPic) {
        state.headPicUrl = gen_static_file_url(row.headPic);
      }
      state.ruleForm = {
        id: row.id,
        username: row.username ? row.username : '',
        nickname: row.nickname ? row.nickname : '',
        headPic: row.headPic ? row.headPic : '',
        password: '',
      }
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
		// 更新
		const onSubmit = () => {
      state.ruleForm.headPic = dec_static_file_url(state.headPicUrl);
			user_api.add_update(state.ruleForm).then(() => {
				ElMessage.success(`修改用户【${state.ruleForm.username}】成功！`);
				closeDialog();
				query();
			});
		};
    /* 头像上传成功处理 */
    const handleAvatarSuccess = (res: string, file: File) => {
      state.headPicUrl = gen_static_file_url(res);
    }
    /* 头像上传前置处理 */
    const beforeAvatarUpload = (file: File) => {
      const isJPG = file.type === 'image/jpeg';
      const isPng = file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG && !isPng) {
        ElMessage.error('上传头像图片只能是 JPG/PNG 格式!');
      }
      if (!isLt2M) {
        ElMessage.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    }
		// 页面加载时
		onMounted(() => {
			
		});
		return {
      handleAvatarSuccess,
      beforeAvatarUpload,
      UPLOAD_FILE_CONFIG,

			openDialog,
			closeDialog,
			onCancel,
			onSubmit,
			...toRefs(state),
		};
	},
});
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 37px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
