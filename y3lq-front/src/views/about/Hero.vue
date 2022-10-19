<template>
  <section id="hero">
    <base-jumbotron
      :src="require('@/assets/dawdawdaw.jpeg')"
      dark
    >
      <v-row
        align="center"
        justify="center"
      >
        <base-title>
          关于
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
