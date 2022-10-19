<template>
  <section id="message">
    <hero />

    <contents />

    <floating-button/>
  </section>
</template>

<script>

export default {
  name: "MessageIndex",

  metaInfo: {
    title: "Message",
  },

  components: {
    Hero: () => import("./Hero"),
    Contents: () => import("./Contents"),
    FloatingButton: () => import("@/components/FloatingButton")
  },
};
</script>
