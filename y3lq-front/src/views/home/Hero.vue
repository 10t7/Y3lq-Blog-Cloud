<template>
  <section id="hero">
    <base-jumbotron
      :src="require('@/assets/bg.jpg')"
      color="grey darken-4"
      dark
      height="100%"
      min-height="100vh"
    >
      <v-row
        align="center"
        class="pb-12"
      >
        <v-col
          cols="12"
          md="5"
        >
          <div>
            <base-title>
              Less is more
            </base-title>

            <v-divider class="mb-3" />

            <div class="subheading mb-4">
              <!-- <p>Best Material Design framework on the web</p> -->

              <p>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
              </p>
            </div>

            <v-btn
              color="primary"
              class="ma-0"
              large
            >
              Get Started Today
              <v-icon right>
                mdi-school
              </v-icon>
            </v-btn>
          </div>
        </v-col>
      </v-row>
    </base-jumbotron>
  </section>
</template>

<script>
  export default {
    name: 'HomeHero',
  }
</script>
