<template>
  <el-main>
    <!-- 搜索新增删除表单 -->
    <el-form
      :inline="true"
      :model="userQueryParams"
      class="demo-form-inline"
      size="mini"
    >
      <el-form-item label="用户名">
        <el-input
          v-model="userQueryParams.username"
          placeholder="请输入用户名"
        ></el-input>
      </el-form-item>
      <el-form-item label="用户昵称">
        <el-input
          v-model="userQueryParams.nickname"
          placeholder="请输入用户昵称"
        ></el-input>
      </el-form-item>
      <el-form-item label="手机号码">
        <el-input
          v-model="userQueryParams.phone"
          placeholder="请输入手机号码"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="userSearch" icon="el-icon-search"
          >查 询</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-refresh-right" @click="resetUserSearchForm"
          >重 置</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button
          type="success"
          icon="el-icon-plus"
          @click="handleOpenAddUserDialog"
          v-if="hasPermission('authority:user:add')"
          >新 增</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button
          type="danger"
          icon="el-icon-delete"
          @click="handleDeleteUser"
          v-if="hasPermission('authority:user:delete')"
          >删 除</el-button
        >
      </el-form-item>
    </el-form>

    <!-- 用户列表表单 -->
    <el-table
      :data="userTableData"
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <!-- 复选框 -->
      <el-table-column
        type="selection"
        width="55"
        align="center"
        fixed
      ></el-table-column>
      <!-- 用户名 -->
      <el-table-column
        prop="username"
        label="用户名"
        width="150"
        align="center"
        fixed
      >
      </el-table-column>
      <!-- 用户昵称 -->
      <el-table-column
        prop="nickname"
        label="用户昵称"
        width="150"
        align="center"
      >
      </el-table-column>
      <!-- 角色 -->
      <el-table-column prop="roles" label="角色" align="center" width="250">
        <template slot-scope="{ row }">
          <el-tag
            style="margin-left: 3px"
            type="success"
            v-for="(item, index) in row.roles"
            :key="index"
            >{{ item.name }}</el-tag
          >
        </template>
      </el-table-column>
      <!-- 头像 -->
      <el-table-column prop="avatar" label="头像" align="center" width="120">
        <template slot-scope="{ row }">
          <img :src="row.avatar" alt="" style="width: 70px; height: 70px" />
        </template>
      </el-table-column>
      <!-- 用户状态 -->
      <el-table-column prop="status" label="状态" align="center" width="120">
        <template slot-scope="{ row }">
          <el-switch
            v-model="row.status"
            active-value="0"
            inactive-value="1"
            @change="changeUserStatus(row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <!-- 手机 -->
      <el-table-column prop="phone" label="手机" align="center" width="120">
      </el-table-column>
      <!-- 微信 -->
      <el-table-column prop="wechat" label="微信" align="center" width="120">
      </el-table-column>
      <!-- 邮箱 -->
      <el-table-column prop="email" label="邮箱" align="center" width="190">
      </el-table-column>
      <!-- 性别 -->
      <el-table-column prop="gender" label="性别" width="70" align="center">
        <template slot-scope="{ row }">
          <div>{{ getGender(row) }}</div>
        </template>
      </el-table-column>
      <!-- 创建时间 -->
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="180"
        :formatter="dateFormat"
        align="center"
      >
      </el-table-column>
      <!-- 操作 -->
      <el-table-column
        prop="operation"
        label="操作"
        align="center"
        fixed="right"
        width="300"
      >
        <template slot-scope="{ row }">
          <el-button
            type="warning"
            plain
            icon="el-icon-edit"
            size="mini"
            @click="handleOpenUpdateUserDialog(row)"
            v-if="hasPermission('authority:user:edit')"
            >修改</el-button
          >
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            @click="handleDeleteUser(row)"
            v-if="hasPermission('authority:user:delete')"
            >删除</el-button
          >
          <el-button
            type="info"
            plain
            icon="el-icon-setting"
            size="mini"
            @click="handleResetPwd(row)"
            v-if="hasPermission('authority:user:resetpwd')"
            >重置密码</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      style="margin-top: 15px; textalign: center"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="userQueryParams.pageNum"
      :page-sizes="[5, 10, 20, 30]"
      :page-size="userQueryParams.pageSize"
      layout="sizes, total, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>

    <!-- 新增或者修改用户对话框 -->
    <el-dialog
      :title="addUpdateUserDialogTitle"
      :visible.sync="addUpdateUserdialogFormVisible"
      width="50%"
    >
      <el-form
        :model="userData"
        :rules="addUpdateUserRules"
        ref="addUpdateUserForm"
      >
        <el-row>
          <el-col :span="11">
            <!-- 用户名 -->
            <el-form-item
              label="用户名"
              :label-width="addUpdateUserformLabelWidth"
              prop="username"
            >
              <el-input
                v-model="userData.username"
                autocomplete="off"
                maxlength="18"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <!-- 昵称 -->
            <el-form-item
              label="昵称"
              :label-width="addUpdateUserformLabelWidth"
              prop="nickname"
            >
              <el-input
                v-model="userData.nickname"
                autocomplete="off"
                maxlength="18"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="11">
            <!-- 密码 -->
            <el-form-item
              label="密码"
              :label-width="addUpdateUserformLabelWidth"
              prop="password"
              v-if="userData.userId == undefined"
            >
              <el-input
                v-model="userData.password"
                autocomplete="off"
                type="password"
                maxlength="18"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <!-- 邮箱 -->
            <el-form-item
              label="邮箱"
              :label-width="addUpdateUserformLabelWidth"
              prop="email"
            >
              <el-input v-model="userData.email" autocomplete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="11">
            <!-- 手机号码 -->
            <el-form-item
              label="手机"
              :label-width="addUpdateUserformLabelWidth"
              prop="phone"
            >
              <el-input
                v-model="userData.phone"
                autocomplete="off"
                maxlength="11"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <!-- 微信 -->
            <el-form-item
              label="微信"
              :label-width="addUpdateUserformLabelWidth"
              prop="wechat"
            >
              <el-input
                v-model="userData.wechat"
                autocomplete="off"
                maxlength="20"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="11">
            <!-- 角色 -->
            <el-form-item
              label="角色"
              :label-width="addUpdateUserformLabelWidth"
              prop="roleIds"
            >
              <el-select
                v-model="userData.roleIds"
                multiple
                placeholder="请选择用户角色"
              >
                <el-option
                  v-for="item in roleOptions"
                  :key="item.roleSort"
                  :label="item.name"
                  :value="item.roleId"
                  :disabled="item.status == 1"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="11">
            <!-- 性别 -->
            <el-form-item
              label="性别"
              :label-width="addUpdateUserformLabelWidth"
              prop="gender"
            >
              <el-select v-model="userData.gender" placeholder="请选择用户性别">
                <el-option
                  v-for="item in genderOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="11">
            <!-- 头像 -->
            <el-form-item
              label="头像"
              :label-width="addUpdateUserformLabelWidth"
            >
              <!-- 上传组件 -->
              <el-upload
                class="avatar-uploader"
                action="https://y3lq-blog.oss-cn-shenzhen.aliyuncs.com"
                :data="uploadAvatarData"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <img
                  v-if="userData.avatar"
                  :src="userData.avatar"
                  style="width: 60px; height: 60px"
                  class="avatar"
                />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                <div slot="tip" class="el-upload__tip">
                  只能上传jpg/png文件，且不超过500kb
                </div>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <!-- 状态 -->
            <el-form-item
              label="状态"
              :label-width="addUpdateUserformLabelWidth"
              prop="status"
            >
              <el-radio-group v-model="userData.status">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="22">
            <!-- 备注 -->
            <el-form-item
              label="备注"
              :label-width="addUpdateUserformLabelWidth"
              prop="remark"
            >
              <el-input
                v-model="userData.remark"
                autocomplete="off"
                maxlength="15"
                type="textarea"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="addUpdateUserdialogFormVisible = false"
          >取 消</el-button
        >
        <el-button type="primary" @click="handlerInsertUpdateUser"
          >提 交</el-button
        >
      </div>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog
      title="修改密码"
      :visible.sync="resetPwdDialogFormVisible"
      width="40%"
    >
      <el-form :model="resetPwdData" :rules="resetPwdRules" ref="resetPwdForm">
        <el-form-item label="请输入新密码" label-width="120px" prop="password">
          <el-input
            v-model="resetPwdData.password"
            autocomplete="off"
            type="password"
            style="width: 85%"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetPwdDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="resetPwd">提 交</el-button>
      </div>
    </el-dialog>
  </el-main>
