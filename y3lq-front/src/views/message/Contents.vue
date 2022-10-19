<template>
  <section id="content">
    <base-card>
      <!-- <v-card class="mx-auto mt-5 mb-5" color="#FAFAFA" max-width="980"> -->
      <v-form ref="messageForm" v-model="valid">
        <v-row class="ml-2 mr-2">
          <v-col
            class="mt-n4 mb-n5"
            cols="12"
            md="6"
            v-show="showAnonymousComponent"
          >
            <v-text-field
              :counter="15"
              :rules="nicknameRules"
              v-model="nickname"
              label="昵称*"
              required
            ></v-text-field>
          </v-col>
          <v-col
            class="mt-n4 mb-n5"
            cols="12"
            md="6"
            v-show="showAnonymousComponent"
          >
            <v-text-field
              v-model="email"
              :rules="emailRules"
              label="邮箱"
              required
            ></v-text-field>
          </v-col>
          <v-col cols="12" class="mb-n8">
            <v-textarea
              rows="5"
              outlined
              label="内容*"
              v-model="content"
              :rules="contentRules"
              auto-grow
            ></v-textarea>
            <!-- <mavon-editor
                previewBackground="#F5F5F5"
                :toolbars="markdownOption"
                ref="md"
                @save="saveMavon"
                @change="changeMavon"
                placeholder="请在这填写内容..."
                style="
                  z-index: 0;
                  height: 260px;
                  min-height: 200px;
                  min-width: 300px;
                "
                class="mt-5"
                fontSize="16px"
                :subfield="false"
              ></mavon-editor> -->
          </v-col>
          <!-- :block="$vuetify.breakpoint.smAndDown" -->
          <v-col class="mt-n3">
            <v-btn
              v-if="!this.$store.state.user.token"
              @click="handleLogin"
              class="text-center"
              color="primary"
              text
            >
              <strong>登 录</strong>
              <!-- <strong v-if="this.$store.state.user.token">提 交</strong>
                <strong v-if="!this.$store.state.user.token">登 录</strong> -->
            </v-btn>
            <v-btn
              class="text-center"
              color="primary"
              text
              @click="handleSubmit"
            >
              <strong>提 交</strong>
            </v-btn>
          </v-col>
        </v-row>
      </v-form>
      <!-- </v-card> -->
      <v-col
        class="mb-1"
        cols="12"
        v-for="message in messages"
        :key="message.messageId"
      >
        <v-card class="mx-auto" color="#FAFAFA" max-width="1000">
          <v-card-actions>
            <v-list-item>
              <!-- 头像 -->
              <v-list-item-avatar color="grey darken-3">
                <v-img class="elevation-6" :src="message.fromAvatar"></v-img>
              </v-list-item-avatar>
              <!-- 昵称日期 -->
              <v-list-item-content>
                <v-list-item-title
                  ><a
                    href="javascript:void(0);"
                    style="color: #01579b"
                    class="text-decoration-none"
                    >{{ getNickname(message) }}</a
                  ></v-list-item-title
                >
                <v-list-item-subtitle>{{
                  handleDate(message.createTime)
                }}</v-list-item-subtitle>
              </v-list-item-content>

              <v-row align="center" justify="end">
                <v-btn x-small text fab
                  ><v-icon color="grey darken-1" v-if="message.isLike == '1'"
                    >mdi-heart</v-icon
                  >
                  <v-icon color="primary" v-if="message.isLike == '0'"
                    >mdi-heart</v-icon
                  >
                </v-btn>
                <span class="subheading" style="color: #212121">{{
                  message.likeCount
                }}</span>

                <v-btn x-small text fab @click="openMessageContent(message)"
                  ><v-icon color="grey darken-1">mdi-chat</v-icon></v-btn
                >
                <!-- <span class="subheading mr-3">1</span> -->
              </v-row>
            </v-list-item>
          </v-card-actions>
          <!-- 评论内容 -->
          <v-card-text class="body-1 mt-n5 ml-3" style="color: #212121">
            {{ message.content }}
          </v-card-text>
          <v-row>
            <v-col cols="12" md="5">
              <v-text-field
                v-if="handleShowAnonymousInputBox(message)"
                v-model="message.nickname"
                class="mr-3 mb-n6 ml-3"
                label="昵称*"
                required
                dense
                outlined
              ></v-text-field>
            </v-col>
            <v-col cols="12" md="5">
              <v-text-field
                v-if="handleShowAnonymousInputBox(message)"
                v-model="message.email"
                class="mr-3 mb-n6 ml-3"
                label="邮箱"
                required
                dense
                outlined
              ></v-text-field>
            </v-col>
          </v-row>
          <v-textarea
            v-if="message.isComment"
            class="mt-4 mr-3 ml-3"
            rows="1"
            outlined
            :label="message.replyLabel"
            v-model="message.replyComment"
            auto-grow
            dense
            :append-outer-icon="'mdi-send'"
            @click:append-outer="sendCommentOrClose(message)"
          ></v-textarea>
          <v-card-text class="mt-n5" v-if="message.commentCount !== 0">
            <v-btn
              text
              x-small
              class="ml-3"
              :loading="loadComment"
              :disabled="loadComment"
              style="color: #616161"
              @click="handleMessageChildren(message)"
              ><v-icon small v-if="message.expand == undefined"
                >mdi-chevron-right</v-icon
              >
              <v-icon small v-if="message.expand">mdi-chevron-down</v-icon
              ><span v-if="message.expand">收起</span>
              <span v-if="message.expand == undefined">展开</span
              >{{ message.commentCount }}条回复</v-btn
            >
          </v-card-text>
          <!-- 回复评论 -->
          <div v-if="message.expand">
            <div
              class="ml-6"
              v-for="child in message.children"
              :key="child.messageId"
            >
              <v-divider></v-divider>
              <v-card-actions>
                <v-list-item class="grow mt-n1">
                  <v-list-item-avatar color="grey"
                    ><v-img class="elevation-6" :src="child.fromAvatar"></v-img
                  ></v-list-item-avatar>
                  <v-list-item-content>
                    <v-list-item-title
                      ><a
                        href="javascript:void(0);"
                        style="color: #01579b"
                        class="text-decoration-none"
                        >{{ getNickname(child) }}</a
                      ></v-list-item-title
                    >
                    <v-list-item-subtitle>{{
                      handleDate(child.createTime)
                    }}</v-list-item-subtitle>
                  </v-list-item-content>

                  <v-row align="center" justify="end" class="">
                    <v-btn x-small text fab @click="openMessageContent(child)"
                      ><v-icon color="grey darken-1">mdi-chat</v-icon></v-btn
                    >
                  </v-row>
                </v-list-item>
              </v-card-actions>
              <v-card-text class="body-1 mt-n5 ml-3" style="color: #212121">
                回复
                <a
                  href="javascript:void(0);"
                  style="color: #01579b"
                  class="text-decoration-none"
                  >@{{ getNickname(message) }}</a
                >
                {{ child.content }}
              </v-card-text>
              <v-row>
                <v-col cols="12" md="5">
                  <v-text-field
                    v-if="handleShowAnonymousInputBox(child)"
                    v-model="child.nickname"
                    class="mr-3 mb-n6 ml-3"
                    label="昵称*"
                    required
                    dense
                    outlined
                  ></v-text-field>
                </v-col>
                <v-col cols="12" md="5">
                  <v-text-field
                    v-if="handleShowAnonymousInputBox(child)"
                    v-model="child.email"
                    class="mr-3 mb-n6 ml-3"
                    label="邮箱"
                    required
                    dense
                    outlined
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-textarea
                v-if="child.isComment"
                class="mt-4 mr-3 ml-3"
                rows="1"
                outlined
                :label="child.replyLabel"
                v-model="child.replyComment"
                auto-grow
                dense
                :append-outer-icon="'mdi-send'"
                @click:append-outer="sendCommentCommentOrClose(child, message)"
              ></v-textarea>
            </div>
          </div>
        </v-card>
      </v-col>
      <v-col cols="12" v-if="this.total >= 10">
        <div class="text-center">
          <v-pagination
            v-model="pageNum"
            :length="pages"
            :total-visible="6"
          ></v-pagination>
        </div>
      </v-col>
    </base-card>
  </section>
