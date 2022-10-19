import request from '@/utils/request'

export function login(username, password, uuid, code) {
  return request({
    url: '/auth/login',
    method: 'post',
    data: { username, password, uuid, code },
  })
}

export function getInfo(token) {
  return request({
    url: '/system/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取用户的菜单
export async function getMenuList() {
  return await request({
    url: '/system/menu/get-routers',
    method: 'get'
  })
}

// 分页获取用户列表
export function getUserList({ pageNum, pageSize, phone, username, nickname }) {
  return request({
    url: '/system/user/list',
    method: 'get',
    params: {
      pageNum: pageNum,
      pageSize: pageSize,
      phone: phone,
      username: username,
      nickname: nickname
    }
  })
}

// 修改用户状态
export function changeUserStatus(userId, status) {
  return request({
    url: '/system/user/change-status',
    method: 'put',
    params: {
      userId: userId,
      status: status
    }
  })
}


// 获取用户上传头像的签名
export function getSignature() {
  return request({
    url: '/thirdparty/oss/signature',
    method: 'get'
  })
}

// 新增用户
export function insertUser(userData) {
  return request({
    url: '/system/user',
    method: 'post',
    data: JSON.stringify(userData),
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 修改用户信息
export function updateUser(userData) {
  return request({
    url: '/system/user',
    method: 'put',
    data: JSON.stringify(userData),
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 拿到对话框中需要的 role 数据
export function getDialogRoleData() {
  return request({
    url: '/system/user/role-admin-can-set',
    method: 'get',
  })
}

// 删除用户
export function deleteUser(userIds) {
  return request({
    url: `/system/user/${userIds}`,
    method: 'delete',
  })
}

// 获取指定用户信息
export function getUserInfo(userId) {
  return request({
    url: `/system/user/infoByUserId/${userId}`,
    method: 'get',
  })
}




// 重置密码
export function resetUserPwd(data) {
  return request({
    url: "/system/user/reset-pwd",
    method: 'put',
    data: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 获取验证码图片
export function getCaptchaImage() {
  return request({
    url: "/auth/captcha",
    method: 'get',
  })
}
