import Vue from 'vue'
import Vuex from 'vuex'

// Modules
import app from '@/store/modules/app'
import content from '@/store/modules/content'
import user from '@/store/modules/user'
import snackbar from "@/store/modules/snackbar";
import getters from './getters'
import profile from "@/store/modules/profile";

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    app,
    content,
    user,
    snackbar,
    getters,
    profile
  },
})
