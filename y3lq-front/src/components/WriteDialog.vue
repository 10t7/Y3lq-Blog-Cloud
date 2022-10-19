<template>
  <v-row justify="center">
    <v-dialog
      v-model="dialog"
      fullscreen
      hide-overlay
      transition="dialog-bottom-transition"
    >
      <!-- <template v-slot:activator="{ on, attrs }">
        <v-btn color="primary" dark v-bind="attrs" v-on="on">
          Open Dialog
        </v-btn>
      </template> -->
      <v-card color="#FAFAFA">
        <v-toolbar color="#FFFFFF">
          <v-btn icon @click="dialog = false">
            <v-icon>mdi-arrow-left</v-icon>
          </v-btn>
          <v-toolbar-title>编写文章</v-toolbar-title>
          <v-spacer></v-spacer>
          <!-- <v-toolbar-items> -->
          <!-- <v-btn
            class="mr-5"
            rounded
            large
            outlined
            @click="dialog = false"
            color="primary"
            >保存草稿</v-btn
          > -->
          <!-- <v-btn rounded large color="#FFFFFF" @click="dialog = false"
            >保存草稿</v-btn -->

          <!-- </v-toolbar-items> -->
          <!-- <v-toolbar-items> -->
          <v-btn rounded large color="primary" @click="handleSubmit"
            >发布文章</v-btn
          >
          <!-- </v-toolbar-items> -->
        </v-toolbar>
        <v-form class="ml-15 mr-15 mt-5">
          <v-row>
            <v-col cols="12">
              <v-text-field
                :counter="40"
                label="请输入文章标题*"
                required
                v-model="article.title"
              ></v-text-field
            ></v-col>
            <v-col cols="12">
              <v-chip color="success" outlined label> 请选择文章标签 </v-chip>
              <v-chip
                class="ma-2"
                close
                color="#FF5722"
                text-color="white"
                close-icon="mdi-delete"
                v-for="tag in selectTag"
                :key="tag.tagId"
                small
                @click:close="closeTag(tag)"
              >
                <v-avatar left>
                  <v-icon>mdi-label</v-icon>
                </v-avatar>
                {{ tag.name }}
              </v-chip>

              <v-btn
                class="mx-2"
                outlined
                dark
                color="primary"
                @click="handleTag"
                small
              >
                <v-icon dark>mdi-plus</v-icon>
              </v-btn>
            </v-col>
            <v-col cols="12">
              <mavon-editor
                @change="changeMavon"
                previewBackground="#F5F5F5"
                ref="md"
                v-model="content"
                :toolbars="toolbars"
                placeholder="请在这填写文章内容*..."
                style="
                  z-index: 0;
                  height: 260px;
                  min-height: 600px;
                  min-width: 300px;
                "
                class="mt-5"
                fontSize="16px"
              ></mavon-editor>
            </v-col>
            <v-col cols="12" md="8">
              <v-textarea
                solo
                name="input-7-4"
                label="请输入文章摘要*"
                counter="100"
                v-model="article.summary"
              ></v-textarea>
            </v-col>
            <v-col cols="12" md="4">
              <v-file-input
                label="文章封面上传"
                filled
                dense
                prepend-icon="mdi-image-plus"
                @change="handleUploadPicture"
                v-model="file"
              ></v-file-input>
              <!-- 
              <v-img
                v-if="article.thumbnail"
                :src="article.thumbnail"
              /> -->
            </v-col>
          </v-row>
        </v-form>
      </v-card>
      <v-dialog v-model="tagDialog" width="500">
        <v-card max-width="500" class="mx-auto">
          <v-toolbar color="#FFFFFF">
            <v-toolbar-title>添加标签</v-toolbar-title>
          </v-toolbar>

          <v-list rounded>
            <v-list-group v-for="tag in tagTreeList" :key="tag.tagId" no-action>
              <template v-slot:activator>
                <v-list-item-content>
                  <v-list-item-title v-text="tag.name"></v-list-item-title>
                </v-list-item-content>
              </template>

              <v-list-item v-for="child in tag.children" :key="child.tagId">
                <v-list-item-action>
                  <v-list-item-title v-text="child.name"></v-list-item-title>
                </v-list-item-action>
                <v-spacer />
                <v-list-item-action>
                  <v-btn icon @click="handleAddTag(child)">
                    <v-icon dark color="grey lighten-1">mdi-plus</v-icon>
                  </v-btn>
                </v-list-item-action>
              </v-list-item>
            </v-list-group>
          </v-list>
        </v-card>
      </v-dialog>
    </v-dialog>
  </v-row>
</template>

<script>
import { mavonEditor } from "mavon-editor";
import { addArticle, getUploadPictureSignature } from "@/api/article";
import "mavon-editor/dist/css/index.css";
import { v4 } from "uuid";
import { tagTreeList } from "@/api/article";

