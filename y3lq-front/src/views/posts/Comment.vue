<template>
  <v-form id="comment">
    <v-container class="px-0">
      <h3 class="subheading font-weight-bold mt-10 mb-4" style="color: #424242">
        <v-icon>mdi-square-edit-outline</v-icon>评论
      </h3>
      <!-- <div class="caption mb-4">留下邮箱可及时收到回复哦</div> -->
      <!-- <v-row>
        <v-col
          cols="12"
          md="6"
        >
          <v-text-field
            label="昵称*"
            outlined
            required
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
          <v-text-field
            label="网址"
            outlined
          />
        </v-col>

        <v-col cols="12">
          <v-textarea
            label="内容"
            outlined
            rows="7"
            counter
          />
        </v-col>

        <v-col>
          <v-btn
            :block="$vuetify.breakpoint.smAndDown"
            class="font-weight-light"
            color="accent"
          >
            提 交
          </v-btn>
        </v-col>
      </v-row> -->

      <comment-contents />
    </v-container>
  </v-form>
</template>

<script>
import { commentArticle } from "@/api/article";
export default {
  name: "PostsComment",
  data() {
    return {
      comment: {
        content: "",
        articleId: undefined,
      },
    };
  },
  components: {
    CommentContents: () => import("@/views/posts/CommentContents"),
  },
  mounted() {
    this.resetComment();
  },
  methods: {
    resetComment() {
      this.comment = {
        content: "",
        articleId: undefined,
      };
    },
    handleSubmitComment() {
      this.comment.articleId = this.$route.params.articleId;
      commentArticle(comment);
      this.resetComment();
    },
  },
};
</script>
