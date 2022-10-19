<template>
  <section id="hero">
    <base-jumbotron
      :src="require('@/assets/default-article.jpg')"
      dark
    >
      <v-row
        align="center"
        justify="center"
      >
        <base-title>
          文章
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
