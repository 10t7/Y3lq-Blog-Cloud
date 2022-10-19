import { set, toggle } from '@/utils/vuex'

export default {
  namespaced: true,

  state: {
    links: [
      ['/', '主页'],
      // ['/blog', 'Blog'],
      // ['/sample-page', 'Sample Page'],
      ['/article', '文章'],
      ['/diary', '日记'],
      ['/about', '关于'],
    ],
    drawer: null,
    writeDialog: false,
    // tagDialog:false,
    // tagTreeList:[],

  },


  mutations: {
    CLOSE_WRITEDIALOG(state,newValue){
      state.writeDialog = newValue
    },
    setDrawer: set('drawer'),
    toggleDrawer: toggle('drawer'),
  },
}
