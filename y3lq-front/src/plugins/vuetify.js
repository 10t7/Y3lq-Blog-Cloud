import Vue from 'vue'
import Vuetify from 'vuetify/lib'
import '@mdi/font/css/materialdesignicons.css'
import colors from 'vuetify/lib/util/colors'

Vue.use(Vuetify)

export default new Vuetify({
  // icons: {
  //   iconfont: "mdi" // 'mdi' || 'mdiSvg' || 'md' || 'fa' || 'fa4' || 'faSvg'
  // },
  theme: {
    themes: {
      light: {
        primary: '#F44336',
        secondary: '#2B2D42',
        accent: '#8D99AE',
        // primary: '#3f51b5',
        // secondary: '#b0bec5',
        // accent: '#8c9eff',
        // error: '#b71c1c',
        // primary: colors.purple,
        // secondary: colors.grey.darken1,
        // accent: colors.shades.black,
        // error: colors.red.accent3,
      },
      dark: {
        primary: colors.blue.lighten3,
      },
    },
  },
})
