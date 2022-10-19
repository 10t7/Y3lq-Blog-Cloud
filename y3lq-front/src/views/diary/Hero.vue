<template>
  <section id="hero">
    <base-jumbotron
      :src="require('@/assets/default-diary.jpg')"
      dark
    >
      <v-row
        align="center"
        justify="center"
      >
        <base-title>
          日记
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
