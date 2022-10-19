<template>
  <section id="home">
    <hero />

    <contents />

    <floating-button />
  </section>
</template>

<script>
export default {
  name: "HomeIndex",

  metaInfo: {
    title: "Home",
  },

  components: {
    Hero: () => import("./Hero"),
    Contents: () => import("./Contents"),
    FloatingButton: () => import("@/components/FloatingButton"),
  },
};
</script>
