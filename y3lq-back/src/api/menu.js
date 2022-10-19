import request from '@/utils/request'


// 获取下拉树结构
export function treeselect() {
    return request({
        url: '/system/menu/tree-select',
        method: 'get'
    })
}


// 获取菜单列表
export function menuList({name,status}) {
    return request({
        url: '/system/menu/list',
        method: 'get',
        params: {
            name: name,
            status: status
        }
    })
}

// 新增菜单
export function addMenu(data) {
    return request({
        url: '/system/menu',
        method: 'post',
        data: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 更新菜单
export function updateMenu(data) {
    return request({
        url: '/system/menu',
        method: 'put',
        data: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 获取单个菜单信息
export function getMenuInfo(menuId) {
    return request({
        url: `/system/menu/info/${menuId}`,
        method: 'get',
    })
}

// 删除菜单
export function deleteMenu(menuId) {
    return request({
        url: `/system/menu/${menuId}`,
        method: 'delete',
    })
}