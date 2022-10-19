<template>
  <section id="content">
    <base-card>
      <v-form>
        <v-row>
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
                        ><v-icon>mdi-delete-outline</v-icon></v-list-item-title
                      >
                    </v-list-item>
                  </v-list>
                </v-menu>
              </v-list-item>
              <!-- 日记内容 -->
              <div class="ml-14">
                <v-card-text class="body-2 mt-n3" style="color: #212121">
                  {{ diary.content }}
                </v-card-text>
              </div>
              <!-- 点赞 -->
              <div class="ml-16 nt-n2 mb-2" v-if="diary.likes.length !== 0">
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
                class="ml-16"
                v-if="diary.comments.length !== 0"
              ></v-divider>
              <div class="ml-14 mt-n2" v-if="diary.comments.length !== 0">
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
                  diary.replyMessage === undefined || diary.replyMessage === ''
                    ? 'mdi-close-circle-outline'
                    : 'mdi-send'
                "
                @click:append-outer="sendMessageOrClose(diary)"
                dense
              ></v-textarea>
            </v-card>
          </v-col>
          <v-col cols="12" v-if="this.total > 9">
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
import moment from "moment";
import jwt from "jsonwebtoken";
import PersonProfile from "@/components/PersonProfile";

export default {
  name: "DiaryContents",
  components: {
    PersonProfile,
  },
  data() {
    return {
      total: 0,
      pageNum: 1,
      pageSize: 9,
      pages: 0,
      message: "",
      items: [{ title: "评论" }, { title: "点赞" }],
      diaryDatas: [
        {
          diaryId: 1,
          diaryUserId: 1,
          diaryNickname: "Y3lq",
          diaryUserAvatar:
            "https://pic2.zhimg.com/80/v2-856c0608949c6098ae82be1c8fcceb25_1440w.jpg",
          content:
            "当一件作品的内容被减至最低限度时所散发的完美感觉罚款解放巴黎警方霸王龙",

          createTime: "2020-8-10 14:23",
          likes: [
            {
              userId: 2,
              nickname: "10t7",
            },
            {
              userId: 1,
              nickname: "Y3lq",
            },
          ],
          comments: [
            {
              commentId: 1,
              diaryId: 1,
              toNickname: "",
              toUserId: 1,
              fromUserId: 2,
              fromNickname: "10t7",
              content:
                "所有细节以及所有的连接都被减少压缩至精华时，他就会拥有这种特性",
            },
            {
              commentId: 2,
              diaryId: 1,
              toNickname: "10t7",
              toUserId: 2,
              fromUserId: 1,
              fromNickname: "Y3lq",
              content: "所有细节以及所有的连接大无法无法都被减少压缩至精华时",
            },
          ],
        },
        {
          diaryId: 2,
          diaryUserId: 1,
          diaryNickname: "Y3lq",
          diaryUserAvatar:
            "https://pic2.zhimg.com/80/v2-856c0608949c6098ae82be1c8fcceb25_1440w.jpg",
          content:
            "当一件作品的内容被减至最低限度时所散发的完美感觉罚款解放巴黎警方霸王龙",

          createTime: "2020-8-10 14:23",
          likes: [
            {
              userId: 2,
              nickname: "10t7",
            },
            {
              userId: 1,
              nickname: "Y3lq",
            },
          ],
          comments: [
            {
              commentId: 3,
              diaryId: 2,
              toNickname: "",
              // toUserId: 1,
              fromUserId: 2,
              fromNickname: "10t7",
              content:
                "所有细节以及所有的连接都被减少压缩至精华时，他就会拥有这种特性",
            },
            {
              commentId: 4,
              diaryId: 2,
              toNickname: "10t7",
              toUserId: 2,
              fromUserId: 1,
              fromNickname: "Y3lq",
              content: "所有细节以及所有的连接大无法无法都被减少压缩至精华时",
            },
          ],
        },
        {
          diaryId: 3,
          diaryUserId: 1,
          diaryNickname: "Y3lq",
          diaryUserAvatar:
            "https://pic2.zhimg.com/80/v2-856c0608949c6098ae82be1c8fcceb25_1440w.jpg",
          content:
            "当一件作品的内容被减至最低限度时所散发的完美感觉罚款解放巴黎警方霸王龙",

          createTime: "2020-8-10 14:23",
          likes: [
            {
              userId: 2,
              nickname: "10t7",
            },
            {
              userId: 1,
              nickname: "Y3lq",
            },
          ],
          comments: [
            {
              commentId: 5,
              diaryId: 3,
              toNickname: "",
              // toUserId: 1,
              fromUserId: 2,
              fromNickname: "10t7",
              content:
                "所有细节以及所有的连接都被减少压缩至精华时，他就会拥有这种特性",
            },
            {
              commentId: 6,
              diaryId: 3,
              toNickname: "10t7",
              toUserId: 2,
              fromUserId: 1,
              fromNickname: "Y3lq",
              content: "所有细节以及所有的连接大无法无法都被减少压缩至精华时",
            },
          ],
        },
      ],
      diaryData: [],
    };
  },

  mounted() {
    this.handleDiaryList();
  },
  watch: {
    total() {
      if (this.total % 10 === 0) {
        this.handleDiaryList();
      }
    },
    pageNum() {
      diaryList(this.pageNum, this.pageSize).then((res) => {
        this.pages = res.data.pages;
        this.diaryData = res.data.list;
      });
    },
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
      this.$store.dispatch("snackbar/openSnackbar", {
        msg: "删除评论成功",
        color: "success",
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
        this.$store.dispatch("snackbar/openSnackbar", {
          msg: "删除日记成功",
          color: "success",
        });
      });
    },
    // 处理是否展示删除日记按钮
    handleShowDeleteDiaryBtn(diary) {
      let jwt = require("jsonwebtoken");
      if (this.$store.state.user.token) {
        let token = jwt.decode(this.$store.state.user.token);
        if (token.user_id == diary.diaryUserId) {
          return true;
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
      // console.log(this.$store.state.user.personProfileInfoUserId)
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
  },
};
</script>
