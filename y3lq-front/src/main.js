import Vue from 'vue'
import './plugins/base'
import './plugins/meta'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'

Vue.config.productionTip = false

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App),
  beforeCreate() {
    Vue.prototype.$bus = this//安装全局事件总线
  }
}).$mount('#app')
