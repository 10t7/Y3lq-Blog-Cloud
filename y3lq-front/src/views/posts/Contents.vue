<template>
  <section id="content">
    <base-card>
      <v-container class="py-0">
        <post  />

        <comment />
      </v-container>
    </base-card>
  </section>
</template>

<script>
  // Utilities
  import {
    mapState,
  } from 'vuex'

  export default {
    name: 'PostContents',

    components: {
      Comment: () => import('./Comment'),
      Post: () => import('@/components/Post'),
    },

    // computed: {
    //   ...mapState('content', ['posts']),
    //   post () {

    //     for (let index = 0; index < this.posts.length; index++) {

    //         if(this.posts[index].articleId ==this.$route.params.articleId){

              
    //           return this.posts[index]
    //         }
          
    //     }
    //   },
    // },
  }
</script>
