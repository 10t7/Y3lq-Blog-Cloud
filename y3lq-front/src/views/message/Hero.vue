<template>
  <section id="hero">
    <base-jumbotron
      :src="require('@/assets/dafault-about.jpg')"
      dark
    >
      <v-row
        align="center"
        justify="center"
      >
        <base-title>
          留言
        </base-title>
      </v-row>
    </base-jumbotron>
  </section>
</template>

<script>
  export default {
    name: 'BlogHero',
  }
</script>
