<template>
  <el-main>
    <!-- 搜索新增删除按钮 -->
    <el-form
      :inline="true"
      :model="roleQueryParams"
      class="demo-form-inline"
      size="mini"
      
    >
      <el-form-item label="角色名称">
        <el-input
          v-model="roleQueryParams.name"
          placeholder="请输入角色名称"
        ></el-input>
      </el-form-item>
      <el-form-item label="权限字符">
        <el-input
          v-model="roleQueryParams.roleKey"
          placeholder="请输入权限字符"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="roleList"
          >查 询</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-refresh-right" @click="resetRoleSearchForm"
          >重 置</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button
          type="success"
          icon="el-icon-plus"
          @click="handleOpenAddRoleDialog"
          v-if="hasPermission('authority:role:add')"
          >新 增</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button
          type="danger"
          icon="el-icon-delete"
          @click="handleDeleteRole"
          v-if="hasPermission('authority:role:delete')"
          >删 除</el-button
        >
      </el-form-item>
    </el-form>

    <!-- 角色列表表单 -->
    <el-table
      :data="roleTableData"
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
      <!-- 角色名称 -->
      <el-table-column
        prop="name"
        label="角色名称"
        width="140"
        align="center"
        fixed
      >
      </el-table-column>
      <!-- 权限字符 -->
      <el-table-column
        prop="roleKey"
        label="权限字符"
        width="140"
        align="center"
      >
      </el-table-column>

      <!-- 显示顺序 -->
      <el-table-column
        prop="roleSort"
        label="显示顺序"
        width="140"
        align="center"
      >
      </el-table-column>
      <!-- 角色状态 -->
      <el-table-column prop="status" label="状态" align="center" width="140">
        <template slot-scope="{ row }">
          <el-switch
            v-model="row.status"
            active-value="0"
            inactive-value="1"
            @change="changeRoleStatus(row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <!-- 备注 -->
      <el-table-column prop="remark" label="备注" width="250" align="center">
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
        width="300"
        fixed="right"
      >
        <template slot-scope="{ row }">
          <el-button
            type="warning"
            plain
            icon="el-icon-edit"
            size="mini"
            @click="handleOpenUpdateRoleDialog(row)"
            v-if="hasPermission('authority:role:edit')"
            >修改</el-button
          >
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            @click="handleDeleteRole(row)"
            v-if="hasPermission('authority:role:delete')"
            >删除</el-button
          >
          <el-button
            type="info"
            plain
            icon="el-icon-setting"
            size="mini"
            @click="handleAssignUser(row)"
            v-if="hasPermission('authority:role:edit')"
            >分配用户</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      style="margin-top: 15px; textalign: center"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="roleQueryParams.pageNum"
      :page-sizes="[5, 10, 20]"
      :page-size="roleQueryParams.pageSize"
      layout="sizes, total, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>

    <!-- 添加或修改角色对话框 -->
    <el-dialog
      :title="addUpdateRoleDialogTitle"
      :visible.sync="addUpdateRoleDialogFormVisible"
      width="33%"
      append-to-body
    >
      <el-form
        :model="roleData"
        :rules="addUpdateRoleRules"
        ref="addUpdateRoleForm"
      >
        <!-- 角色名字 -->
        <el-form-item
          label="角色名字"
          :label-width="addUpdateFormLabelWidth"
          style="width: 410px"
          prop="name"
        >
          <el-input v-model="roleData.name" autocomplete="off"></el-input>
        </el-form-item>
        <!-- 权限字符 -->
        <el-form-item
          label="权限字符"
          :label-width="addUpdateFormLabelWidth"
          style="width: 410px"
          prop="roleKey"
        >
          <el-input v-model="roleData.roleKey" autocomplete="off"></el-input>
        </el-form-item>
        <!-- 角色顺序 -->
        <el-form-item
          label="角色顺序"
          :label-width="addUpdateFormLabelWidth"
          prop="roleSort"
        >
          <el-input-number
            v-model="roleData.roleSort"
            controls-position="right"
            :min="0"
            :max="1000"
          ></el-input-number>
        </el-form-item>
        <!-- 状态 -->
        <el-form-item
          label="状态"
          :label-width="addUpdateFormLabelWidth"
          prop="status"
        >
          <el-radio-group v-model="roleData.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 菜单权限 -->
        <el-form-item
          label="菜单权限"
          :label-width="addUpdateFormLabelWidth"
          style="width: 410px"
          prop="menus"
        >
          <el-checkbox
            v-model="menuExpand"
            @change="handleCheckedTreeExpand($event)"
            >展开/折叠</el-checkbox
          >
          <el-checkbox
            v-model="menuNodeAll"
            @change="handleCheckedTreeNodeAll($event)"
            >全选/全不选</el-checkbox
          >
          <el-checkbox
            v-model="roleData.menuCheckStrictly"
            @change="handleCheckedTreeConnent($event)"
            >父子联动</el-checkbox
          >
          <el-tree
            :data="menuOptions"
            show-checkbox
            node-key="id"
            ref="menu"
            :check-strictly="!roleData.menuCheckStrictly"
            :props="defaultProps"
            empty-text="加载中，请稍候"
          >
          </el-tree>
        </el-form-item>
        <!-- 备注 -->
        <el-form-item
          label="备注"
          :label-width="addUpdateFormLabelWidth"
          style="width: 410px"
        >
          <el-input
            v-model="roleData.remark"
            autocomplete="off"
            type="textarea"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addUpdateRoleDialogFormVisible = false"
          >取 消</el-button
        >
        <el-button type="primary" @click="handleInsertUpdateRole"
          >提 交</el-button
        >
      </div>
    </el-dialog>

    <!-- 给角色分配用户对话框 -->
    <el-dialog
      title="分配用户"
      :visible.sync="assignUserDialogTableVisible"
      width="71.6%"
    >
      <!-- 搜索新增取消授权按钮 -->
      <el-form
        :inline="true"
        :model="assignUserQueryParams"
        class="demo-form-inline"
        size="mini"
      >
        <el-form-item label="用户名">
          <el-input
            v-model="assignUserQueryParams.username"
            placeholder="请输入用户名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号码">
          <el-input
            v-model="assignUserQueryParams.phone"
            placeholder="请输入手机号码"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            @click="toAssignUserList"
            >查 询</el-button
          >
        </el-form-item>
        <el-form-item>
          <el-button
            icon="el-icon-refresh-right"
            @click="resetAssignUserQueryParams"
            >重 置</el-button
          >
        </el-form-item>
        <el-form-item>
          <el-button
            type="success"
            icon="el-icon-plus"
            @click="handleAddUserToRole"
            >新 增</el-button
          >
        </el-form-item>
        <el-form-item>
          <el-button
            type="danger"
            icon="el-icon-close"
            @click="handleCancelAuth"
            >取 消 授 权</el-button
          >
        </el-form-item>
      </el-form>
      <!-- 已经分配给该角色用户表格 -->
      <el-table
        :data="assignUserList"
        stripe
        style="width: 100%"
        @selection-change="handleCancelAuthSelectionChange"
      >
        <!-- 复选框 -->
        <el-table-column
          type="selection"
          width="55"
          align="center"
          fixed
        ></el-table-column>
        <el-table-column
          property="username"
          label="用户名"
          width="150"
          align="center"
          fixed=""
        ></el-table-column>
        <el-table-column
          property="nickname"
          label="用户昵称"
          width="150"
          align="center"
        ></el-table-column>
        <el-table-column
          property="phone"
          label="手机号码"
          width="150"
          align="center"
        ></el-table-column>
        <el-table-column
          property="status"
          label="状态"
          width="150"
          align="center"
        >
          <template slot-scope="{ row }">
            <div>
              {{ row.status == "0" ? "正常" : "停用" }}
            </div>
          </template>
        </el-table-column>
        <!-- 创建时间 -->
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="170"
          :formatter="dateFormat"
          align="center"
        >
        </el-table-column>
        <el-table-column
          property="operation"
          label="操作"
          width="170"
          align="center"
        >
          <template slot-scope="{ row }">
            <div>
              <el-button
                type="text"
                icon="el-icon-close"
                size="mini"
                @click="handleCancelAuth(row)"
                >取消授权</el-button
              >
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页器 -->
      <el-pagination
        style="margin-top: 15px; textalign: center"
        @size-change="handleSizeChangeOfAssignUser"
        @current-change="handleCurrentChangeOfAssignUser"
        :current-page="assignUserQueryParams.pageNum"
        :page-sizes="[5, 10, 20]"
        :page-size="assignUserQueryParams.pageSize"
        layout="sizes, total, prev, pager, next, jumper"
        :total="assignUserQueryParams.total"
      >
      </el-pagination>

      <!-- 给为分配该角色的用户分配角色内层对话框 -->
      <el-dialog
        width="60%"
        title="选择用户"
        :visible.sync="innerVisible"
        append-to-body
      >
        <!-- 查询按钮 -->
        <el-form
          :inline="true"
          :model="assignUserQueryParams"
          class="demo-form-inline"
          size="mini"
        >
          <el-form-item label="用户名">
            <el-input
              v-model="unassignUserQueryParams.username"
              placeholder="请输入用户名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="手机号码">
            <el-input
              v-model="unassignUserQueryParams.phone"
              placeholder="请输入手机号码"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              @click="getNoAssignedUser"
              >查 询</el-button
            >
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-refresh-right"
              @click="resetUnAssignUserQueryParams"
              >重 置</el-button
            >
          </el-form-item>
        </el-form>
        <!-- 未分配角色用户表单 -->
        <el-table
          :data="unassignUserList"
          style="width: 100%"
          @selection-change="handleAuthRoleSelectChange"
          stripe
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
            property="username"
            label="用户名"
            width="150"
            align="center"
            fixed
          ></el-table-column>
          <!-- 用户昵称 -->
          <el-table-column
            property="nickname"
            label="用户昵称"
            width="150"
            align="center"
          ></el-table-column>
          <!-- 手机号码 -->
          <el-table-column
            property="phone"
            label="手机号码"
            width="150"
            align="center"
          ></el-table-column>
          <!-- 状态 -->
          <el-table-column
            property="status"
            label="状态"
            width="145"
            align="center"
          >
            <template slot-scope="{ row }">
              <div>
                {{ row.status == "0" ? "正常" : "停用" }}
              </div>
            </template>
          </el-table-column>
          <!-- 创建时间 -->
          <el-table-column
            prop="createTime"
            label="创建时间"
            width="170"
            :formatter="dateFormat"
            align="center"
          >
          </el-table-column>
        </el-table>
        <!-- 分页器 -->
        <el-pagination
          style="margin-top: 15px; textalign: center"
          @size-change="handleSizeChangeOfUnAssignUser"
          @current-change="handleCurrentChangeOfUnAssignUser"
          :current-page="unassignUserQueryParams.pageNum"
          :page-sizes="[5, 10, 20]"
          :page-size="unassignUserQueryParams.pageSize"
          layout="sizes, total, prev, pager, next, jumper"
          :total="unassignUserQueryParams.total"
        >
        </el-pagination>
        <div slot="footer" class="dialog-footer">
          <el-button @click="innerVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleAuthRoleToUser"
            >确 定</el-button
          >
        </div>
      </el-dialog>
    </el-dialog>
  </el-main>
