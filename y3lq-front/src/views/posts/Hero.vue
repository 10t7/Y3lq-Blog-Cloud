<template>
  <section id="hero">
    <base-jumbotron :src="this.images" dark>
      <v-row align="center" justify="center">
        <base-title>{{ title }} </base-title>
      </v-row>
    </base-jumbotron>
  </section>
</template>

<script>
export default {
  name: "PostsHero",

  data() {
    return {
      images:
        "https://oss-cdn.yevpt.com/post/bg-images/202008/01ab6a6a2e31ae3ca406aba7064c1f56.jpg?a=34b475f0246d2dfb3ea64957a8f7f16f&b=62ac2350",
      title: "",
    };
  },

  mounted() {
    // 绑定事件，拿到背景图片，标题
    this.$bus.$on("getBackgroundImage", this.getBackgroundImage);
  },
  beforeDestroy() {
    this.$bus.$off("getBackgroundImage");
  },

  methods: {
    getBackgroundImage(article) {
      this.images = article.thumbnail;
      this.title = article.title;
    },
  },
};
</script>
