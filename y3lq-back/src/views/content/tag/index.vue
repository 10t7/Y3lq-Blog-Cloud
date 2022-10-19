<template>
  <el-main>
    <!--搜索新增按钮  -->
    <el-form
      :inline="true"
      :model="tagQueryParames"
      class="demo-form-inline"
      size="mini"
    >
      <el-form-item label="标签名称">
        <el-input
          v-model="tagQueryParames.name"
          placeholder="请输入标签名称"
        ></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select
          v-model="tagQueryParames.status"
          placeholder="请选择标签状态"
        >
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="searchTagList" v-if="hasPermission('content:tag:query')"
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
          @click="handleAddTag"
          
          v-if="hasPermission('content:tag:add')"
          >新 增</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button type="info" icon="el-icon-sort" @click="handleExpandAll"
          >展 开/折 叠</el-button
        >
      </el-form-item>
    </el-form>

    <!-- 文章表格 -->
    <el-table
      v-if="refreshTable"
      :data="tagTableData"
      style="width: 100%; margin-bottom: 20px"
      row-key="tagId"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      :default-expand-all="isExpandAll"
      lazy
      :cell-style="tableCellStyle"
    >
      <el-table-column
        prop="name"
        label="标签名称"
        width="200"
        align="center"
        fixed
      >
        <template slot-scope="{ row }">
          <i v-if="row.parentId == 0">{{ row.name }}</i>
          <el-tag type="success" v-else>{{ row.name }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" label="排序" width="200" align="center">
      </el-table-column>
      <el-table-column prop="status" label="状态" width="200" align="center">
        <!-- <template slot-scope="{ row }">
          <el-tag v-if="row.status == '0'">正常</el-tag>
          <el-tag type="danger" v-if="row.status == '1'">停用</el-tag>
        </template> -->
        <template slot-scope="{ row }">
          <el-switch
            v-model="row.status"
            active-value="0"
            inactive-value="1"
            @change="changeTagStatus(row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="200"
        align="center"
        :formatter="dateFormat"
      >
      </el-table-column>
      <el-table-column
        prop="createUsername"
        label="创建者"
        width="180"
        align="center"
      >
      </el-table-column>

      <el-table-column
        prop="operation"
        label="操作"
        width="200"
        align="center"
        fixed="right"
      >
        <template slot-scope="{ row }">
          <el-button
            type="warning"
            icon="el-icon-edit"
            circle
            size="mini"
            @click="handleUpdateTag(row)"
            v-if="hasPermission('content:tag:edit')"
          ></el-button>
          <el-button
            v-if="row.parentId === 0 && hasPermission('content:tag:add')"
            type="success"
            icon="el-icon-plus"
            circle
            size="mini"
            @click="handleAddTag(row)"
          ></el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            circle
            size="mini"
            @click="handleDeleteTag(row)"
            v-if="hasPermission('content:tag:delete')"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增或者修改标签对话框 -->
    <el-dialog
      :title="addUpdateTagDialogTitle"
      :visible.sync="addUpdateTagDialogFormVisible"
      width="30%"
    >
      <el-form
        :model="tagData"
        :rules="addUpdateTagRules"
        ref="addUpdateTagForm"
      >
        <!-- 标签名 -->
        <el-form-item
          :label="type == '0' ? '类别名' : '标签名'"
          :label-width="addUpdateTagformLabelWidth"
          prop="name"
        >
          <el-input
            style="width: 265px"
            v-model="tagData.name"
            autocomplete="off"
            maxlength="18"
          ></el-input>
        </el-form-item>
        <!-- 类型 -->
        <el-form-item
          label="类型"
          :label-width="addUpdateTagformLabelWidth"
          prop="type"
          v-if="!disableTagCategory"
        >
          <el-radio v-model="type" label="0" @change="handleTypeChange"
            >标签类别</el-radio
          >
          <el-radio v-model="type" label="1" @change="handleTypeChange"
            >标签
          </el-radio>
        </el-form-item>
        <!-- 所属分类 -->
        <el-form-item
          label="所属类别"
          :label-width="addUpdateTagformLabelWidth"
          prop="parentId"
          v-if="type == 1"
        >
          <el-select v-model="tagData.parentId" placeholder="请选择分类">
            <el-option
              v-for="item in tagCategoryOptions"
              :key="item.tagId"
              :label="item.name"
              :value="item.tagId"
              :disabled="item.status == '1'"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <!-- 排序 -->
        <el-form-item
          label="排序"
          :label-width="addUpdateTagformLabelWidth"
          prop="orderNum"
        >
          <el-input-number
            v-model="tagData.orderNum"
            controls-position="right"
            :min="1"
          ></el-input-number>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="addUpdateTagDialogFormVisible = false"
          >取 消</el-button
        >
        <el-button type="primary" @click="addUpdateTag">提 交</el-button>
      </div>
    </el-dialog>
  </el-main>
</template>

<script>
import { handleTree } from "@/utils/y3lq";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {
  tagList,
  addTag,
  tagCategoryList,
  tagInfo,
  updateTag,
  deleteTag,
  changeTagStatus,
} from "@/api/tag";
import moment from "moment";
export default {
  name: "Tag",
  components: {
    Treeselect,
  },
  data() {
    return {
      statusOptions: [
        {
          value: "0",
          label: "正常",
        },
        {
          value: "1",
          label: "停用",
        },
      ],
      disableTagCategory: false,
      // 标签类标数组
      tagCategoryOptions: [],
      type: "0",
      addUpdateTagformLabelWidth: "90px",
      addUpdateTagRules: {
        name: [
          { required: true, message: "类别名/标签名不能为空", trigger: "blur" },
          {
            min: 2,
            max: 18,
            message: "类别名/标签名长度应在[1, 10]",
            trigger: "blur",
          },
        ],
        orderNum: [
          {
            required: true,
            message: "必须为类别/标签设置顺序",
            trigger: "blur",
          },
        ],
        parentId: [
          {
            required: true,
            message: "必须为标签设置所属分类",
            trigger: "blur",
          },
        ],
      },
      // 新增更新标签数据
      tagData: {},
      // 新增更新对话框标题
      addUpdateTagDialogTitle: undefined,
      addUpdateTagDialogFormVisible: false,
      // 标签查询参数
      tagQueryParames: {
        name: undefined,
        status: undefined,
      },
      // 表格数据
      tagTableData: [],
      // 重新渲染表格
      refreshTable: true,
      // 是否展开全部
      isExpandAll: false,
    };
  },

  mounted() {
    this.getTagList();
  },

  methods: {
    // 修改标签状态
    changeTagStatus(row) {
      // console.log(row.tagId,row.status)
      let text = row.status === "0" ? "启用" : "停用";
      this.$confirm('确认要"' + text + '" "' + row.name + '"吗？', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          changeTagStatus(row.tagId, row.status)
            .then((res) => {
              this.$message({
                type: "success",
                message: text + "成功",
              });
              this.getTagList();
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
    handleTypeChange(type) {
      this.tagData.parentId = undefined;
      if (type == "0") {
        this.addUpdateTagDialogTitle = "新增类别";
      } else {
        this.type == "1";
        this.addUpdateTagDialogTitle = "新增标签";
      }
    },

    // 获取所有标签类别
    getTagCategoryOptions() {
      tagCategoryList().then((res) => {
        if (res.code == 200) {
          this.tagCategoryOptions = res.data;
        }
      });
    },

    resetTagDate() {
      this.tagData = {
        name: undefined,
        parentId: undefined,
        orderNum: undefined,
      };
    },
    // 新增更新标签
    addUpdateTag() {
      this.$refs["addUpdateTagForm"].validate((valid) => {
        if (valid) {
          if (this.tagData.tagId == undefined) {
            addTag(this.tagData).then((res) => {
              if (res.code == 200) {
                this.addUpdateTagDialogFormVisible = false;
                this.getTagList();
                this.$message({
                  message: "新增成功",
                  type: "success",
                });
              } else {
                this.$message.error(res.msg);
              }
            });
          } else {
            updateTag(this.tagData).then((res) => {
              if (res.code == 200) {
                this.addUpdateTagDialogFormVisible = false;
                this.getTagList();
                this.$message({
                  message: "修改成功",
                  type: "success",
                });
              } else {
                this.$message.error(res.msg);
              }
            });
          }
        }
      });
    },
    // 处理删除标签
    handleDeleteTag(row) {
      const title = row.name;
      this.$confirm("确定删除" + title + "?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteTag(row.tagId).then((res) => {
            if (res.code == 200) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.getTagList();
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
    // 处理更新标签
    handleUpdateTag(row) {
      if (this.$refs.addUpdateTagForm) {
        this.$refs.addUpdateTagForm.resetFields();
      }
      this.resetTagDate();
      this.addUpdateTagDialogFormVisible = true;
      this.type = "0";
      tagInfo(row.tagId).then((res) => {
        this.tagData = res.data;
        this.disableTagCategory = true;
        if (res.data.parentId === 0) {
          this.addUpdateTagDialogTitle = "修改类别";
        } else {
          this.type = "1";
          this.getTagCategoryOptions();
          this.addUpdateTagDialogTitle = "修改标签";
        }
      });
    },
    // 处理新增标签
    handleAddTag(row) {
      if (this.$refs.addUpdateTagForm) {
        this.$refs.addUpdateTagForm.resetFields();
      }
      this.resetTagDate();
      this.getTagCategoryOptions();
      this.addUpdateTagDialogFormVisible = true;
      this.disableTagCategory = false;
      if (row.tagId != undefined) {
        this.addUpdateTagDialogTitle = "新增标签";
        this.type = "1";
        this.tagData.parentId = row.tagId;
        this.disableTagCategory = true;
      } else {
        this.addUpdateTagDialogTitle = "新增类别";
        this.type = "0";
      }
    },
    // 条件查询标签列表
    searchTagList() {
      this.tagTableData = [];
      this.getTagList();
    },
    // 重置查询参数
    handleResetSearchParameters() {
      this.tagQueryParames = {
        name: undefined,
        status: undefined,
      };
      this.getTagList();
    },
    // 改变单元格颜色
    tableCellStyle({ row, column, rowIndex, columnIndex }) {
      if (row.parentId != 0) {
        return "background-color: #f0f9eb";
      }
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
    // 处理展开
    handleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    // 获取标签列表
    getTagList() {
      tagList(this.tagQueryParames).then((res) => {
        this.tagTableData = handleTree(res.data, "tagId");
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