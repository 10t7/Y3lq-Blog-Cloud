<template>
  <section id="hero">
    <base-jumbotron
      :src="require('@/assets/bg22.jpg')"
      dark
    >
      <v-row
        align="center"
        justify="center"
      >
        <base-title>
          This is our Blog
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