</template>

<script>
import { getUserList } from "@/api/user";
import { changeUserStatus } from "@/api/user";
import moment from "moment";
import {
  getSignature,
  insertUser,
  getDialogRoleData,
  deleteUser,
  getUserInfo,
  updateUser,
  resetUserPwd,
} from "@/api/user";
import { v4 } from "uuid";
export default {
  name: "User",

  data() {
    return {
      // 选择的userId数组
      selectUserIds: [],
      // 重置密码参数
      resetPwdData: {
        userId: undefined,
        password: undefined,
      },
      resetPwdDialogFormVisible: false,
      // 新增修改用户对话框标题
      addUpdateUserDialogTitle: "",
      // 上传头像相关参数
      uploadAvatarData: {
        policy: "",
        signature: "",
        key: "",
        ossaccessKeyId: "",
        dir: "",
        host: "",
      },
      // 用户状态选项
      statusOptions: [
        {
          value: 0,
          label: "正常",
        },
        {
          value: 1,
          label: "停用",
        },
      ],
      // 角色选项
      roleOptions: [],
      // 性别选项
      genderOptions: [
        {
          value: "0",
          label: "女",
        },
        {
          value: "1",
          label: "男",
        },
        {
          value: "2",
          label: "未知",
        },
      ],
      // 修改密码校验
      resetPwdRules: {
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
          {
            min: 2,
            max: 18,
            message: "密码长度应在[2, 18]",
            trigger: "blur",
          },
        ],
      },
      // 新增修改修改用户表单校验
      addUpdateUserRules: {
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
          {
            min: 2,
            max: 18,
            message: "用户名长度应在[2, 18]",
            trigger: "blur",
          },
        ],
        nickname: [
          { required: true, message: "昵称不能为空", trigger: "blur" },
          {
            min: 2,
            max: 18,
            message: "昵称长度应在[2, 18]",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" },
          {
            min: 2,
            max: 18,
            message: "密码长度应在[2, 18]",
            trigger: "blur",
          },
        ],
        email: [
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        phone: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],
        wechat: [
          {
            min: 6,
            max: 20,
            message: "微信账号长度应在[6, 20]",
            trigger: "blur",
          },
        ],
        roleIds: [
          { required: true, message: "必须给用户分配角色", trigger: "blur" },
        ],
        status: [
          { required: true, message: "必须设置用户状态", trigger: "blur" },
        ],
      },
      // 新增修改用户对话框隐藏显示
      addUpdateUserdialogFormVisible: false,
      // 新增修改用户label宽度
      addUpdateUserformLabelWidth: "85px",

      // 查询用户相关参数
      userQueryParams: {
        pageNum: 1,
        pageSize: 5,
        username: undefined,
        nickname: undefined,
        phone: undefined,
      },
      total: 0,
      // 用户表单
      userTableData: [],
      // 单个用户
      userData: {
        userId: undefined,
        username: undefined,
        nickname: undefined,
        roleIds: undefined,
        wechat: undefined,
        password: undefined,
        email: undefined,
        phone: undefined,
        gender: undefined,
        avatar: undefined,
        status: undefined,
      },
    };
  },

  mounted() {
    this.userList();
  },

  methods: {
    // 复选框改变保存对应的userId
    handleSelectionChange(selection) {
      this.selectUserIds = selection.map((item) => item.userId);
    },
    // 修改密码
    resetPwd() {
      this.$refs["resetPwdForm"].validate((valid) => {
        if (valid) {
          resetUserPwd(this.resetPwdData).then((res) => {
            if (res.code == 200) {
              this.$message({
                message: "重置密码成功",
                type: "success",
              });
            }
          });
          this.resetPwdDialogFormVisible = false;
        }
      });
    },
    // 打开修改密码对话框
    handleResetPwd(row) {
      if (this.$refs.resetPwdForm) {
        this.$refs.resetPwdForm.resetFields();
      }
      this.resetPwdData = {
        userId: undefined,
        password: undefined,
      };
      this.resetPwdData.userId = row.userId;
      this.resetPwdDialogFormVisible = true;
    },
    // 获取性别
    getGender(row) {
      if (row.gender == 0) {
        return "女";
      }
      if (row.gender == 1) {
        return "男";
      }
      return "未知";
    },
    // 时间格式化
    dateFormat: function (row, column) {
      var date = row[column.property];
      if (date === undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    // 删除用户
    handleDeleteUser(row) {
      const userIds = row.userId || this.selectUserIds;

      this.$confirm("确定要删除" + row.nickname + "吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteUser(userIds).then((res) => {
            if (res.code == 200) {
              this.$message({
                message: "删除成功",
                type: "success",
              });
              this.userList();
            }else{
              this.$message.error(res.msg)
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    // 打开修改用户信息的对话框
    handleOpenUpdateUserDialog(row) {
      if (this.$refs.addUpdateUserForm) {
        this.$refs.addUpdateUserForm.resetFields();
      }
      this.resetUserData();
      getDialogRoleData().then((res) => {
        this.roleOptions = res.data;
      });
      getUserInfo(row.userId).then((res) => {
        this.userData.userId = res.data.userId;
        this.userData.username = res.data.username;
        this.userData.nickname = res.data.nickname;
        this.userData.wechat = res.data.wechat;
        this.userData.email = res.data.email;
        this.userData.phone = res.data.phone;
        this.userData.gender = res.data.gender;
        this.userData.avatar = res.data.avatar;
        this.userData.status = res.data.status;
        this.userData.roleIds = res.data.roleIds;
        this.addUpdateUserdialogFormVisible = true;
        this.addUpdateUserDialogTitle = "修改[" + res.data.nickname + "]信息";
      });
    },
    // 打开新增用户对话框处理
    handleOpenAddUserDialog() {
      if (this.$refs.addUpdateUserForm) {
        this.$refs.addUpdateUserForm.resetFields();
      }
      this.resetUserData();
      getDialogRoleData().then((res) => {
        this.roleOptions = res.data;
      });
      this.addUpdateUserdialogFormVisible = true;
      this.addUpdateUserDialogTitle = "新增用户";
    },
    // 重置用户数据
    resetUserData() {
      this.userData = {
        userId: undefined,
        username: undefined,
        nickname: undefined,
        roleIds: undefined,
        wechat: undefined,
        password: undefined,
        email: undefined,
        phone: undefined,
        gender: undefined,
        avatar: undefined,
        status: undefined,
      };
    },
    // 处理新增修改用户
    handlerInsertUpdateUser() {
      console.log(this.userData);
      this.$refs["addUpdateUserForm"].validate((valid) => {
        if (valid) {
          if (this.userData.userId === undefined) {
            insertUser(this.userData).then((res) => {
              if (res.code == 200) {
                this.addUpdateUserdialogFormVisible = false;
                this.resetUserData();
                this.userList();
                this.$message({
                  message: "新增成功",
                  type: "success",
                });
              } else {
                this.addUpdateUserdialogFormVisible = false;
                this.resetUserData();
                this.$message.error(res.msg);
              }
            });
          } else {
            updateUser(this.userData).then((res) => {
              if (res.code == 200) {
                this.addUpdateUserdialogFormVisible = false;
                this.resetUserData();
                this.userList();
                this.$message({
                  message: "修改成功",
                  type: "success",
                });
              } else {
                this.addUpdateUserdialogFormVisible = false;
                this.resetUserData();
                this.$message.error(res.msg);
              }
            });
          }
        }
      });
    },
    // 上传头像成功回调
    handleAvatarSuccess(res, file) {
      this.userData.avatar =
        "https://y3lq-blog.oss-cn-shenzhen.aliyuncs.com/" +
        this.uploadAvatarData.key;
    },
    // 上传头像前的检查并且获取上传签名
    async beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      // 获取上传文件到aliyun oss的签名
      let _self = this;
      await getSignature().then((res) => {
        if (res.code == 200) {
          _self.uploadAvatarData.policy = res.data.policy;
          _self.uploadAvatarData.signature = res.data.signature;
          _self.uploadAvatarData.ossaccessKeyId = res.data.accessid;
          _self.uploadAvatarData.key = res.data.dir + v4();
          _self.uploadAvatarData.dir = res.data.dir;
          _self.uploadAvatarData.host = res.data.host;
        }
      });
      return isJPG && isLt2M;
    },
    // 改变用户状态
    changeUserStatus(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$confirm(
        '确认要"' + text + '" "' + row.nickname + '"用户吗？',
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          changeUserStatus(row.userId, row.status)
            .then((res) => {
              this.$message({
                type: "success",
                message: text + "成功",
              });
            })
            .catch(function () {
              row.status = row.status === "0" ? "1" : "0";
            });
        })
        .catch(() => {
          row.status = row.status === "0" ? "1" : "0";
          this.$message({
            type: "info",
            message: "已取消" + text,
          });
        });
    },
    // 重置搜索用户表单参数
    resetUserSearchForm() {
      this.userQueryParams = {
        username: undefined,
        nickname: undefined,
        phone: undefined,
        pageNum: 1,
        pageSize: 2,
      };
      this.userList();
    },

    // 条件搜索用户
    userSearch() {
      this.userList();
    },
    // pageSize改变触发
    handleSizeChange(pageSize) {
      this.userQueryParams.pageSize = pageSize;
      this.userList();
    },
    // 当前页码改变触发
    handleCurrentChange(pageNum) {
      this.userQueryParams.pageNum = pageNum;
      this.userList();
    },
    // 获取用户列表
    userList() {
      getUserList(this.userQueryParams).then((res) => {
        if (res.code == 200) {
          this.userTableData = res.data.list;
          this.total = res.data.total;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
</style>