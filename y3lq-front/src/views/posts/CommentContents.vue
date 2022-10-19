<template>
  <!-- 评论 -->
  <v-form>
    <v-row>
      <v-col cols="12">
        <v-textarea
          label="评论内容"
          v-model="comment.content"
          outlined
          rows="4"
          counter
        />
      </v-col>
      <v-col class="mb-5">
        <v-btn
          v-if="this.$store.state.user.token"
          class="text-center mt-n7"
          color="primary"
          block
          rounded
          @click="handleSubmitComment()"
        >
          <strong>提交评论</strong>
          <v-icon>mdi-swap-horizontal</v-icon></v-btn
        >
        <v-btn
          v-if="!this.$store.state.user.token"
          class="text-center mt-n7"
          color="primary"
          block
          rounded
          @click="handleSubmitComment()"
        >
          <strong>登录</strong>
          <v-icon small>mdi-login</v-icon></v-btn
        >
      </v-col>
      <v-col
        class="mb-1"
        cols="12"
        v-for="comment in comments"
        :key="comment.commentId"
      >
        <v-card class="mx-auto" color="#FAFAFA" max-width="1000">
          <v-card-actions>
            <v-list-item>
              <!-- 头像 -->
              <v-list-item-avatar color="grey darken-3">
                <v-img
                  class="elevation-6"
                  @click="handleShowUserData(comment.fromUserId)"
                  :src="comment.avatar"
                ></v-img>
              </v-list-item-avatar>
              <!-- 昵称日期 -->
              <v-list-item-content>
                <v-list-item-title
                  ><a
                    href="javascript:void(0);"
                    @click="handleShowUserData(comment.fromUserId)"
                    style="color: #01579b"
                    class="text-decoration-none"
                    >{{ comment.fromNickname }}</a
                  ></v-list-item-title
                >
                <v-list-item-subtitle>{{
                  handleDate(comment.createTime)
                }}</v-list-item-subtitle>
              </v-list-item-content>

              <v-row align="center" justify="end">
                <v-btn x-small text fab @click="handleLikeOrCancelLike(comment)"
                  ><v-icon color="grey darken-1" v-if="comment.isLike == '0'"
                    >mdi-heart</v-icon
                  >
                  <v-icon color="primary" v-if="comment.isLike == '1'"
                    >mdi-heart</v-icon
                  >
                </v-btn>
                <span class="subheading" style="color: #212121">{{
                  comment.likeCount
                }}</span>

                <v-btn
                  x-small
                  text
                  fab
                  @click="handleOpenComment(comment, comment.fromNickname)"
                  ><v-icon color="grey darken-1">mdi-chat</v-icon></v-btn
                >
                <v-btn
                  v-if="handleShowDeleteBtn(comment)"
                  x-small
                  text
                  fab
                  @click="handleDeleteComment(comment, comments)"
                  ><v-icon color="grey darken-1">mdi-delete</v-icon></v-btn
                >
                <!-- <span class="subheading mr-3">1</span> -->
              </v-row>
            </v-list-item>
          </v-card-actions>
          <!-- 评论内容 -->
          <v-card-text class="body-1 mt-n5 ml-3" style="color: #212121">
            {{ comment.content }}
          </v-card-text>
          <v-textarea
            autofocus
            v-if="comment.isComment"
            class="mt-4 mr-3 ml-3"
            rows="1"
            outlined
            :label="comment.replyLabel"
            v-model="comment.replyComment"
            auto-grow
            dense
            :append-outer-icon="'mdi-send'"
            @click:append-outer="sendCommentOrClose(comment)"
          ></v-textarea>
          <v-card-text class="mt-n5" v-if="comment.childrenCommentCount !== 0">
            <v-btn
              text
              x-small
              :loading="loadComment"
              :disabled="loadComment"
              class="ml-3"
              @click="handleExpand(comment)"
              style="color: #616161"
              ><v-icon small v-if="comment.expand == undefined"
                >mdi-chevron-right</v-icon
              >
              <v-icon small v-if="comment.expand">mdi-chevron-down</v-icon
              ><span v-if="comment.expand">收起</span>
              <span v-if="comment.expand == undefined">展开</span
              >{{ comment.childrenCommentCount }}条回复</v-btn
            >
          </v-card-text>

          <!-- 回复评论 -->
          <div v-if="comment.expand">
            <div
              class="ml-6"
              v-for="child in comment.children"
              :key="child.commentId"
            >
              <v-divider></v-divider>
              <v-card-actions>
                <v-list-item class="grow mt-n1">
                  <v-list-item-avatar color="grey"
                    ><v-img
                      class="elevation-6"
                      @click="handleShowUserData(child.fromUserId)"
                      :src="child.avatar"
                    ></v-img
                  ></v-list-item-avatar>
                  <v-list-item-content>
                    <v-list-item-title
                      ><a
                        href="javascript:void(0);"
                        @click="handleShowUserData(child.fromUserId)"
                        style="color: #01579b"
                        class="text-decoration-none"
                        >{{ child.fromNickname }}</a
                      ></v-list-item-title
                    >
                    <v-list-item-subtitle>{{
                      handleDate(child.createTime)
                    }}</v-list-item-subtitle>
                  </v-list-item-content>

                  <v-row align="center" justify="end" class="">
                    <!-- <v-btn
                      x-small
                      text
                      fab
                      @click="handleLikeOrCancelLike(child)"
                      ><v-icon color="grey darken-1" v-if="child.isLike == '0'"
                        >mdi-heart</v-icon
                      >
                      <v-icon color="primary" v-if="child.isLike == '1'"
                        >mdi-heart</v-icon
                      >
                    </v-btn>
                    <span class="subheading" style="color: #212121">{{
                      child.likeCount
                    }}</span> -->
                    <v-btn
                      x-small
                      text
                      fab
                      @click="handleOpenComment(child, child.fromNickname)"
                      ><v-icon color="grey darken-1">mdi-chat</v-icon></v-btn
                    >
                    <v-btn
                      x-small
                      text
                      fab
                      v-if="handleShowDeleteBtn(child)"
                      @click="handleDeleteChildComment(child, comment)"
                      ><v-icon color="grey darken-1">mdi-delete</v-icon></v-btn
                    >
                  </v-row>
                </v-list-item>
              </v-card-actions>
              <v-card-text class="body-1 mt-n5 ml-3" style="color: #212121">
                回复
                <a
                  href="javascript:void(0);"
                  @click="handleShowUserData(child.toUserId)"
                  style="color: #01579b"
                  class="text-decoration-none"
                  >@{{ comment.fromNickname }}</a
                >
                {{ child.content }}
              </v-card-text>
              <v-textarea
                autofocus
                v-if="child.isComment"
                class="mt-4 mr-3 ml-3"
                rows="1"
                outlined
                :label="child.replyLabel"
                v-model="child.replyComment"
                auto-grow
                dense
                :append-outer-icon="'mdi-send'"
                @click:append-outer="sendChildCommentOrClose(child, comment)"
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
    </v-row>
    <person-profile />
  </v-form>
