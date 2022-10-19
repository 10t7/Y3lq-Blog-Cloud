<template>
  <section id="about">
    <hero />

    <contents />

    <floating-button/>
  </section>
</template>

<script>
export default {
  name: "AboutIndex",

  metaInfo: {
    title: "About",
  },

  components: {
    Hero: () => import("./Hero"),
    Contents: () => import("./Contents"),
    FloatingButton: () => import("@/components/FloatingButton")
  },
};
</script>
