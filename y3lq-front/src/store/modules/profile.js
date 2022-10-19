const profile = {
    // 
    namespaced: true,
    state: {
        username: '',
        nickname: '',
        wechat: '',
        email: '',
        website: '',
        dialog: false,
        avatar:'',


    },
    // 逻辑处理,同步函数
    mutations: {
        OPEN_PROFILE(state) {
            state.dialog = true
        },
        SET_USERINFO(state, userInfo) {
            state.nickname = userInfo.nickname
            state.username = userInfo.username
            state.wechat = userInfo.wechat
            state.email = userInfo.email
            // state.website = userInfo.website
            state.avatar = userInfo.avatar

            
        },
        CLOSE_PROFILE(state,value) {
            state.dialog = value
        },
    },
    // 逻辑处理,异步函数
    actions: {
        openProfile(context, userInfo) {
            // console.log(userInfo)
            // let timeout = context.state.timeout
            context.commit('SET_USERINFO', userInfo)
            context.commit('OPEN_PROFILE')
            // setTimeout(()=>{
            //   context.commit('CLOSE_PROFILE')
            // },timeout)
        },

        open(context) {
            context.commit('OPEN_PROFILE')
        }
    }
}
export default profile;