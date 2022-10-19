import request from '@/utils/request'

// 用户登录
export function login({ username, password, type }) {
    return request({
        url: '/auth/login',
        method: 'post',
        data: { username, password, type },

    })
}

// 获取用户信息
export function getInfo(token) {
    return request({
        url: '/system/user/info',
        method: 'get',
        params: { token }
    })
}

// 用户退出
export function logout() {
    return request({
        url: '/auth/logout',
        method: 'post'
    })
}

// 用户注册
export function register(userInfo) {
    return request({
        url: '/system/user/registered',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(userInfo)
    })
}

// 获取指定用户个人基础信息
export function blogUserInfo(userId) {
    return request({
        url: `/system/user/blog-user-info`,
        method: 'get',
        params: {
            userId: userId
        }
    })
}


// 改变用户个人设置
export function changeUserSetting(replyReceiveEmail, newReceiveEmail, showPersonInfo) {
    return request({
        url: '/system/user/change-user-setting',
        method: 'put',
        params: { replyReceiveEmail, newReceiveEmail, showPersonInfo },
    })
}