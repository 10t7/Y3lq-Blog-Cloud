<template>
  <!-- <v-footer
    absolute
    
    color="transparent"
    height="auto"
    max-width="95%"
  > -->
  <!-- <v-col class="text-center mt-10" cols="12">
      <div class="mt-10">© 2022 10t7.tech All Rights Reserved.</div>
    </v-col> -->
  <!-- <v-col class="text-center" cols="12"> 粤ICP备2021165301 </v-col> -->
  <!-- <v-row justify="center">
      <v-btn
        v-for="([link, text], i) in links"
        :key="`link-${i}`"
        :to="link"
        class="mx-auto hidden-sm-and-down"
        small
        text
      >
        {{ text }}
      </v-btn>

      <v-spacer />

      <v-col
        class="hidden-md-and-up"
        cols="12"
      />

      <v-btn
        v-for="([href, text], i) in socials"
        :key="`social-${i}`"
        :href="href"
        class="px-0 mx-2"
        color="grey darken-1"
        dark
        min-width="48"
        rel="noopener"
        target="_blank"
      >
        <v-icon
          size="18"
          v-text="text"
        />
      </v-btn>
    </v-row> -->
  <!-- </v-footer> -->
  <v-footer  padless>
    <v-col class="text-center" cols="12">
      <div>© 2022 10t7.tech All Rights Reserved</div>
      粤ICP备2021165301
    </v-col>
  </v-footer>
</template>

<script>
// Utilities
import { mapState } from "vuex";

export default {
  name: "CoreFooter",

  data: () => ({
    socials: [
      ["https://facebook.com", "mdi-email"],
      ["https://twitter.com", "mdi-message-text"],
    ],
  }),

  computed: {
    ...mapState("app", ["links"]),
    categories() {
      return [];
    },
  },
};
</script>