</template>

<script>
import moment from "moment";

import {
  getArticleCommentList,
  getArticleCommentChildren,
  commentArticle,
  postLikeArticleComment,
  cancelLikeArticleComment,
  deleteArticleComment,
} from "@/api/article";
import PersonProfile from "../../components/PersonProfile.vue";

export default {
  components: { PersonProfile },
  name: "CommentContents",

  data() {
    return {
      // 评论
      comment: {
        content: "",
        articleId: undefined,
      },
      loadComment: false,
      pageNum: 1,
      pageSize: 9,
      total: 0,
      pages: 0,
      zhan: false,
      commentss: [
        {
          commentId: 1,
          articleId: 1,
          firstCommentId: 0,
          avatar:
            "https://pic2.zhimg.com/80/v2-856c0608949c6098ae82be1c8fcceb25_1440w.jpg",
          likeCount: 4,
          toNickname: "",
          toUserId: undefined,
          fromUserId: 1,
          fromNickname: "Y3lq",
          // fromEmail:"",
          content:
            " 极简主义（Minimalism）可以定义为：当一件作品的内容被减至最低限度时所散发的完美感觉。当物体的所有组成部分、所有细节以及所有的连接都被减少压缩至精华时，他就会拥有这种特性，这就是去掉非本质元素的结果。",
          createTime: "2020-8-11 14:22",
          children: [
            {
              commentId: 2,
              articleId: 1,
              firstCommentId: 1,
              avatar:
                "https://pic2.zhimg.com/80/v2-856c0608949c6098ae82be1c8fcceb25_1440w.jpg",
              toNickname: "Y3lq",
              toUserId: 1,
              fromUserId: 2,
              fromNickname: "10t7",
              // fromEmail:"",
              content: "罚款不发啦发诶发饿哦if包诶发吧饿哦发布那饿哦",
              createTime: "2020-8-11 14:33",
            },
            {
              commentId: 3,
              articleId: 1,
              firstCommentId: 1,
              avatar:
                "https://pic2.zhimg.com/80/v2-856c0608949c6098ae82be1c8fcceb25_1440w.jpg",
              toNickname: "10t7",
              toUserId: 2,
              fromUserId: 1,
              fromNickname: "Y3lq",
              // fromEmail:"",
              content: "罚款不发啦发诶发发吧饿哦发布那饿哦",
              createTime: "2020-8-11 14:44",
            },
          ],
        },
      ],
      comments: [],
    };
  },
  props: {
    type: {
      type: String,
      require: true,
    },
  },

  mounted() {
    this.handleGetCommentList();
  },

  methods: {
    handleDeleteChildComment(child, comment) {
      deleteArticleComment(child.commentId).then((res) => {
        const comments = comment.children;
        const newChildren = [];
        comments.forEach((commentsss) => {
          if (commentsss.commentId !== child.commentId) {
            newChildren.push(commentsss);
          }
        });
        comment.children = newChildren;
        comment.childrenCommentCount = comment.childrenCommentCount - 1;
        this.$forceUpdate();
      });
    },
    // 删除指定评论
    handleDeleteComment(comment, comments) {
      const newComments = [];
      deleteArticleComment(comment.commentId).then((res) => {
        comments.forEach((commenttt) => {
          if (commenttt.commentId !== comment.commentId) {
            newComments.push(commenttt);
          }
        });
        this.comments = newComments;
      });
    },
    handleShowDeleteBtn(comment) {
      let jwt = require("jsonwebtoken");
      if (this.$store.state.user.token) {
        let token = jwt.decode(this.$store.state.user.token);
        if (token.user_id == comment.fromUserId) {
          return true;
        }
        return false;
      }
      return false;
    },
    handleShowUserData(userId) {
      this.$store.state.user.personProfileInfoUserId = userId;
      this.$store.state.user.personProfileDialog = true;
    },
    // 处理点赞评论或者取消点赞
    handleLikeOrCancelLike(comment) {
      if (
        this.$store.state.user.token === undefined ||
        this.$store.state.user.token === ""
      ) {
        this.$store.state.user.loginDialog = true;
        return;
      }
      if (comment.isLike == "0") {
        // 发送点赞请求
        postLikeArticleComment(comment.commentId).then((res) => {
          comment.isLike = "1";
          comment.likeCount++;
        });
      }
      if (comment.isLike == "1") {
        // 发送取消点赞请求
        cancelLikeArticleComment(comment.commentId).then((res) => {
          comment.isLike = "0";
          comment.likeCount--;
        });
      }
      // this.$forceUpdate()
    },
    // 提交评论的评论或者隐藏评论的评论输入框
    sendChildCommentOrClose(child, comment) {
      // console.log(comment.childrenCommentCount)
      // 关闭
      if (child.replyComment == undefined || child.replyComment == "") {
        child.isComment = false;
        this.$forceUpdate();
      }
      // console.log(child.replyComment);
      // 发送评论
      if (child.replyComment !== undefined && child.replyComment !== "") {
        const sendComment = {
          articleId: this.$route.params.articleId,
          firstCommentId: comment.commentId,
          content: child.replyComment,
          toUserId: child.fromUserId,
        };
        commentArticle(sendComment).then((res) => {
          child.replyComment = undefined;
          comment.children.unshift(res.data);
          comment.commentCount = comment.commentCount + 1;
          // console.log(comment.childrenCommentCount)
          this.$forceUpdate();
        });
        this.$forceUpdate();
      }
    },
    // 打开关闭评论输入框
    handleOpenComment(comment, toNickname) {
      // 判断登录
      if (
        this.$store.state.user.token === undefined ||
        this.$store.state.user.token === ""
      ) {
        this.$store.state.user.loginDialog = true;
        return;
      }
      if (comment.isComment == undefined) {
        comment.isComment = true;
      } else {
        comment.isComment = undefined;
      }
      comment.replyLabel = "回复 " + toNickname;
      this.$forceUpdate();
    },
    // 提交评论或者移除评论输入框(回复评论)
    sendCommentOrClose(comment) {
      // 关闭
      if (comment.replyComment == undefined || comment.replyComment == "") {
        comment.isComment = false;
        this.$forceUpdate();
      }
      console.log(comment.replyComment);
      // 发送评论
      if (comment.replyComment !== undefined && comment.replyComment !== "") {
        const sendComment = {
          articleId: this.$route.params.articleId,
          firstCommentId: comment.commentId,
          content: comment.replyComment,
          toUserId: comment.fromUserId,
        };
        commentArticle(sendComment).then((res) => {
          comment.replyComment = undefined;
          if (comment.children === undefined) {
            comment.children = [];
          }
          comment.children.unshift(res.data);
          comment.childrenCommentCount = comment.childrenCommentCount + 1;
        });
        this.$forceUpdate();
      }
    },
    // 处理显示分页器
    handleShowPagination() {
      if (this.comments.length > 10) {
        return true;
      } else {
        return false;
      }
    },
    // 重置评论
    resetComment() {
      this.comment = {
        content: "",
        articleId: undefined,
      };
    },
    // 提交评论（评论文章）
    handleSubmitComment() {
      if (
        this.$store.state.user.token === undefined ||
        this.$store.state.user.token === ""
      ) {
        this.$store.state.user.loginDialog = true;
        return;
      }
      if (this.comment.content == undefined || this.comment.content == "") {
        return;
      }
      this.comment.articleId = this.$route.params.articleId;
      commentArticle(this.comment).then((res) => {
        this.comments.unshift(res.data);
        this.total = this.total + 1;
      });
      this.resetComment();
    },
    // 处理时间格式化
    handleDate(date) {
      return moment(date).format("YYYY-MM-DD");
    },

    // 获取评论列表
    handleGetCommentList() {
      const articleId = this.$route.params.articleId;
      getArticleCommentList(articleId, this.pageNum, this.pageSize).then(
        (res) => {
          this.comments = res.data.list;
          this.pages = res.data.pages;
          this.total = res.data.total;
        }
      );
    },
    // 打开关闭子评论
    handleExpand(comment) {
      // console.log(comment.expand);
      if (comment.expand === undefined) {
        this.loadComment = true;
        // 获取该条评论所有子评论
        getArticleCommentChildren(comment.commentId).then((res) => {
          comment.children = res.data;
          comment.expand = true;
          this.$forceUpdate();
        });
        this.loadComment = false;
      } else {
        comment.expand = undefined;
      }
      this.$forceUpdate();
      // console.log(comment.expand);
    },
  },
  watch: {
    total() {
      if (this.total % 10 === 0) {
        this.handleGetCommentList();
      }
    },
    pageNum() {
      getArticleCommentList(
        this.$route.params.articleId,
        this.pageNum,
        this.pageSize
      ).then((res) => {
        this.pages = res.data.pages;
        this.comments = res.data.list;
        this.total = res.data.total;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
</style>