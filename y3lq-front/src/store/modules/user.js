import { getToken, setToken, removeToken } from '@/utils/auth'
import { login, logout, getInfo } from '@/api/user'
import store from "@/store";

// 用于存储数据
const state = {
    userInfo: undefined,
    token: getToken(),
    newNum: 1,
    newNumVisible: true,
    loginDialog: false,
    personProfileDialog: false,
    personProfileInfo:undefined,
    personProfileInfoUserId:undefined,
    userInfo:{
        username:"",
        nickname:"",
        email:"",
        wechat:"",
        avatar:"https://pic1.zhimg.com/v2-4e36b3511ae845536ee47c5b426b58f3_r.jpg?source=1940ef5c",
    }
}

// 用于操作数据
const mutations = {
    CLOSE_LOGINDIALOG: (state, newValue) => {
        state.loginDialog = newValue
    },
    SET_TOKEN: (state, token) => {
        state.token = token
    },
    SET_USERINFO: (state, userInfo) => {
        state.userInfo = userInfo
    },
    CLOSE_PERSONPROFILEDIALOG: (state, newValue) => {
        state.personProfileDialog = newValue
    }
}
// 用于响应组件动作
const actions = {
    // 用户登录
    login({ commit }, userInfo) {
        // const { username, password, uuid, code } = userInfo
        const type = "blog-login"
        userInfo.type = type
        return new Promise((resolve, reject) => {
            login(userInfo).then(res => {
                const { data } = res
                commit('SET_TOKEN', data)
                setToken(data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    // 获取用户信息
    getInfo({ commit, state }) {
        return new Promise((resolve, reject) => {
            getInfo().then(res => {
                const { data } = res
                if (!data) {
                    reject('登录失败请重新登录')
                }
                // console.log(data)
                store.dispatch("profile/openProfile", data)
                // const { roles, username, avatar } = data
                // roles must be a non-empty array
                // if (!roles || roles.length <= 0) {
                //     reject('getInfo: roles must be a non-null array!')
                // }
                // sessionStorage.setItem("perms", JSON.stringify(perms))
                commit('SET_USERINFO', data)
                resolve(data)
            }).catch(error => {
                reject(error)
            })
        })
    },

    // 用户退出
    logout({ commit, state, dispatch }) {
        return new Promise((resolve, reject) => {
            logout(state.token).then(() => {
                commit('SET_TOKEN', '')
                // commit('SET_ROLES', [])
                removeToken()
                // resetRouter()

                // reset visited views and cached views
                // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
                // dispatch('tagsView/delAllViews', null, { root: true })

                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },
    // 移除token
    resetToken({ commit }) {
        return new Promise(resolve => {
            commit('SET_TOKEN', '')
            // commit('SET_ROLES', [])
            removeToken()
            resolve()
        })
    },



}
// 创建暴露
export default {
    namespaced: true,
    state,
    mutations,
    actions
}

// const getters={
//     gerAvatar(state){
//         return state.userInfo.avatar
//     }
// }