import axios from "axios";
export default {
  components: { mavonEditor },
  data() {
    return {
      uploadImgaeData: {
        policy: "",
        signature: "",
        key: "",
        ossaccessKeyId: "",
        dir: "",
        host: "",
      },
      toolbars: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: true, // 上角标
        subscript: true, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: true, // code
        table: true, // 表格
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空

        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true, // 预览
      },
      file: undefined,
      tagTreeList: [],
      tagDialog: false,
      notifications: false,
      sound: true,
      widgets: false,
      selectTag: [],
      html: undefined,
      markdown: undefined,
      content: "",
      article: {
        title: undefined,
        summary: undefined,
        content: undefined,
        thumbnail: undefined,
        tagIds: [],
      },
    };
  },

  methods: {
    // getThumbnail() {
    //   return this.article.thumbnail;
    // },
    // 处理上传图片
    handleUploadPicture() {
      this.beforeUploadPicture();
    },
    // 上传图片前置工作
    async beforeUploadPicture() {
      if (this.file.type !== "image/jpeg") {
        this.$store.dispatch("snackbar/openSnackbar", {
          msg: "只接收图片且为jpeg格式",
          color: "error",
        });
        this.file = undefined;
        return;
      }
      const isLt10M = this.file.size / 1024 / 1024 < 10;
      if (!isLt10M) {
        this.$store.dispatch("snackbar/openSnackbar", {
          msg: "上传封面不能超过10MB",
          color: "error",
        });
        this.file = undefined;
        return;
      }
      let _self = this;
      await getUploadPictureSignature().then((res) => {
        _self.uploadImgaeData.policy = res.data.policy;
        _self.uploadImgaeData.signature = res.data.signature;
        _self.uploadImgaeData.ossaccessKeyId = res.data.accessid;
        _self.uploadImgaeData.key = res.data.dir + v4();
        _self.uploadImgaeData.dir = res.data.dir;
        _self.uploadImgaeData.host = res.data.host;
      });
      axios({
        url: "https://y3lq-blog.oss-cn-shenzhen.aliyuncs.com",
        method: "post",
        headers: {
          "Access-Control-Allow-Origin": "*",
          "Content-Type": "multipart/form-data",
        },
        data: {
          policy: this.uploadImgaeData.policy,
          signature: this.uploadImgaeData.signature,
          key: this.uploadImgaeData.key,
          ossaccessKeyId: this.uploadImgaeData.ossaccessKeyId,
          dir: this.uploadImgaeData.dir,
          host: this.uploadImgaeData.host,
          file: this.file,
        },
        timeout: 5000,
      });
      // .then((value) => {
      //   // 获取请求成功的结果
      //   console.log(value);
      // })
      // .catch((error) => {
      //   // 获取请求发送失败的结果
      //   console.log("error", error);
      // });
      this.article.thumbnail =
        "https://y3lq-blog.oss-cn-shenzhen.aliyuncs.com/" +
        this.uploadImgaeData.key;
      this.$forceUpdate();
    },
    changeMavon() {
      this.article.content = this.$refs.md.d_render;
      // console.log(this.content)
      // this.markdown = this.$refs.md.d_value;
    },

    // 新增文章
    handleSubmit() {
      this.article.tagIds = [];
      this.selectTag.forEach((tag) => {
        this.article.tagIds.push(tag.tagId);
      });
      if (this.article.title == undefined || this.article.title == "") {
        this.$store.dispatch("snackbar/openSnackbar", {
          msg: "请输入文章标题",
          color: "error",
        });
        return;
      }
      if (this.article.tagIds == undefined || this.article.tagIds.length ==0) {
        this.$store.dispatch("snackbar/openSnackbar", {
          msg: "请选择文章标签",
          color: "error",
        });
        return;
      }
      if (this.article.content == undefined || this.article.content == "") {
        this.$store.dispatch("snackbar/openSnackbar", {
          msg: "请输入文章内容",
          color: "error",
        });
        return;
      }
      if (this.article.summary == undefined || this.article.summary == "") {
        this.$store.dispatch("snackbar/openSnackbar", {
          msg: "请输入文章简介",
          color: "error",
        });
        return;
      }
      addArticle(this.article).then((res) => {
        if (res.code == 200) {
          this.$store.dispatch("snackbar/openSnackbar", {
            msg: "新增文章成功",
            color: "success",
          });
          this.article = {
            title: undefined,
            summary: undefined,
            content: undefined,
            thumbnail: undefined,
            tagIds: [],
          };
          this.selectTag = [];
          this.$store.state.app.writeDialog = false;

          this.content = "";
        }
      });
    },
    closeTag(tag) {
      // console.log(tag)
      const newTags = [];
      for (let index = 0; index < this.selectTag.length; index++) {
        if (this.selectTag[index].tagId !== tag.tagId) {
          newTags.push(this.selectTag[index]);
        }
      }
      this.selectTag = newTags;
    },
    // 为文章添加标签
    handleAddTag(tag) {
      // console.log(tag)
      // console.log(tagTreeList)
      // console.log(this.selectTag)
      if (this.selectTag.length >= 5) {
        this.$store.dispatch("snackbar/openSnackbar", {
          msg: "标签最多能添加五个",
          color: "error",
        });
        return;
      }
      for (let index = 0; index < this.selectTag.length; index++) {
        if (this.selectTag[index].tagId === tag.tagId) {
          this.$store.dispatch("snackbar/openSnackbar", {
            msg: "该标签已添加",
            color: "error",
          });
          return;
        }
      }
      this.selectTag.push(tag);
    },
    getTagTreeList() {
      if (
        this.$store.state.user.token === undefined ||
        this.$store.state.user.token === ""
      ) {
        return;
      }
      tagTreeList().then((res) => {
        // this.$store.state.app.tagTreeList = res.data;
        this.tagTreeList = res.data;
      });
    },
    handleTag() {
      this.tagDialog = true;
    },
  },
  computed: {
    dialog: {
      get() {
        this.getTagTreeList();
        return this.$store.state.app.writeDialog;
      },
      set(newValue) {
        this.$store.commit("app/CLOSE_WRITEDIALOG", newValue);
      },
    },
  },
};
</script>