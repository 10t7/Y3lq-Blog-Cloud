<template>
  <el-main>
    <!--搜索新增按钮 -->
    <el-form
      :inline="true"
      :model="articleQueryParames"
      class="demo-form-inline"
      size="mini"
    >
      <el-form-item label="标题">
        <el-input
          v-model="articleQueryParames.title"
          placeholder="请输入文章标题"
        ></el-input>
      </el-form-item>

      <!-- <el-form-item label="标签">
        <el-popover placement="bottom" width="200" trigger="click">
          <el-tree
            :data="tagTableData"
            node-key="tagId"
            @node-click="handleSearchNodeClick"
            :props="defaultProps"
          >
          </el-tree>
          <el-button plain slot="reference" @click="handleSelectTag">{{
            chooseTagName
          }}</el-button>
        </el-popover>
      </el-form-item> -->

      <el-form-item label="作者">
        <el-input
          v-model="articleQueryParames.authorUsername"
          placeholder="请输入文章作者用户名"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="handleSearchArticle"
          v-if="hasPermission('content:article:query')"
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
          @click="handleAddArticle"
          v-if="hasPermission('content:article:add')"
          >新 增 文 章</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button
          type="danger"
          icon="el-icon-delete"
          @click="handleDeleteArticle"
          v-if="hasPermission('content:article:delete')"
          >删 除</el-button
        >
      </el-form-item>
    </el-form>

    <!-- 文章表格 -->
    <el-table
      :data="articleData"
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
      <el-table-column
        prop="title"
        label="标题"
        width="200"
        align="center"
        fixed
      >
      </el-table-column>
      <el-table-column
        prop="authorUsername"
        label="作者"
        width="150"
        align="center"
      >
      </el-table-column>
      <el-table-column prop="tag" label="标签" width="150" align="center">
        <template slot-scope="{ row }">
          <el-tag
            type="success"
            v-for="(item, index) in row.tagEntity"
            :key="index"
            style="margin-left: 3px"
          >
            {{ item.name }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="likeCount"
        label="点赞数"
        width="100"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="commentCount"
        label="评论数"
        width="100"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="orderNum"
        label="排序(首页推荐)"
        width="120"
        align="center"
      >
      </el-table-column>
      <el-table-column prop="thumbnail" label="封面" width="150" align="center">
        <template slot-scope="{ row }">
          <img :src="row.thumbnail" alt="" style="width: 124px; height: 70px" />
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template slot-scope="{ row }">
          <el-tag v-if="row.status === '0'" type="success">发布</el-tag>
          <el-tag v-if="row.status === '1'" type="warning">未发布</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="180"
        align="center"
        :formatter="dateFormat"
      >
      </el-table-column>
      <el-table-column
        prop="operation"
        label="操作"
        width="300"
        align="center"
        fixed="right"
      >
        <template slot-scope="{ row }">
          <el-button
            type="warning"
            icon="el-icon-edit"
            plain
            size="mini"
            @click="handleUpdateArticle(row)"
            v-if="hasPermission('content:article:edit')"
            >编辑</el-button
          >
          <el-button
            type="info"
            icon="el-icon-caret-bottom"
            plain
            size="mini"
            v-if="row.status === '0'"
            @click="handleChangeArticleStatus(row)"
            >下架</el-button
          >
          <el-button
            v-if="row.status === '1'"
            type="info"
            icon="el-icon-caret-top"
            plain
            size="mini"
            @click="handleChangeArticleStatus(row)"
            >发布</el-button
          >
          <el-button
            type="danger"
            icon="el-icon-delete"
            plain
            size="mini"
            v-if="hasPermission('content:article:delete')"
            @click="handleDeleteArticle(row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      style="margin-top: 15px; textalign: center"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="articleQueryParames.pageNum"
      :page-sizes="[5, 10, 20]"
      :page-size="articleQueryParames.pageSize"
      layout="sizes, total, prev, pager, next, jumper"
      :total="articleListTotal"
    >
    </el-pagination>

    <!-- 新增修改文章对话框 -->
    <el-dialog
      :title="addUpdateArticleDialogTitle"
      :visible.sync="addUpdateArticleDialogFormVisible"
      fullscreen
      style="background: #dcdfe6"
      custom-class="customClass"
    >
      <el-form
        ref="addUpdateArticleForm"
        :rules="addUpdateArticleRules"
        :model="addUpdateArticleForm"
      >
        <!-- 富文本编辑器 -->
        <el-row>
          <el-col :span="19">
            <el-form-item
              label="标 题"
              :label-width="AddUpdateArticleFormLabelWidth"
              prop="title"
            >
              <el-input
                v-model="addUpdateArticleForm.title"
                autocomplete="off"
              ></el-input> </el-form-item
          ></el-col>
          <el-col :span="19">
            <el-form-item
              label="内 容"
              prop="content"
              :label-width="AddUpdateArticleFormLabelWidth"
            >
              <MyEditor @getEditor="getEditor" ref="myEditor" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <!-- 标签 -->
            <el-col :span="4"
              ><el-form-item prop="tag" label="标 签" label-width="270px">
                <el-popover placement="top-end" width="275" trigger="click">
                  <el-form :inline="true" class="demo-form-inline">
                    <el-form-item>
                      <el-input
                        placeholder="输入关键字进行过滤"
                        v-model="filterText"
                        size="mini"
                      >
                      </el-input>
                    </el-form-item>

                    <el-form-item>
                      <el-button
                        size="mini"
                        icon="el-icon-sort"
                        circle
                        @click="handleExpandAll"
                      ></el-button>
                    </el-form-item>
                    <el-form-item
                      ><el-popover
                        placement="right"
                        width="230"
                        trigger="click"
                      >
                        <el-form
                          :model="tagData"
                          ref="addTagForm"
                          :rules="addTagRules"
                        >
                          <!-- 标签名 -->
                          <el-form-item prop="name">
                            <el-input
                              size="small"
                              :placeholder="
                                type == '0' ? '请输入类别名' : '请输入标签名'
                              "
                              v-model="tagData.name"
                              autocomplete="off"
                              maxlength="10"
                            ></el-input>
                          </el-form-item>
                          <!-- 类型 -->
                          <el-form-item prop="type" v-if="!disableTagCategory">
                            <el-radio
                              size="small"
                              v-model="type"
                              label="0"
                              @change="handleTypeChange"
                              >标签类别</el-radio
                            >
                            <el-radio
                              size="small"
                              v-model="type"
                              label="1"
                              @change="handleTypeChange"
                              >标签
                            </el-radio>
                          </el-form-item>
                          <!-- 所属分类 -->
                          <el-form-item prop="parentId" v-if="type == 1">
                            <el-select
                              size="small"
                              v-model="tagData.parentId"
                              placeholder="请选择标签类别"
                            >
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
                          <el-form-item prop="orderNum">
                            <el-input-number
                              size="small"
                              placeholder="请选择排序"
                              v-model="tagData.orderNum"
                              controls-position="right"
                              :min="1"
                            ></el-input-number>
                            <i style="margin-left: 12px">
                              <el-button size="small" @click="insertTag"
                                >添 加</el-button
                              >
                            </i>
                          </el-form-item>
                        </el-form>
                        <el-button
                          slot="reference"
                          size="mini"
                          @click="handleTagCategoryList"
                          circle
                          icon="el-icon-plus"
                        ></el-button> </el-popover
                    ></el-form-item>
                  </el-form>
                  <el-tree
                    v-if="refreshTable"
                    :data="tagTableData"
                    node-key="tagId"
                    :filter-node-method="filterNode"
                    ref="tree"
                    :default-expand-all="isExpandAll"
                    @node-click="handleNodeClick"
                  >
                    <span class="custom-tree-node" slot-scope="{ node, data }">
                      <span>{{ data.name }}</span>
                      <span>
                        <el-tag
                          type="success"
                          v-if="showButton(data.parentId)"
                          size="mini"
                          effect="plain"
                          >添 加</el-tag
                        >
                      </span>
                    </span>
                  </el-tree>
                  <el-button
                    slot="reference"
                    plain
                    icon="el-icon-plus"
                    @click="handleTagList"
                    size="mini"
                    >标签</el-button
                  >
                </el-popover>
              </el-form-item>
            </el-col>
            <el-col :span="20"
              ><el-form-item label-width="125px">
                <el-tag
                  v-for="tag in tags"
                  :key="tag.tagId"
                  :type="tag.type"
                  closable
                  @close="remove(tag.tagId)"
                >
                  {{ tag.name }}
                </el-tag>
              </el-form-item></el-col
            >
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item
              label="排序值"
              :label-width="AddUpdateArticleFormLabelWidth"
            >
              <el-input-number
                v-model="addUpdateArticleForm.orderNum"
                controls-position="right"
                :min="0"
                :max="99999"
              ></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="13">
            <!-- 摘要 -->
            <el-form-item
              label="摘 要"
              :label-width="AddUpdateArticleFormLabelWidth"
              prop="summary"
            >
              <el-input
                type="textarea"
                :rows="7"
                v-model="addUpdateArticleForm.summary"
                autocomplete="off"
              ></el-input> </el-form-item
          ></el-col>
          <el-col :span="6"
            ><el-form-item label="封 面" label-width="60px">
              <el-upload
                class="avatar-uploader"
                action="https://y3lq-blog.oss-cn-shenzhen.aliyuncs.com"
                :data="uploadImgaeData"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
              >
                <img
                  v-if="addUpdateArticleForm.thumbnail"
                  :src="addUpdateArticleForm.thumbnail"
                  class="avatar"
                />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload> </el-form-item
          ></el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleSubmitArticle" round
          >发布文章</el-button
        >
      </div>
    </el-dialog>
  </el-main>
</template>

<script>
import { handleTree } from "@/utils/y3lq";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {
  getArticleList,
  addArticle,
  updateArticle,
  deleteArticle,
  changeArticleStatus,
} from "@/api/article";
import MyEditor from "@/components/MyEditor";
import { getSignature } from "@/api/user";
import { tagNameList } from "@/api/tag";
import { v4 } from "uuid";
import { tagCategoryList, addTag } from "@/api/tag";
import moment from "moment";

export default {
  name: "Article",
  components: { MyEditor },
  data() {
    return {
      addUpdateArticleRules: {
        content: [{ required: true, message: "内容不能为空", trigger: "blur" }],
        // tag: [{ required: true, message: "标签不能为空", trigger: "blur" }],
        summary: [
          { required: true, message: "摘要不能为空", trigger: "blur" },
          {
            min: 1,
            max: 200,
            message: "摘要长度应在[1, 200]",
            trigger: "blur",
          },
        ],
        title: [
          { required: true, message: "标题不能为空", trigger: "blur" },
          {
            min: 1,
            max: 40,
            message: "标题长度应在[1, 40]",
            trigger: "blur",
          },
        ],
      },
      chooseTagName: "请选择标签",
      selectArticleIds: [],
      // 重新渲染表格
      refreshTable: true,
      refreshTableSearch: true,
      // 是否展开全部
      isExpandAll: false,
      isExpandAllSearch: false,
      addTagRules: {
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
      tagCategoryOptions: [],
      disableTagCategory: false,
      tagData: {},
      type: "0",
      tags: [],
      filterText: "",
      defaultProps: {
        children: "children",
        label: "name",
      },
      // 标签查询参数
      tagName: undefined,
      tagTableData: [],
      // 上传头像相关参数
      uploadImgaeData: {
        policy: "",
        signature: "",
        key: "",
        ossaccessKeyId: "",
        dir: "",
        host: "",
      },
      editor: undefined,
      // 新增修改文章lable宽度
      AddUpdateArticleFormLabelWidth: "270px",
      // 新增修改文章的form数据
      addUpdateArticleForm: {},
      // 新增修改文章对话框标题
      addUpdateArticleDialogTitle: "",
      // 新增修改文章对话框显示/隐藏
      addUpdateArticleDialogFormVisible: false,
      // 文章查询参数
      articleQueryParames: {
        title: undefined,
        authorUsername: undefined,
        pageSize: 5,
        pageNum: 1,
        tagId: undefined,
      },
      // 文章列表文章数
      articleListTotal: 0,
      // 文章表格数据
      articleData: [],
    };
  },

  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
  },

  mounted() {
    this.articleList();
  },

  methods: {
    handleSearchNodeClick(data) {
      if (data.parentId !== 0) {
        this.articleQueryParames.tagId = data.tagId;
        this.chooseTagName = data.name;
      }
    },
    handleSelectTag() {
      const name = "";
      tagNameList(name).then((res) => {
        this.tagTableData = handleTree(res.data, "tagId");
      });
    },
    handleChangeArticleStatus(row) {
      const status = row.status === "0" ? "1" : "0";
      const text = row.status == "0" ? "下架" : "发布";
      this.$confirm("确定要" + text + "吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          changeArticleStatus(row.articleId, status).then((res) => {
            if (res.code == 200) {
              this.$message({
                message: text + "成功",
                type: "success",
              });
              this.articleList();
            } else {
              this.$message.error(res.msg);
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消" + text,
          });
        });
    },
    // 复选框改变保存对应的userId
    handleSelectionChange(selection) {
      this.selectArticleIds = selection.map((item) => item.articleId);
    },
    // 处理删除文章
    handleDeleteArticle(row) {
      const articleIds = row.articleId || this.selectArticleIds;
      if (row.articleId == undefined && articleIds.length === 0) {
        this.$message({
          message: "请先勾选文章",
          type: "warning",
        });
        return;
      }
      this.$confirm("确定要删除吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteArticle(articleIds).then((res) => {
            if (res.code == 200) {
              this.$message({
                message: "删除成功",
                type: "success",
              });
              this.articleList();
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
    // 处理重置搜索参数
    handleResetSearchParameters() {
      this.articleQueryParames = {
        title: undefined,
        authorUsername: undefined,
        tagId: undefined,

        pageSize: 5,
        pageNum: 1,
      };
      this.chooseTagName = "请选择标签";
      this.articleList();
    },
    handleSearchArticle() {
      this.articleList();
    },
    handleNodeClick(data) {
      if (data.parentId !== 0) {
        this.append(data.tagId, data.name);
      }
    },
    // 处理展开折叠
    handleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    handleTypeChange(type) {
      this.tagData.parentId = undefined;
    },
    // 获取标签所有分类
    async handleTagCategoryList() {
      if (this.$refs.addTagForm) {
        this.$refs.addTagForm.resetFields();
      }
      await tagCategoryList().then((res) => {
        this.tagCategoryOptions = res.data;
      });
      this.type = "0";
      this.tagData = {
        name: undefined,
        parentId: undefined,
        orderNum: undefined,
      };
    },
    // 新建标签
    insertTag() {
      console.log(this.tagData);
      this.$refs["addTagForm"].validate((valid) => {
        if (valid) {
          addTag(this.tagData).then((res) => {
            if (res.code == 200) {
              this.tagData = {
                name: undefined,
                parentId: undefined,
                orderNum: undefined,
              };
              this.$message({
                message: "新增标签成功",
                type: "success",
              });
              this.handleTagList();
              this.handleTagCategoryList();
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      });
    },
    showButton(parentId) {
      if (parentId === 0) {
        return false;
      }
      return true;
    },
    remove(tagId, name) {
      const newTags = [];
      for (let index = 0; index < this.tags.length; index++) {
        if (this.tags[index].tagId !== tagId) {
          newTags.push(this.tags[index]);
        }
      }
      this.tags = newTags;
    },
    // 添加标签
    append(tagId, name) {
      if (this.tags.length >= 5) {
        this.$message.error("标签最多能添加5个");
        return;
      }
      for (let index = 0; index < this.tags.length; index++) {
        if (this.tags[index].tagId === tagId) {
          this.$message.error("该标签已添加");
          return;
        }
      }
      this.tags.push({ tagId, name });
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },

    // 处理查找标签列表
    handleTagList() {
      tagNameList(this.tagName).then((res) => {
        this.tagTableData = handleTree(res.data, "tagId");
      });
    },
    getEditor(editor) {
      this.editor = editor;
    },
    // 上传头像成功回调
    handleAvatarSuccess(res, file) {
      this.addUpdateArticleForm.thumbnail =
        "https://y3lq-blog.oss-cn-shenzhen.aliyuncs.com/" +
        this.uploadImgaeData.key;
    },
    // 上传头像前的检查并且获取上传签名
    async beforeAvatarUpload(file) {
      // const isJPG = file.type === "image/jpeg";
      const isLt10M = file.size / 1024 / 1024 < 10;
      // if (!isJPG) {
      //   this.$message.error("上传缩略图只能是 JPG 格式!");
      //   return
      // }
      if (!isLt10M) {
        this.$message.error("上传封面大小不能超过 10MB!");
        return;
      }
      // 获取上传文件到aliyun oss的签名
      let _self = this;
      await getSignature().then((res) => {
        if (res.code == 200) {
          _self.uploadImgaeData.policy = res.data.policy;
          _self.uploadImgaeData.signature = res.data.signature;
          _self.uploadImgaeData.ossaccessKeyId = res.data.accessid;
          _self.uploadImgaeData.key = res.data.dir + v4();
          _self.uploadImgaeData.dir = res.data.dir;
          _self.uploadImgaeData.host = res.data.host;
        }
      });
      // return isJPG && isLt10M;
    },
    // 处理提交或者修改文章
    handleSubmitArticle() {
      this.addUpdateArticleForm.content = this.editor.getHtml();
      const tagIds = [];
      this.tags.forEach((tag) => {
        tagIds.push(tag.tagId);
      });
      this.addUpdateArticleForm.tagIds = tagIds;
      // console.log(this.addUpdateArticleForm);
      if (this.addUpdateArticleForm.tagIds.length === 0) {
        this.$message.error("请选择标签");
        return;
      }
      if (this.editor.isEmpty()) {
        this.$message.error("文章内容不能为空");
        return;
      }
      this.$refs["addUpdateArticleForm"].validate((valid) => {
        if (valid) {
          if (this.addUpdateArticleForm.articleId == undefined) {
            addArticle(this.addUpdateArticleForm).then((res) => {
              if (res.code == 200) {
                this.addUpdateArticleDialogFormVisible = false;
                this.$message({
                  message: "新增文章成功",
                  type: "success",
                });
              } else {
                this.$message.error(res.msg);
              }
              this.articleList();
            });
          } else {
            updateArticle(this.addUpdateArticleForm).then((res) => {
              if (res.code == 200) {
                this.addUpdateArticleDialogFormVisible = false;
                this.$message({
                  message: "修改文章成功",
                  type: "success",
                });
              } else {
                this.$message.error(res.msg);
              }
              this.articleList();
            });
          }
        }
      });
    },
    // 重置新增修改对话框form
    resetAddUpdateArticleForm() {
      this.addUpdateArticleForm = {
        title: undefined,
        summary: undefined,
        thumbnail: undefined,
        tagIds: undefined,
        time: undefined,
        content: undefined,
        orderNum: undefined,
      };
    },
    // 处理编辑文章
    handleUpdateArticle(row) {
      if (this.$refs.addUpdateArticleForm) {
        this.$refs.addUpdateArticleForm.resetFields();
      }
      this.resetAddUpdateArticleForm();
      this.tags = [];
      if (this.editor !== undefined) {
        this.editor.clear();
      }
      this.$store.state.article.article = row.content;
      this.addUpdateArticleDialogFormVisible = true;
      this.addUpdateArticleDialogTitle = "修改文章";
      this.addUpdateArticleForm = row;
      this.tags = row.tagEntity;
      this.editor.setHtml(row.content);
      
    },
    // 处理新增文章
    handleAddArticle() {
      if (this.$refs.addUpdateArticleForm) {
        this.$refs.addUpdateArticleForm.resetFields();
      }
      this.resetAddUpdateArticleForm();
      this.tags = [];
      if (this.editor !== undefined) {
        this.editor.clear();
      }
      this.addUpdateArticleDialogTitle = "新增文章";
      this.addUpdateArticleDialogFormVisible = true;
    },
    // 获取文章列表
    articleList() {
      // console.log(this.articleQueryParames);
      getArticleList(this.articleQueryParames).then((res) => {
        if (res.code == 200) {
          this.articleData = res.data.list;
          this.articleListTotal = res.data.total;
        }
      });
    },
    // pageSize改变触发
    handleSizeChange(pageSize) {
      this.articleQueryParames.pageSize = pageSize;
      this.articleList();
    },
    // 当前页码改变触发
    handleCurrentChange(pageNum) {
      this.articleQueryParames.pageNum = pageNum;
      this.articleList();
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
.avatar-uploader .el-upload {
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 288px;
  height: 162px;
  line-height: 150px;
  text-align: center;
}
.avatar {
  width: 288px;
  height: 162px;
  display: block;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.el-tag + .el-tag {
  margin-left: 10px;
}
</style>

<style >
.customClass {
  background: #fefeff;
}
</style>