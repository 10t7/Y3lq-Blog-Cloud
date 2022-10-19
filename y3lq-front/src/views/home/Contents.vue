<template>
  <section id="talk">
    <base-card>
      <!-- <v-card-text>
        <v-form>
          <v-container class="px-0"> -->
      <!-- <v-row>
              <v-col
                cols="12"
                md="6"
              >
                <v-text-field
                  label="昵称"
                  outlined
                />
              </v-col>
              <v-col
                cols="12"
                md="6"
              >
                <v-text-field
                  label="邮箱"
                  outlined
                />
              </v-col>
              <v-col cols="12">
                <v-textarea
                  label="内容"
                  outlined
                  rows="7"
                />
              </v-col>
              <v-col cols="12">
                <v-btn
                  :block="$vuetify.breakpoint.smAndDown"
                  class="font-weight-light"
                  color="primary"
                  large
                >
                  发 送 消 息
                </v-btn>
              </v-col>
            </v-row> -->
      <!-- </v-container>
        </v-form>
      </v-card-text> -->
      <v-row>
        <v-col cols="12" md="8">
          <v-row>
            <v-col
              cols="12"
              v-for="article in recommendArticles"
              :key="article.articleId"
            >
              <v-card class="mx-auto" elevation="10" hover max-width="700">
                <v-img
                  v-if="article.thumbnail"
                  :src="article.thumbnail"
                  height="300px"
                ></v-img>

                <v-card-title color="#212121">
                  {{ article.title }}
                </v-card-title>
                <v-card-subtitle>
                  <v-icon small> mdi-calendar </v-icon
                  ><span
                    class="mr-4 ml-1 font-weight-regular"
                    style="color: #212121"
                    >{{ handleDate(article.createTime) }}</span
                  >
                  <v-icon small> mdi-account </v-icon
                  ><span
                    class="mr-4 ml-1 font-weight-regular"
                    style="color: #212121"
                    >{{ article.authorNickname }}</span
                  >
                </v-card-subtitle>
                <v-card-text
                  class="font-weight-regular"
                  style="color: #212121"
                  >{{ article.summary }}</v-card-text
                >
                <v-card-subtitle class="mt-n5">
                  <v-icon class="mr-2" small> mdi-tag-multiple </v-icon>
                  <span style="color: #212121">{{
                    article.tags.join(", ")
                  }}</span>
                </v-card-subtitle>
                <v-card-actions class="mt-n5">
                  <v-btn
                    :to="`/post/${article.articleId}`"
                    text
                    color="primary"
                  >
                    开始阅读
                  </v-btn>
                  <v-spacer></v-spacer>
                  <v-btn text small class="mr-n3"
                    ><v-icon color="#616161"> mdi-heart </v-icon
                    ><span class="font-weight-regular">
                      {{ article.likeCount }}
                    </span></v-btn
                  >
                  <v-btn text small>
                    <v-icon color="#616161"> mdi-chat </v-icon
                    ><span class="font-weight-regular">{{
                      article.commentCount
                    }}</span></v-btn
                  >
                </v-card-actions>
              </v-card>
            </v-col>
          </v-row>
        </v-col>
        <v-col cols="12" md="4">
          <!-- <div class="title">推荐日记</div> -->
          <v-row type="flex" justify="end">
            <v-col cols="12" v-for="diary in diaryData" :key="diary.diaryId">
              <v-card max-width="1000" hover class="mx-auto" color="#FAFAFA">
                <v-list-item>
                  <v-list-item-avatar
                    ><img
                      :src="diary.avatar"
                      @click="handleShowUserData(diary.diaryUserId)"
                  /></v-list-item-avatar>
                  <v-list-item-content>
                    <v-list-item-title>
                      <a
                        href="javascript:void(0);"
                        @click="handleShowUserData(diary.diaryUserId)"
                        style="color: #01579b"
                        class="text-decoration-none"
                        >{{ diary.diaryUserNickname }}
                      </a>
                    </v-list-item-title>
                    <v-list-item-subtitle>{{
                      handleDate(diary.createTime)
                    }}</v-list-item-subtitle>
                  </v-list-item-content>
                  <v-spacer />
                  <v-menu bottom :offset-y="true">
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn icon v-bind="attrs" v-on="on">
                        <v-icon>mdi-dots-vertical</v-icon>
                      </v-btn>
                    </template>
                    <v-list>
                      <v-list-item @click="handleLike(diary)">
                        <v-list-item-title>
                          <v-icon v-if="diary.isLike == '0'"
                            >mdi-heart-outline</v-icon
                          >
                          <v-icon color="primary" v-if="diary.isLike == '1'"
                            >mdi-heart</v-icon
                          ></v-list-item-title
                        >
                      </v-list-item>
                      <v-list-item
                        @click="handleComment(diary, undefined, undefined, '0')"
                      >
                        <v-list-item-title
                          ><v-icon>mdi-chat-outline</v-icon></v-list-item-title
                        >
                      </v-list-item>
                      <v-list-item
                        @click="handleDeleteDiary(diary)"
                        v-if="handleShowDeleteDiaryBtn(diary)"
                      >
                        <v-list-item-title
                          ><v-icon
                            >mdi-delete-outline</v-icon
                          ></v-list-item-title
                        >
                      </v-list-item>
                    </v-list>
                  </v-menu>
                </v-list-item>
                <!-- 日记内容 -->
                <div>
                  <v-card-text class="body-2 mt-n3" style="color: #212121">
                    {{ diary.content }}
                  </v-card-text>
                </div>
                <!-- 点赞 -->
                <div class="ml-4 nt-n2 mb-2" v-if="diary.likes.length !== 0">
                  <v-icon small>mdi-heart-outline</v-icon>&nbsp;
                  <span v-for="like in diary.likes" :key="like.userId">
                    <a
                      v-if="like.status == '1'"
                      href="javascript:void(0);"
                      style="color: #01579b"
                      @click="handleShowUserData(like.userId)"
                      class="text-decoration-none body-2"
                      >{{ like.nickname
                      }}<span
                        v-if="
                          like.userId !==
                            diary.likes[diary.likes.length - 1].userId &&
                          like.status == '1'
                        "
                        >,
                      </span></a
                    ></span
                  >
                </div>
                <v-divider
                  class="ml-4"
                  v-if="diary.comments.length !== 0"
                ></v-divider>
                <div class="mt-n2" v-if="diary.comments.length !== 0">
                  <v-card-text class="body-2 mt-n2" style="color: #212121">
                    <!-- 评论 -->
                    <div
                      class="mb-1 mt-n1"
                      v-for="comment in diary.comments"
                      :key="comment.commentId"
                    >
                      <a
                        href="javascript:void(0);"
                        style="color: #01579b"
                        class="text-decoration-none"
                        @click="
                          handleComment(
                            diary,
                            comment.fromUserNickname,
                            comment.fromUserId,
                            '1'
                          )
                        "
                        v-if="comment.toUserId == 0"
                        >{{ comment.fromUserNickname }}:</a
                      >
                      <a
                        href="javascript:void(0);"
                        style="color: #01579b"
                        class="text-decoration-none"
                        @click="
                          handleComment(
                            diary,
                            comment.fromUserNickname,
                            comment.fromUserId,
                            '1'
                          )
                        "
                        v-if="comment.toUserId !== 0"
                        >{{ comment.fromUserNickname }}&nbsp;</a
                      >
                      <span v-if="comment.toUserId !== 0">回复 </span>
                      <a
                        v-if="comment.toUserId !== 0"
                        href="javascript:void(0);"
                        style="color: #01579b"
                        @click="
                          handleComment(
                            diary,
                            comment.toUserNickname,
                            comment.toUserId,
                            '1'
                          )
                        "
                        class="text-decoration-none"
                        >{{ comment.toUserNickname }}</a
                      >
                      {{ comment.content }}

                      <v-btn
                        x-small
                        icon
                        v-if="handleShowDeleteCommentBtn(comment)"
                        @click="handleDeleteComment(comment, diary)"
                      >
                        <v-icon>mdi-delete</v-icon>
                      </v-btn>
                    </div>
                  </v-card-text>
                </div>
                <v-textarea
                  v-if="diary.isReply"
                  class="ml-16 mr-5"
                  :label="diary.replyLabel"
                  outlined
                  rows="1"
                  auto-grow
                  v-model="diary.replyMessage"
                  :append-outer-icon="
                    diary.replyMessage === undefined ||
                    diary.replyMessage === ''
                      ? 'mdi-close-circle-outline'
                      : 'mdi-send'
                  "
                  @click:append-outer="sendMessageOrClose(diary)"
                  dense
                ></v-textarea>
              </v-card>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
      <person-profile />
    </base-card>
  </section>
