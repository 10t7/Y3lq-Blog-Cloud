<template>
  <section id="diary">
    <hero />

    <contents />

    <floating-button />
  </section>
</template>

<script>
export default {
  name: "DiaryIndex",

  metaInfo: {
    title: "Diary",
  },

  components: {
    Hero: () => import("./Hero"),
    Contents: () => import("./Contents"),
    FloatingButton: () => import("@/components/FloatingButton"),
  },
};
</script>
