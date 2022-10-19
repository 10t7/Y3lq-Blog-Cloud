import request from '@/utils/request'

export function getRoutes() {
  return request({
    url: '/vue-element-admin/routes',
    method: 'get'
  })
}

export function getRoles() {
  return request({
    url: '/vue-element-admin/roles',
    method: 'get'
  })
}

// 批量取消用户某个角色
export function AuthRoleToUser(roleId, userIds) {
  return request({
    url: `/system/role/auth-role-user/${userIds}`,
    method: 'put',
    params: {
      roleId: roleId,
    }
  })
}

// 批量取消用户某个角色
export function cancelAuthUser(roleId, userIds) {
  return request({
    url: `/system/role/cancel-auth-user/${userIds}`,
    method: 'put',
    params: {
      roleId: roleId,
    }
  })
}

// 获取为未分配角色的列表
export function getUnAssignUserList({ pageNum, pageSize, username, phone, roleIds }) {
  return request({
    url: "/system/role/unassigned-user-list",
    method: 'get',
    params: {
      pageNum: pageNum,
      pageSize: pageSize,
      username: username,
      phone: phone,
      roleIds: roleIds
    }
  })
}

// 获取为已经分配角色的列表
export function getAssignUserList({ pageNum, pageSize, username, phone, roleIds }) {
  return request({
    url: "/system/role/assign-user-list",
    method: 'get',
    params: {
      pageNum: pageNum,
      pageSize: pageSize,
      username: username,
      phone: phone,
      roleIds: roleIds
    }
  })
}

// 修改角色状态
export function changeRoleStatus(roleId, status) {
  return request({
    url: '/system/role/change-status',
    method: 'put',
    params: {
      roleId: roleId,
      status: status
    }
  })
}

// 删除角色
export function deleteRoles(roleIds) {
  return request({
    url: `/system/role/${roleIds}`,
    method: 'delete',
  })
}

// 获取角色信息
export function getRole(roleId) {
  return request({
    url: `/system/role/info/${roleId}`,
    method: 'get',
  })
}

// 更新角色
export function toUpdateRole(roleData) {
  return request({
    url: '/system/role',
    method: 'put',
    data: JSON.stringify(roleData),
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 新增角色
export function insertRole(roleData) {
  return request({
    url: '/system/role',
    method: 'post',
    data: JSON.stringify(roleData),
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 获取角色列表
export function getRoleList({ name, roleKey, pageNum, pageSize }) {
  return request({
    url: '/system/role/list',
    method: 'get',
    params: {
      name: name,
      roleKey: roleKey,
      pageNum: pageNum,
      pageSize: pageSize,
    }
  })
}

export function addRole(data) {
  return request({
    url: '/vue-element-admin/role',
    method: 'post',
    data
  })
}

export function updateRole(id, data) {
  return request({
    url: `/vue-element-admin/role/${id}`,
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/vue-element-admin/role/${id}`,
    method: 'delete'
  })
}