</template>

<script>
import {
  diaryList,
  commentDiary,
  cancelLike,
  postLike,
  deleteDiary,
  deleteDiaryComment,
} from "@/api/diary";

import jwt from "jsonwebtoken";
import PersonProfile from "@/components/PersonProfile";
import moment from "moment";
import { getRecommendArticle } from "@/api/article";
import { getInfo } from "@/api/user";
import { getRecommendDiary } from "@/api/diary";
export default {
  name: "HomeContents",
  components: {
    PersonProfile,
  },

  data: () => ({
    message: "",
    items: [{ title: "评论" }, { title: "点赞" }],
    diaryData: [],
    recommendArticles: [],
    userInfo: undefined,
  }),
  mounted() {
    this.handleRecommendedDiary();
    this.handleRecommendedArticle();
    if (this.$store.state.user.token) {
      getInfo().then((res) => {
        this.userInfo = res.data;
      });
    }
  },
  methods: {
    // 处理删除评论
    handleDeleteComment(comment, diary) {
      deleteDiaryComment(comment.commentId).then((res) => {
        const comments = diary.comments;
        const newComments = [];
        comments.forEach((commenttt) => {
          if (commenttt.commentId !== comment.commentId) {
            newComments.push(commenttt);
          }
        });
        diary.comments = newComments;
      });
    },
    // 处理是否展示删除评论按钮
    handleShowDeleteCommentBtn(comment) {
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
    // 处理删除日记
    handleDeleteDiary(diary) {
      deleteDiary(diary.diaryId).then((res) => {
        this.handleDiaryList();
      });
    },
    // 处理是否展示删除日记按钮
    handleShowDeleteDiaryBtn(diary) {
      let jwt = require("jsonwebtoken");
      if (this.$store.state.user.token) {
        if (this.$store.state.user.token) {
          let token = jwt.decode(this.$store.state.user.token);
          if (token.user_id == diary.diaryUserId) {
            return true;
          }
          return false;
        }
        return false;
      }
      return false;
    },
    // 处理时间格式化
    handleDate(date) {
      return moment(date).format("YYYY-MM-DD HH:mm");
    },
    // 处理获取日记列表
    handleDiaryList() {
      diaryList(this.pageNum, this.pageSize).then((res) => {
        this.pages = res.data.pages;
        this.diaryData = res.data.list;
        this.total = res.data.total;
      });
    },
    // 负责展示用户资料
    handleShowUserData(userId) {
      this.$store.state.user.personProfileInfoUserId = userId;
      this.$store.state.user.personProfileDialog = true;
    },
    // 负责发送评论回复或者关闭输入框
    sendMessageOrClose(diary) {
      // 关闭
      if (diary.replyMessage == undefined || diary.replyMessage == "") {
        diary.isReply = false;
        this.$forceUpdate();
      }
      // 发送
      if (diary.replyMessage !== undefined && diary.replyMessage !== "") {
        commentDiary(diary.diaryId, diary.replyUserId, diary.replyMessage).then(
          (res) => {
            diary.comments.unshift(res.data);
          }
        );
        diary.replyMessage = undefined;
        this.$forceUpdate();
      }
    },
    // 处理点赞或者取消点赞
    handleLike(diary) {
      if (
        this.$store.state.user.token === undefined ||
        this.$store.state.user.token === ""
      ) {
        this.$store.state.user.loginDialog = true;
        return;
      }
      if (diary.isLike == "0") {
        // 点赞
        postLike(diary.diaryId).then((res) => {
          const like = {
            userId: res.userId,
            nickname: res.nickname,
            status: "1",
          };
          diary.likes.unshift(like);
          diary.isLike = "1";
        });
      }
      if (diary.isLike == "1") {
        // 取消点赞
        cancelLike(diary.diaryId).then((res) => {
          const newLike = [];
          diary.likes.forEach((like) => {
            if (like.userId !== res.userId) {
              newLike.push(like);
            }
          });
          diary.likes = newLike;
          diary.isLike = "0";
        });
      }
    },
    handleComment(diary, toUserNickname, toUserId, type) {
      diary.replyUserId = undefined;
      if (
        this.$store.state.user.token === undefined ||
        this.$store.state.user.token === ""
      ) {
        this.$store.state.user.loginDialog = true;
        return;
      }
      if (type == "0") {
        diary.replyLabel = "评论";
        diary.isReply = true;
      }
      if (type == "1") {
        diary.replyLabel = "回复" + toUserNickname;
        diary.replyUserId = toUserId;
        diary.isReply = true;
      }
      this.$forceUpdate();
    },
    // 获取推荐日记
    handleRecommendedDiary() {
      getRecommendDiary().then((res) => {
        this.diaryData = res.data;
      });
    },
    // 处理时间格式化
    handleDate(date) {
      return moment(date).format("YYYY-MM-DD");
    },
    // 获取推荐文章
    handleRecommendedArticle() {
      getRecommendArticle().then((res) => {
        this.recommendArticles = res.data;
        // console.log(this.recommendArticle);
      });
    },
  },
};
</script>
