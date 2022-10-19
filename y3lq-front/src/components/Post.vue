<template>
  <v-sheet :class="classes">
    <v-container class="pa-0">
      <v-row class="mb-4">
        <v-col>
          <div class="mr-3">
            <v-icon small> mdi-calendar </v-icon>
            {{ handleDate(value.createTime) }}
          </div>
          <div class="mr-3">
            <v-icon small> mdi-account </v-icon>
            {{ value.authorNickname }}
          </div>
          <div>
            <v-icon small> mdi-chat </v-icon>
            {{ value.commentCount }}
          </div>
          <div>
            <v-icon small> mdi-heart </v-icon>
            {{ value.likeCount }}
          </div>
        </v-col>
      </v-row>
      <!-- <div class="subheading mb-5 post__body" v-html="value.summary" /> -->
      <div class="subheading mb-5 post__body" v-html="value.content" />

      <v-row v-if="value.tags && value.tags.length > 0" class="mb-4">
        <v-col>
          <v-icon class="mr-2"> mdi-tag-multiple </v-icon>
          {{ value.tags.join(", ") }}
        </v-col>
      </v-row>
      <v-row>
        <v-col class="text-center"
          ><v-btn x-large fab text @click="handleLikeOrCancelLike">
            <v-icon v-if="this.value.isLike == '0'">mdi-heart-outline</v-icon>
            <v-icon v-if="this.value.isLike == '1'" color="primary"
              >mdi-heart</v-icon
            >
          </v-btn></v-col
        >
      </v-row>
    </v-container>
  </v-sheet>
</template>

<script>
import moment from "moment";
import {
  getArticleById,
  postLikeArticle,
  cancelLikeArticle,
} from "@/api/article";
export default {
  name: "Post",

  data() {
    return {
      showContent: false,
      value: undefined,
    };
  },
  methods: {
    // 处理点赞或者取消点赞
    handleLikeOrCancelLike() {
            if (
        this.$store.state.user.token === undefined ||
        this.$store.state.user.token === ""
      ) {
        this.$store.state.user.loginDialog = true;
        return;
      }
      if (this.value.isLike === "0") {
        postLikeArticle(this.value.articleId).then((res) => {
          this.value.likeCount = this.value.likeCount + 1;
          this.value.isLike = "1";
        });
      }
      if (this.value.isLike === "1") {
        cancelLikeArticle(this.value.articleId).then((res) => {
          this.value.likeCount = this.value.likeCount - 1;
          this.value.isLike = "0";
        });
      }
    },
    // 查看是否点赞显示不同的爱心
    handleArticleLike() {
      console.log(this.value);
      if (this.value.isLike == "0") {
        return true;
      } else {
        return false;
      }
    },
    handleDate(date) {
      return moment(date).format("YYYY-MM-DD");
    },
  },
  mounted() {
    getArticleById(this.$route.params.articleId).then((res) => {
      this.value = res.data;
      // console.log(this.value)
      this.$bus.$emit("getBackgroundImage", res.data);
    });
  },
  props: {
    dense: {
      type: Boolean,
      default: false,
    },
    // value: {
    //   type: Object,
    //   default: () => ({}),
    // },
  },

  computed: {
    classes() {
      return {
        "post--dense": this.dense,
      };
    },
  },
};
</script>

<style>
.post--dense .post__body > *:not(:first-child) {
  display: none;
}
</style>
