<template>
	<div class="system-user-container">
		<el-card shadow="hover">
			<div class="system-user-search mb15">
				<el-input size="default" placeholder="请输入用户编号" v-model="param.id" type="number" style="max-width: 150px" />
				<el-input size="default" placeholder="请输入用户名称" v-model="param.username" style="max-width: 170px" class="ml10" /> 
				<el-input size="default" placeholder="请输入用户昵称" v-model="param.nickname" style="max-width: 170px" class="ml10" /> 
				<el-button size="default" type="primary" v-on:click="query" class="ml10">
					<el-icon>
						<ele-Search />
					</el-icon>
					查询
				</el-button>
				<el-button size="default" type="primary" v-on:click="reset" class="ml10">
					<el-icon>
						<ele-Search />
					</el-icon>
					重置
				</el-button>
				<el-button size="default" type="success" class="ml10" @click="onOpenAddUser">
					<el-icon>
						<ele-FolderAdd />
					</el-icon>
					新增用户
				</el-button>
			</div>
			<el-table :data="pageData.list" style="width: 100%">
				<el-table-column prop="id" label="用户编号" show-overflow-tooltip></el-table-column>
				<el-table-column prop="username" label="用户名称" show-overflow-tooltip></el-table-column>
				<el-table-column prop="nickname" label="用户昵称" show-overflow-tooltip></el-table-column>
				<el-table-column label="操作" width="100">
					<template #default="scope">
						<el-button :disabled="scope.row.userName === 'admin'" size="small" text type="primary"
							@click="onOpenEditUser(scope.row)">修改</el-button>
						<el-button :disabled="scope.row.userName === 'admin'" size="small" text type="primary"
							@click="onRowDel(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-pagination @size-change="onHandleSizeChange" @current-change="onHandleCurrentChange" class="mt15"
				:pager-count="5" :page-sizes="[3, 5, 7, 10, 20, 30, 50]" v-model:current-page="param.pageNo" background
				v-model:page-size="param.pageSize" layout="total, sizes, prev, pager, next, jumper"
				:total="pageData.total">
			</el-pagination>
		</el-card>
		<AddUer ref="addUserRef" />
		<EditUser ref="editUserRef" />
	</div>
</template>

<script lang="ts">
import { toRefs, reactive, onMounted, ref, defineComponent } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';
import AddUer from '/@/views/system/user/component/addUser.vue';
import EditUser from '/@/views/system/user/component/editUser.vue';
import { user_api } from '/@/api/user';
import { UserInfoDTO, UserPageQueryVO } from '/@/api/user/interface/user';
import { PageResult } from '/@/api/common/interface/common';

// 定义接口来定义对象的类型
interface TableDataState {
	param: UserPageQueryVO;
	pageData: PageResult<UserInfoDTO>;
	loading: boolean;
}

export default defineComponent({
	name: 'systemUser',
	components: { AddUer, EditUser },
	setup() {
		const addUserRef = ref();
		const editUserRef = ref();
		const state = reactive<TableDataState>({
			param: {
				pageNo: 1,
				pageSize: 3,
				id: null,
				username: null,
				nickname: null,
			},
			loading: false,
			pageData: {
				list: [],
				total: 0,
				pages: 1,
			}
		});
		// 初始化表格数据
		const query = () => {
			state.loading = true;
			user_api.page(state.param).then(res => {
				state.pageData = res.data.data;
				state.loading = false;
			});
		};
		const reset = () => {
			state.param = {
				...state.param,
				id: null,
				username: null,
				nickname: null,
			}
			query();
		}
		// 打开新增用户弹窗
		const onOpenAddUser = () => {
			addUserRef.value.openDialog(query);
		};
		// 打开修改用户弹窗
		const onOpenEditUser = (row: UserInfoDTO) => {
			editUserRef.value.openDialog(row, query);
		};
		// 删除用户
		const onRowDel = (row: UserInfoDTO) => {
			ElMessageBox.confirm(`此操作将永久删除用户：“${row.username}”，是否继续?`, '提示', {
				confirmButtonText: '确认',
				cancelButtonText: '取消',
				type: 'warning',
			}).then(() => {
				if(row.id) {
					user_api.del(row.id).then(() => {
						ElMessage.success('删除成功');
						query();
					});
				}
				
			})
			.catch(() => { });
		};
		// 分页改变
		const onHandleSizeChange = (val: number) => {
			state.param.pageSize = val;
			query();
		};
		// 分页改变
		const onHandleCurrentChange = (val: number) => {
			state.param.pageNo = val;
			query();
		};
		// 页面加载时
		onMounted(() => {
			query();
		});
		return {
			query,
			reset,

			addUserRef,
			editUserRef,
			onOpenAddUser,
			onOpenEditUser,
			onRowDel,
			onHandleSizeChange,
			onHandleCurrentChange,
			...toRefs(state),
		};
	},
});
</script>
