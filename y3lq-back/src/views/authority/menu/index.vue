<template>
  <el-main>
    <!-- 搜索项以及按钮 -->
    <el-form
      :inline="true"
      :model="menuQueryParams"
      class="demo-form-inline"
      size="mini"
    >
      <el-form-item label="菜单名称">
        <el-input
          v-model="menuQueryParams.name"
          placeholder="请输入菜单名称"
        ></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-input
          v-model="menuQueryParams.status"
          placeholder="请选择菜单状态"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="handleMenuSearch"
          >查 询</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button
          icon="el-icon-refresh-right"
          @click="handleResetSearchParameters"
          >重 置</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button
          type="success"
          icon="el-icon-plus"
          @click="handleAddMenu"
          v-if="hasPermission('authority:menu:add')"
          >新 增</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button type="info" icon="el-icon-sort" @click="handleExpandAll"
          >展 开/折 叠</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 菜单表格 -->
    <el-table
      v-if="refreshTable"
      :data="menuTableData"
      style="width: 100%; margin-bottom: 20px"
      row-key="menuId"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      :default-expand-all="isExpandAll"
      :header-cell-style="{ background: '#f4f6f9', color: '#606266' }"
      lazy
      :cell-style="tableCellStyle"
    >
      
      <!-- 菜单名称 -->
      <el-table-column
        prop="name"
        label="菜单名称"
        width="140"
        align="center"
        fixed
      >
      </el-table-column>
      <!-- 菜单类型 -->
      <el-table-column
        prop="menuType"
        label="菜单类型"
        width="90"
        align="center"
      >
        <template slot-scope="{ row }">
          <el-tag v-if="row.menuType == 'M'">目录</el-tag>
          <el-tag type="success" v-if="row.menuType == 'C'">菜单</el-tag>
          <el-tag type="info" v-if="row.menuType == 'F'">按钮</el-tag>
        </template>
      </el-table-column>
      <!-- 图标 -->
      <el-table-column prop="icon" label="图标" width="90" align="center">
        <template slot-scope="{ row }">
          <i :class="row.icon"></i>
        </template>
      </el-table-column>

      <!-- 排序 -->
      <el-table-column prop="orderNum" label="排序" width="90" align="center">
      </el-table-column>
      <!-- 权限标识 -->
      <el-table-column prop="perms" label="权限标识" width="230" align="center">
      </el-table-column>
      <!-- 组件路径 -->
      <el-table-column
        prop="component"
        label="组件路径"
        width="230"
        align="center"
      >
      </el-table-column>
      <!-- 状态 -->
      <el-table-column prop="status" label="状态" align="center" width="100">
        <template slot-scope="{ row }">
          <el-tag>{{ row.status == "0" ? "正常" : "停用" }}</el-tag>
        </template>
      </el-table-column>
      <!-- 创建时间 -->
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="195"
        :formatter="dateFormat"
        align="center"
      >
      </el-table-column>
      <!-- 操作 -->
      <el-table-column
        prop="operation"
        label="操作"
        align="center"
        width="180"
        fixed="right"
      >
        <template slot-scope="{ row }">
          <el-button
            type="warning"
            icon="el-icon-edit"
            circle
            size="mini"
            @click="handleUpdateMenu(row)"
            v-if="hasPermission('authority:menu:edit')"
          ></el-button>
          <el-button
            type="success"
            icon="el-icon-plus"
            circle
            size="mini"
            @click="handleAddMenu(row)"
            v-if="hasPermission('authority:menu:add')"
          ></el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            circle
            size="mini"
            @click="handleDeleteMenu(row)"
            v-if="hasPermission('authority:menu:delete')"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加修改菜单对话框 -->
    <el-dialog
      :title="addUpdateDialogTitle"
      :visible.sync="addUpdateMenuDialogFormVisible"
      width="700px"
      append-to-body
    >
      <el-form
        :model="addUpdateMenuData"
        :rules="addUpdateDialogRules"
        ref="addUpdateMenuForm"
      >
        <!-- 上级菜单 -->
        <el-row>
          <el-col :span="22">
            <el-form-item
              label="上级菜单"
              :label-width="addUpdateMenuformLabelWidth"
            >
              <treeselect
                v-model="addUpdateMenuData.parentId"
                :options="menuOptions"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="请选择上级菜单"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 菜单类型 -->
        <el-row>
          <el-col :span="22">
            <el-form-item
              label="菜单类型"
              :label-width="addUpdateMenuformLabelWidth"
              prop="menuType"
            >
              <el-radio-group v-model="addUpdateMenuData.menuType">
                <el-radio label="M">目录</el-radio>
                <el-radio label="C">菜单</el-radio>
                <el-radio label="F">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 菜单图标 -->
        <!-- <el-row>
          <el-col :span="22" v-if="addUpdateMenuData.menuType != 'F'">
            <el-form-item
              label="菜单图标"
              :label-width="addUpdateMenuformLabelWidth"
              prop="icon"
            >
              <el-popover
                placement="bottom-start"
                width="460"
                trigger="click"
                @show="$refs['iconSelect'].reset()"
              >
                <IconSelect ref="iconSelect" @selected="iconSelected" />
                <el-input
                  slot="reference"
                  v-model="addUpdateMenuData.icon"
                  readonly
                  placeholder="请选择图标"
                >
                  <svg-icon
                    v-if="addUpdateMenuData.icon"
                    slot="prefix"
                    :icon-class="addUpdateMenuData.icon"
                    class="el-input__icon"
                    style="height: 32px; width: 16px"
                  />
                  <i
                    v-else
                    slot="prefix"
                    class="el-icon-search el-input__icon"
                  />
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
        </el-row> -->
        <!-- 菜单名称/显示顺序 -->
        <el-row>
          <el-col :span="11">
            <el-form-item
              label="菜单名称"
              :label-width="addUpdateMenuformLabelWidth"
              prop="name"
            >
              <el-input
                v-model="addUpdateMenuData.name"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item
              label="显示顺序"
              :label-width="addUpdateMenuformLabelWidth"
              prop="orderNum"
            >
              <el-input-number
                v-model="addUpdateMenuData.orderNum"
                controls-position="right"
                :min="0"
              ></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 是否外链/路由地址 -->
        <el-row>
          <el-col :span="11" v-if="addUpdateMenuData.menuType != 'F'">
            <el-form-item
              label="是否外链"
              :label-width="addUpdateMenuformLabelWidth"
            >
              <el-radio-group v-model="addUpdateMenuData.isFrame">
                <el-radio label="0">是</el-radio>
                <el-radio label="1">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="11" v-if="addUpdateMenuData.menuType != 'F'">
            <el-form-item
              label="路由地址"
              :label-width="addUpdateMenuformLabelWidth"
              prop="path"
            >
              <el-input
                v-model="addUpdateMenuData.path"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 组件路径/权限字符 -->
        <el-row>
          <el-col :span="11" v-if="addUpdateMenuData.menuType == 'C'">
            <el-form-item
              label="组件路径"
              :label-width="addUpdateMenuformLabelWidth"
              prop="component"
            >
              <el-input
                v-model="addUpdateMenuData.component"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="11" v-if="addUpdateMenuData.menuType != 'M'">
            <el-form-item
              label="权限字符"
              :label-width="addUpdateMenuformLabelWidth"
            >
              <el-input
                v-model="addUpdateMenuData.perms"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 路由参数/是否缓存 -->
        <el-row>
          <el-col :span="11" v-if="addUpdateMenuData.menuType == 'C'">
            <el-form-item
              label="路由参数"
              :label-width="addUpdateMenuformLabelWidth"
            >
              <el-input
                v-model="addUpdateMenuData.query"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="11" v-if="addUpdateMenuData.menuType == 'C'">
            <el-form-item
              label="是否缓存"
              :label-width="addUpdateMenuformLabelWidth"
            >
              <el-radio-group v-model="addUpdateMenuData.isCache">
                <el-radio label="0">缓存</el-radio>
                <el-radio label="1">不缓存</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 显示状态/菜单状态 -->
        <el-row>
          <el-col :span="11" v-if="addUpdateMenuData.menuType != 'F'">
            <el-form-item
              label="显示状态"
              :label-width="addUpdateMenuformLabelWidth"
            >
              <el-radio-group v-model="addUpdateMenuData.visible">
                <el-radio label="0">显示</el-radio>
                <el-radio label="1">隐藏</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="11" v-if="addUpdateMenuData.menuType != 'F'">
            <el-form-item
              label="菜单状态"
              :label-width="addUpdateMenuformLabelWidth"
            >
              <el-radio-group v-model="addUpdateMenuData.status">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item
              label="备注"
              :label-width="addUpdateMenuformLabelWidth"
              prop="remark"
            >
              <el-input
                type="textarea"
                v-model="addUpdateMenuData.remark"
                maxlength="50"
                show-word-limit
              >
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addUpdateMenuDialogFormVisible = false"
          >取 消</el-button
        >
        <el-button type="primary" @click="submitAddUpdateForm">提 交</el-button>
      </div>
    </el-dialog>
  </el-main>
