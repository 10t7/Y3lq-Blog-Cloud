<template>
  <section id="article">
    <hero />

    <contents />

    <floating-button />
  </section>
</template>

<script>
export default {
  name: "ArticleIndex",

  metaInfo: {
    title: "Article",
  },

  components: {
    Hero: () => import("./Hero"),
    Contents: () => import("./Contents"),
    FloatingButton: () => import("@/components/FloatingButton"),
  },
};
</script>
