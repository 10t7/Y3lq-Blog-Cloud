<template>
  <section id="posts">
    <base-card >
      <v-row type="flex" >
        <!-- 标签 -->

        <!-- 文章 -->
        <v-col cols="12" md="12" v-for="post in posts" :key="post.aritcleId" >
          <v-card class="mx-auto" elevation="10" hover max-width="700">
            <v-img
              v-if="post.thumbnail"
              :src="post.thumbnail"
              height="300px"
            ></v-img>

            <v-card-title color="#212121">
              {{ post.title }}
            </v-card-title>

            <v-card-subtitle>
              <v-icon small> mdi-calendar </v-icon
              ><span
                class="mr-4 ml-1 font-weight-regular"
                style="color: #212121"
                >{{ handleDate(post.createTime) }}</span
              >
              <v-icon small> mdi-account </v-icon
              ><span
                class="mr-4 ml-1 font-weight-regular"
                style="color: #212121"
                >{{ post.authorNickname }}</span
              >
            </v-card-subtitle>

            <v-card-text class="font-weight-regular" style="color: #212121">{{
              post.summary
            }}</v-card-text>
            <v-card-subtitle class="mt-n5">
              <!-- <v-icon small> mdi-tag-multiple </v-icon>
              <span style="color: #212121">{{ handleShowTag(post.tags) }}</span> -->
              <v-icon class="mr-2" small> mdi-tag-multiple </v-icon>

              <span style="color: #212121">{{ post.tags.join(", ") }}</span>
            </v-card-subtitle>
            <v-card-actions class="mt-n5">
              <!-- <v-btn text color="primary">开始阅读</v-btn>
               -->
              <v-btn :to="`/post/${post.articleId}`" text color="primary">
                开始阅读
              </v-btn>
              <v-spacer></v-spacer>
              <!-- <v-btn text small class="mr-n3"> -->
                <v-icon v-if="post.isLike=='0'" color="#616161"> mdi-heart </v-icon>
                <!-- <v-icon v-if="post.isLike=='0'" color="#616161"> mdi-heart </v-icon> -->
                <v-icon v-if="post.isLike=='1'" color="primary"> mdi-heart </v-icon>
                <span class="font-weight-regular mr-2">{{
                  post.likeCount
                }}</span>
                <!-- </v-btn
              > -->

              <!-- <v-btn text small> -->
                <v-icon color="#616161">
                  mdi-chat
                </v-icon>
    

                <span class="font-weight-regular mr-2">{{
                  post.commentCount
                }}</span>
                <!-- </v-btn
              > -->
            </v-card-actions>
          </v-card>
        </v-col>

        <v-col cols="12" md="12" >
          <div class="text-center">
            <v-pagination
              v-model="pageNum"
              :length="pages"
              :total-visible="6"
            ></v-pagination>
          </div>
        </v-col>
      </v-row>


    </base-card>
  </section>
</template>

<script>
// Utilities
import { mapState } from "vuex";
import { articleList } from "@/api/article";
import moment from "moment";

export default {
  name: "ArticleContents",

  data() {
    return {
      pageNum: 1,
      pageSize: 5,
      pages: 0,

      articleList: [],
    };
  },
  mounted() {
    this.handleArticleList();
  },

  methods: {
    // handlePageChange() {
    //   articleList(this.pageNum, this.pageSize).then((res) => {
    //     this.$store.commit("content/SAVE_ARTICLELIST", res.data.list);
    //     this.pages = res.data.pages;
    //     // console.log(this.articleList);
    //   });
    // },
    // 处理时间格式化
    handleDate(date) {
      return moment(date).format("YYYY-MM-DD");
    },
    // 获取文章列表
    handleArticleList() {
      articleList(this.pageNum, this.pageSize).then((res) => {
        this.$store.commit("content/SAVE_ARTICLELIST", res.data.list);
        // this.$forceUpdate();
        this.pages = res.data.pages;
        // console.log(this.articleList);
      });
    },
  },

  components: {
    Post: () => import("@/components/Post"),
  },
  watch: {
    pageNum() {
      articleList(this.pageNum, this.pageSize).then((res) => {
        this.$store.commit("content/SAVE_ARTICLELIST", res.data.list);
        this.pages = res.data.pages;
      });
    },
  },

  computed: {
    ...mapState("content", ["posts"]),
  },
};
</script>