</template>

<script>
import moment from "moment";
import { mavonEditor } from "mavon-editor";
import "mavon-editor/dist/css/index.css";
import CommentContents from "@/views/posts/CommentContents";
import {
  commentMessage,
  messageList,
  comment,
  messageChildren,
} from "@/api/message";
import store from "@/store";
export default {
  data() {
    return {
      pageNum: 1,
      pageSize: 9,
      total: 0,
      pages: 0,
      markdownOption: {
        bold: true,
        italic: true,
        link: true, // 链接
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        preview: true, // 预览
        code: true,
      },
      messages: undefined,
      valid: true,
      content: "",
      loadComment: false,
      html: "",
      markdown: "",
      nickname: undefined,
      email: undefined,
      nicknameRules: [
        (v) => !!v || " 匿名评论昵称不能为空",
        (v) => (v && v.length <= 15) || "匿名评论昵称长度应在[1, 15]",
      ],
      email: "",
      emailRules: [
        // v => !!v || 'E-mail is required',
        (v) => /.+@.+\..+/.test(v) || "邮箱格式错误",
      ],

      contentRules: [
        (v) => !!v || "留言不能为空",
        (v) => (v && v.length <= 400) || "留言长度应在[1, 400]",
      ],
    };
  },

  computed: {
    showAnonymousComponent() {
      if (
        this.$store.state.user.token === undefined ||
        this.$store.state.user.token === ""
      ) {
        return true;
      } else {
        return false;
      }
    },
  },
  mounted() {
    this.getMessageList();
  },
  methods: {
    // 获取指定留言的所有评论
    handleMessageChildren(message) {
      const messageId = message.messageId;
      if (message.expand === undefined) {
        // 获取该条评论所有子评论
        this.loadComment = true;
        messageChildren(messageId).then((res) => {
          message.children = res.data;
          message.expand = true;
          this.$forceUpdate();
        });
        this.loadComment = false;
      } else {
        message.expand = undefined;
      }
      console.log(message.expand);
      this.$forceUpdate();
    },
    handleShowAnonymousInputBox(message) {
      const flag = message.isComment == true ? true : false;
      if (flag) {
        if (!this.$store.state.user.token) {
          return true;
        }
      }
      return false;
    },
    // 发送评论留言评论或者关闭输入框
    sendCommentCommentOrClose(child, message) {
      if (child.replyComment == undefined || child.replyComment == "") {
        child.isComment = false;
        this.$forceUpdate();
        return;
      }
      console.log(child, message);
      const content = child.replyComment;
      if (this.$store.state.user.token) {
        // 登录评论留言
        comment(
          content,
          "0",
          undefined,
          undefined,
          message.messageId,
          child.fromUserId,
          undefined
        ).then((res) => {
          child.replyComment = undefined;
          message.children.unshift(res.data);
          message.commentCount = message.commentCount + 1;
        });
        child.replyComment = undefined;
      } else {
        // 匿名评论留言
        comment(
          content,
          "1",
          child.nickname,
          child.email,
          message.messageId,
          child.fromUserId,
          child.fromAnonymousNickname
        ).then((res) => {
          child.replyComment = undefined;
          message.children.unshift(res.data);
          message.commentCount = message.commentCount + 1;
        });
        child.replyComment = undefined;
        child.email = undefined;
        child.nickname = undefined;
      }
    },
    // 发送评论留言或者关闭输入框
    sendCommentOrClose(message) {
      if (message.replyComment == undefined || message.replyComment == "") {
        message.isComment = false;
        this.$forceUpdate();
        return;
      }
      const content = message.replyComment;
      if (this.$store.state.user.token) {
        // 登录评论留言
        comment(
          content,
          "0",
          undefined,
          undefined,
          message.messageId,
          message.fromUserId,
          undefined
        ).then((res) => {
          message.replyComment = undefined;
          if (message.children === undefined) {
            message.children = [];
          }
          message.children.unshift(res.data);
          message.commentCount = message.commentCount + 1;
        });
        message.replyComment = undefined;
      } else {
        // 匿名评论留言
        comment(
          content,
          "1",
          message.nickname,
          message.email,
          message.messageId,
          message.fromUserId,
          message.fromAnonymousNickname
        ).then((res) => {
          message.replyComment = undefined;
          if (message.children === undefined) {
            message.children = [];
          }
          message.children.unshift(res.data);
          message.commentCount = message.commentCount + 1;
        });
        message.replyComment = undefined;
        message.email = undefined;
        message.nickname = undefined;
      }
    },

    // 打开留言评论输入框
    openMessageContent(message) {
      if (message.isComment == undefined) {
        message.isComment = true;
      } else {
        message.isComment = undefined;
      }
      const nickname =
        message.type == "1"
          ? message.fromAnonymousNickname
          : message.fromNickname;
      message.replyLabel = "回复 " + nickname;
      console.log(message);
      this.$forceUpdate();
    },
    // 获取昵称
    getNickname(message) {
      const nickname =
        message.type == "1"
          ? message.fromAnonymousNickname
          : message.fromNickname;
      return nickname;
    },
    // 获取留言列表
    getMessageList() {
      messageList(this.pageNum, this.pageSize).then((res) => {
        this.messages = res.data.list;
        this.pages = res.data.pages;
        this.total = res.total;
      });
    },
    // 处理时间格式化
    handleDate(date) {
      return moment(date).format("YYYY-MM-DD");
    },
    resetData() {
      this.content = "";
      this.nickname = "";
      this.email = "";
      this.$refs.messageForm.resetValidation();
    },
    handleLogin() {
      store.state.user.loginDialog = true;
    },

    // 如果未登录，则可匿名评论，展示匿名组件
    // showAnonymousComponent() {
    // console.log(store.state.user.token);
    // if (store.state.user.token) {
    //   return false;
    // } else {
    //   return true;
    // }
    // this.$store.state.user.token !==undefined ?true:false
    // },
    // 输入事件
    // changeMavon() {
    //   this.html = this.$refs.md.d_render;

    // },
    // 保存
    // saveMavon(value, render) {

    // },
    // 提交留言
    handleSubmit() {
      const content = this.content;
      this.$refs.messageForm.validate();
      if (this.$store.state.user.token && this.valid) {
        // 已登录
        const type = "0";
        commentMessage(content, type, undefined, undefined).then((res) => {
          this.messages.unshift(res.data);
          this.total = this.total + 1;
          console.log(res);
        });
      }
      if (this.valid) {
        // 匿名评论
        const fromNickname = this.nickname;
        const fromEmail = this.email;
        const type = "1";
        commentMessage(content, type, fromNickname, fromEmail).then((res) => {
          this.messages.unshift(res.data);
          this.total = this.total + 1;
          console.log(res);
        });
      }
      this.resetData();
    },
  },
  components: { CommentContents, mavonEditor },

  name: "SampleContents",
};
</script>

<style scoped>
.mavonEditor {
  width: 100%;
  height: 100%;
}
</style>