</template>

<script>
import moment from "moment";
import {
  getRoleList,
  insertRole,
  toUpdateRole,
  getRole,
  deleteRoles,
  getAssignUserList,
  getUnAssignUserList,
  changeRoleStatus,
  cancelAuthUser,
  AuthRoleToUser,
} from "@/api/role";
import { treeselect } from "@/api/menu";
export default {
  name: "Role",

  data() {
    return {
      /////////////////////////////////////////
      // 被选中准备授权的用户ID
      selectAuthRoleToUserUserIds: [],
      // 被选中取消授权的用户ID
      selectCancelAuthUserIds: [],

      // 为分配角色用户的查询参数
      unassignUserQueryParams: {
        username: undefined,
        phone: undefined,
        pageNum: 1,
        pageSize: 5,
        total: 0,
        roleIds: undefined,
      },
      // 未被分配角色的用户列表
      unassignUserList: [],
      // 分配用户的查询参数
      assignUserQueryParams: {
        username: undefined,
        phone: undefined,
        pageNum: 1,
        pageSize: 5,
        total: 0,
        roleIds: undefined,
      },
      // 已被分配改角色用户列表
      assignUserList: [],
      // 选择分配用户的内嵌对话框
      innerVisible: false,
      // 给角色分配用户对话框隐藏显示
      assignUserDialogTableVisible: false,
      /////////////////////////////////////////

      // 被选择的role
      selectRoleIds: [],
      // 新增更新角色对话框标题
      addUpdateRoleDialogTitle: "",
      // 新增更新对话框 lbael 宽度
      addUpdateFormLabelWidth: "85px",
      // 新增更新角色对话框显示
      addUpdateRoleDialogFormVisible: false,
      // 角色查询参数
      roleQueryParams: {
        name: undefined,
        roleKey: undefined,
        pageNum: 1,
        pageSize: 5,
      },
      total: 0,
      // 角色表格数据
      roleTableData: [],
      // 单个角色数据
      roleData: {
        roleId: undefined,
        name: undefined,
        roleKey: undefined,
        roleSort: undefined,
        status: undefined,
        menuCheckStrictly: undefined,
        remark: undefined,
      },
      // 新增和更新角色表达规则
      addUpdateRoleRules: {
        name: [
          { required: true, message: "角色名称不能为空", trigger: "blur" },
          {
            min: 2,
            max: 7,
            message: "角色名称长度应在[2, 7]",
            trigger: "blur",
          },
        ],
        roleKey: [
          { required: true, message: "权限字符不能为空", trigger: "blur" },
          {
            min: 2,
            max: 15,
            message: "权限字符长度应在[2, 15]",
            trigger: "blur",
          },
        ],
        status: [{ required: true, message: "状态必须选择", trigger: "blur" }],
        roleSort: [
          { required: true, message: "角色顺序必须选择", trigger: "blur" },
        ],
        // menuIds: [
        //   { required: true, message: "必须给角色分配菜单", trigger: "blur" },
        // ],
      },
      // 菜单展开/全选
      menuExpand: false,
      menuNodeAll: false,
      // 菜单列表
      menuOptions: [],
      defaultProps: {
        children: "children",
        label: "label",
      },
    };
  },

  mounted() {
    this.roleList();
  },

  methods: {
    // 批量授权角色给用户
    handleAuthRoleToUser() {
      const userIds = this.selectAuthRoleToUserUserIds;
      const roleId = this.assignUserQueryParams.roleIds;
      console.log(userIds + "----" + roleId);
      this.$confirm("确定授权给用户吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          AuthRoleToUser(roleId, userIds).then((res) => {
            if (res.code == 200) {
              this.innerVisible = false;
              getAssignUserList(this.assignUserQueryParams).then((res) => {
                this.assignUserList = res.data.list;
                this.assignUserQueryParams.total = res.data.total;
              });
              this.$message({
                type: "success",
                message: "授权成功!",
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    // 保存复选框钩上的用户ID--授权角色
    handleAuthRoleSelectChange(selection) {
      this.selectAuthRoleToUserUserIds = selection.map((item) => item.userId);
    },
    // 保存复选框钩上的用户ID--取消授权
    handleCancelAuthSelectionChange(selection) {
      this.selectCancelAuthUserIds = selection.map((item) => item.userId);
    },
    // 处理批量取消授权
    handleCancelAuth(row) {
      const userIds = row.userId || this.selectCancelAuthUserIds;
      const roleId = this.assignUserQueryParams.roleIds;
      if (userIds.length == 0) {
        this.$message({
          message: "请先勾选用户",
          type: "warning",
        });
        return;
      }
      this.$confirm("确定取消授权吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          cancelAuthUser(roleId, userIds).then((res) => {
            if (res.code == 200) {
              this.$message({
                type: "success",
                message: "取消授权成功!",
              });
              getAssignUserList(this.assignUserQueryParams).then((res) => {
                this.assignUserList = res.data.list;
                this.assignUserQueryParams.total = res.data.total;
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消操作",
          });
        });
    },
    // 重置搜索条件
    resetUnAssignUserQueryParams() {
      this.unassignUserQueryParams = {
        username: undefined,
        phone: undefined,
        pageNum: 1,
        pageSize: 7,
      };
      this.unassignUserQueryParams.roleIds = this.assignUserQueryParams.roleIds;
      getUnAssignUserList(this.unassignUserQueryParams).then((res) => {
        this.unassignUserList = res.data.list;
        this.unassignUserQueryParams.total = res.data.total;
      });
    },
    // 条件搜索未分配角色的用户
    getNoAssignedUser() {
      getUnAssignUserList(this.unassignUserQueryParams).then((res) => {
        this.unassignUserList = res.data.list;
        this.unassignUserQueryParams.total = res.data.total;
      });
    },
    handleAddUserToRole() {
      // 清空相关数据
      this.unassignUserList = [];
      this.unassignUserQueryParams = {
        username: undefined,
        phone: undefined,
        pageNum: 1,
        pageSize: 7,
        total: 0,
        roleIds: undefined,
      };
      this.unassignUserQueryParams.roleIds = this.assignUserQueryParams.roleIds;
      // 获取未分配该角色所有用户
      getUnAssignUserList(this.unassignUserQueryParams).then((res) => {
        this.unassignUserList = res.data.list;
        this.unassignUserQueryParams.total = res.data.total;
      });
      this.innerVisible = true;
    },
    // 重置参数
    resetAssignUserQueryParams() {
      let roleId = this.assignUserQueryParams.roleIds;
      this.assignUserQueryParams = {
        username: undefined,
        phone: undefined,
        pageNum: 1,
        pageSize: 7,
      };
      this.assignUserQueryParams.roleIds = roleId;
      getAssignUserList(this.assignUserQueryParams).then((res) => {
        this.assignUserList = res.data.list;
        this.assignUserQueryParams.total = res.data.total;
      });
    },
    // 重置与分配用户相关搜索数据
    resetAssignUserData() {
      this.assignUserQueryParams = {
        username: undefined,
        phone: undefined,
        pageNum: 1,
        pageSize: 7,
        roleIds: undefined,
      };
      this.roleTableData = [];
    },
    // 获取已经分配该角色的用户列表
    toAssignUserList() {
      getAssignUserList(this.assignUserQueryParams).then((res) => {
        this.assignUserList = res.data.list;
        this.assignUserQueryParams.total = res.data.total;
      });
    },
    // 分配用户
    handleAssignUser(row) {
      this.resetAssignUserData;
      const roleId = row.roleId;
      this.assignUserQueryParams.roleIds = roleId;
      getAssignUserList(this.assignUserQueryParams).then((res) => {
        this.assignUserList = res.data.list;
        this.assignUserQueryParams.total = res.data.total;
      });
      this.assignUserDialogTableVisible = true;
    },
    // 修改角色状态
    changeRoleStatus(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$confirm('确认要"' + text + '" "' + row.name + '"角色吗？', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          changeRoleStatus(row.roleId, row.status)
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
    // 保存钩中复选框的roleIds
    handleSelectionChange(selection) {
      this.selectRoleIds = selection.map((item) => item.roleId);
    },
    // 处理删除角色
    handleDeleteRole(row) {
      const roleIds = row.roleId || this.selectRoleIds;
      this.$confirm("确定删除吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteRoles(roleIds).then((res) => {
            if (res.code == 200) {
              this.$message({
                message: "删除成功",
                type: "success",
              });
            }
            this.roleList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    // 父子联动
    handleCheckedTreeConnent(value) {
      this.roleData.menuCheckStrictly = value ? true : false;
    },
    // 全选/全部选
    handleCheckedTreeNodeAll(value) {
      this.$refs.menu.setCheckedNodes(value ? this.menuOptions : []);
    },
    // 展开树
    handleCheckedTreeExpand(value) {
      let treeList = this.menuOptions;
      for (let index = 0; index < treeList.length; index++) {
        this.$refs.menu.store.nodesMap[treeList[index].id].expanded = value;
      }
    },
    // 获取菜单所有勾上的节点
    getCheckedKeyAll(){
      let checkKeys = this.$refs.menu.getCheckedKeys();
      let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys(); 
      checkKeys.unshift.apply(checkKeys,halfCheckedKeys)
      return checkKeys
    },

    // 新增或更新角色
    handleInsertUpdateRole() {
      this.$refs["addUpdateRoleForm"].validate((valid) => {
        if (valid) {
          if (this.roleData.roleId == undefined) {
            let checkKeys = this.getCheckedKeyAll();
            this.roleData.menuIds = checkKeys;
            insertRole(this.roleData).then((res) => {
              if (res.code == 200) {
                this.addUpdateRoleDialogFormVisible = false;
                this.resetRoleData();
                this.roleList();
                this.$message({
                  message: "新增成功",
                  type: "success",
                });
              } else {
                this.addUpdateRoleDialogFormVisible = false;
                this.resetRoleData();
                this.$message.error(res.msg);
              }
            });
          } else {
            let checkKeys = this.getCheckedKeyAll();
            this.roleData.menuIds = checkKeys;
            // console.log(this.roleData);
            // return;
            toUpdateRole(this.roleData).then((res) => {
              if (res.code == 200) {
                this.addUpdateRoleDialogFormVisible = false;
                this.resetRoleData();
                this.roleList();
                this.$message({
                  message: "修改成功",
                  type: "success",
                });
              } else {
                this.addUpdateRoleDialogFormVisible = false;
                this.resetRoleData();
                this.$message.error(res.msg);
              }
            });
          }
        }
      });
    },
    // 重置角色数据
    resetRoleData() {
      this.roleData = {
        roleId: undefined,
        name: undefined,
        roleKey: undefined,
        roleSort: 0,
        status: "0",
        menuIds: [],
        menuCheckStrictly: true,
        createTime: undefined,
        remark: undefined,
      };
    },
    // 打开新增角色对话框
    handleOpenAddRoleDialog() {
      if (this.$refs.addUpdateRoleForm) {
        this.$refs.addUpdateRoleForm.resetFields();
      }
      this.menuExpand = false;
      this.menuNodeAll = false;
      this.resetRoleData();
      this.getMenuTreeselect();
      this.addUpdateRoleDialogTitle = "新增角色";
      this.addUpdateRoleDialogFormVisible = true;
    },
    // 打开修改角色对话框
    async handleOpenUpdateRoleDialog(row) {
      if (this.$refs.addUpdateRoleForm) {
        this.$refs.addUpdateRoleForm.resetFields();
      }
      this.menuExpand = false;
      this.menuNodeAll = false;
      this.resetRoleData();
      // 获取菜单下拉树
      await treeselect().then((res) => {
        this.menuOptions = res.data;
      });
      // 回显角色和角色菜单
      await getRole(row.roleId).then((res) => {
        if (this.$refs.menu != undefined) {
          this.$refs.menu.setCheckedKeys([]);
        }
        this.roleData = res.data;
        this.roleData.menuCheckStrictly = true;
        let m = res.data.menuIds;
        this.$nextTick(() => {
          m.forEach((value) => {
            this.$refs.menu.setChecked(value, true, false);
          });
        });
        this.addUpdateRoleDialogTitle = "修改角色";
        this.addUpdateRoleDialogFormVisible = true;
      });
    },
    // 获取菜单树结构
    getMenuTreeselect() {
      treeselect().then((res) => {
        this.menuOptions = res.data;
      });
    },
    // 重置搜索用户表单参数
    resetRoleSearchForm() {
      this.roleQueryParams = {
        name: undefined,
        roleKey: undefined,
        pageNum: 1,
        pageSize: 4,
      };
      this.roleList();
    },
    // 获取角色列表
    roleList() {
      getRoleList(this.roleQueryParams).then((res) => {
        if (res.code == 200) {
          this.roleTableData = res.data.list;
          this.total = res.data.total;
        }
      });
    },
    // 时间格式化
    dateFormat: function (row, column) {
      var date = row[column.property];
      if (date === undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    // 给角色分配用户的分页器方法
    handleSizeChangeOfAssignUser(pageSize) {
      this.assignUserQueryParams.pageSize = pageSize;
      getAssignUserList(this.assignUserQueryParams).then((res) => {
        this.assignUserList = res.data.list;
        this.assignUserQueryParams.total = res.data.total;
      });
    },
    handleSizeChangeOfUnAssignUser(pageSize) {
      this.unassignUserQueryParams.pageSize = pageSize;
      getUnAssignUserList(this.unassignUserQueryParams).then((res) => {
        this.unassignUserList = res.data.list;
        this.unassignUserQueryParams.total = res.data.total;
      });
    },
    handleCurrentChangeOfAssignUser(pageNum) {
      this.assignUserQueryParams.pageNum = pageNum;
      getAssignUserList(this.assignUserQueryParams).then((res) => {
        this.assignUserList = res.data.list;
        this.assignUserQueryParams.total = res.data.total;
      });
    },
    handleCurrentChangeOfUnAssignUser(pageNum) {
      this.unassignUserQueryParams.pageNum = pageNum;
      getUnAssignUserList(this.unassignUserQueryParams).then((res) => {
        this.unassignUserList = res.data.list;
        this.unassignUserQueryParams.total = res.data.total;
      });
    },
    // pageSize改变触发
    handleSizeChange(pageSize) {
      this.roleQueryParams.pageSize = pageSize;
      this.roleList();
    },
    // 当前页码改变触发
    handleCurrentChange(pageNum) {
      this.roleQueryParams.pageNum = pageNum;
      this.roleList();
    },
  },
};
</script>

<style lang="scss" scoped>
</style>