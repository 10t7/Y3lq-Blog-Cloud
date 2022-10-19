<template>
  <section id="posts">
    <hero />

    <contents />

    <floating-button/>
  </section>
</template>

<script>

  export default {
    name: 'PostsIndex',

    metaInfo: {
      title: 'Posts',
    },

    components: {
      FloatingButton: () => import("@/components/FloatingButton"),
      Contents: () => import('./Contents'),
      Hero: () => import('./Hero')

    },
  }
</script>