</template>

<script>
import moment from "moment";
import {
  menuList,
  addMenu,
  updateMenu,
  getMenuInfo,
  deleteMenu,
} from "@/api/menu";
import { handleTree } from "@/utils/y3lq";
import IconSelect from "@/components/IconSelect";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "Menu",
  components: { IconSelect, Treeselect },
  data() {
    return {
      // 新增更新菜单对话框表单校验
      addUpdateDialogRules: {
        name: [
          { required: true, message: "菜单名称不能为空", trigger: "blur" },
        ],
        orderNum: [
          { required: true, message: "菜单顺序不能为空", trigger: "blur" },
        ],
        path: [
          { required: true, message: "路由地址不能为空", trigger: "blur" },
        ],
      },
      // 标签宽度
      addUpdateMenuformLabelWidth: "110px",
      // 新增更新菜单对话框标题
      addUpdateDialogTitle: undefined,
      addUpdateMenuData: {},
      // 添加修改菜单对话框显示隐藏
      addUpdateMenuDialogFormVisible: false,
      // 重新渲染表格
      refreshTable: true,
      // 是否展开全部
      isExpandAll: false,
      menuQueryParams: {
        name: undefined,
        status: undefined,
      },
      // 菜单树表格
      menuTableData: [],
      // 菜单树选项
      menuOptions: [],
    };
  },

  mounted() {
    this.getMenuList();
  },

  methods: {
    // 处理重置搜索参数
    handleResetSearchParameters() {
      this.menuQueryParams = {
        name: undefined,
        status: undefined,
      };
      menuList(this.menuQueryParams).then((res) => {
        this.menuTableData = handleTree(res.data, "menuId");
      });
    },
    // 处理条件搜索菜单
    handleMenuSearch() {
      const { name, status } = this.menuQueryParams;
      menuList(name, status).then((res) => {
        this.menuTableData = handleTree(res.data, "menuId");
      });
    },
    // 处理删除菜单
    handleDeleteMenu(row) {
      const data = {};
      this.$confirm("此操作将永久删除" + row.name + "菜单, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteMenu(row.menuId).then((res) => {
            if (res.code == 200) {
              this.$message({
                message: "删除菜单成功",
                type: "success",
              });
              this.getMenuList(data);
            } else {
              this.$message.error(res.msg);
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
    // 处理更新菜单
    async handleUpdateMenu(row) {
      if (this.$refs.addUpdateMenuForm) {
        this.$refs.addUpdateMenuForm.resetFields();
      }
      this.resetAddUpdateMenuFormData();
      const menuId = row.menuId;
      const data = {};
      await menuList(data).then((res) => {
        this.menuOptions = [];
        const menu = { menuId: 0, name: "主类目", children: [] };
        menu.children = handleTree(res.data, "menuId");
        this.menuOptions.push(menu);
        this.addUpdateDialogTitle = "修改菜单";
        this.addUpdateMenuDialogFormVisible = true;
      });
      await getMenuInfo(menuId).then((res) => {
        this.addUpdateMenuData = res.data;
      });
    },
    // 提交新增或者修改菜单
    submitAddUpdateForm() {
      this.$refs["addUpdateMenuForm"].validate((valid) => {
        if (valid) {
          const data = {};
          if (this.addUpdateMenuData.menuId == undefined) {
            addMenu(this.addUpdateMenuData).then((res) => {
              if (res.code == 200) {
                this.addUpdateMenuDialogFormVisible = false;
                this.$message({
                  message: "新增菜单成功",
                  type: "success",
                });
              } else {
                this.$message.error(res.msg);
                this.addUpdateMenuDialogFormVisible = false;
              }
              this.getMenuList(data);
            });
          } else {
            updateMenu(this.addUpdateMenuData).then((res) => {
              if (res.code == 200) {
                this.addUpdateMenuDialogFormVisible = false;
                this.$message({
                  message: "更新菜单成功",
                  type: "success",
                });
              } else {
                this.$message.error(res.msg);
                this.addUpdateMenuDialogFormVisible = false;
              }
              this.getMenuList(data);
            });
          }
        }
      });
      console.log(this.addUpdateMenuData);
    },
    // 转换菜单数据结构
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.menuId,
        label: node.name,
        children: node.children,
      };
    },
    // 选择图标
    iconSelected(name) {
      this.addUpdateMenuData.icon = name;
    },
    // 改变单元格背景颜色
    tableCellStyle({ row, column, rowIndex, columnIndex }) {
      if (row.menuType == "M") {
        return "background-color: #fdfdfd";
      }
      if (row.menuType == "C") {
        // #f0f9eb
        return "background-color: #f0f9eb";
      }
      if (row.menuType == "F") {
        return "background-color: #f1f5fa";
      }
    },
    // 重置表单
    resetAddUpdateMenuFormData() {
      this.addUpdateMenuData = {
        menuId: undefined,
        parentId: 0,
        name: undefined,
        icon: undefined,
        menuType: "M",
        orderNum: undefined,
        isFrame: "1",
        isCache: "0",
        visible: "0",
        status: "0",
        remark: undefined,
      };
    },
    // 处理新增菜单
    handleAddMenu(row) {
      if (this.$refs.addUpdateMenuForm) {
        this.$refs.addUpdateMenuForm.resetFields();
      }
      this.resetAddUpdateMenuFormData();
      const data = {};
      menuList(data).then((res) => {
        this.menuOptions = [];
        const menu = { menuId: 0, name: "主类目", children: [] };
        menu.children = handleTree(res.data, "menuId");
        this.menuOptions.push(menu);
        this.addUpdateDialogTitle = "新增菜单";
        this.addUpdateMenuDialogFormVisible = true;
        if (row.menuId != undefined) {
          this.addUpdateMenuData.parentId = row.menuId;
        } else {
          this.addUpdateMenuData.parentId = 0;
        }
      });
    },
    // 处理展开
    handleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    // 获取菜单树列表
    getMenuList() {
      menuList(this.menuQueryParams).then((res) => {
        this.menuTableData = handleTree(res.data, "menuId");
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
  },
};
</script>

<style lang="scss" scoped>
</style>