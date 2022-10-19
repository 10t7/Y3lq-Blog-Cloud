import request from '@/utils/request'


// 获取标签名字列表
export function tagNameList(name) {
    return request({
        url: '/system/tag/tag-name-list',
        method: 'get',
        params: {
            name: name,
        }
    })
}

// 获取标签列表
export function tagList({ name, type, status }) {
    return request({
        url: '/system/tag/list',
        method: 'get',
        params: {
            name: name,
            type: type,
            status: status,
        }
    })
}


// 修改标签状态
export function changeTagStatus(tagId, status) {
    return request({
        url: '/system/tag/chenge-tag-status',
        method: 'put',
        params: {
            tagId: tagId,
            status: status
        }
    })
}

// 删除标签
export function deleteTag(tagId) {
    return request({
        url: `/system/tag/${tagId}`,
        method: 'delete',
    })
}

// 新增标签
export function addTag(data) {
    return request({
        url: '/system/tag',
        method: 'post',
        data: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 更新标签
export function updateTag(data) {
    return request({
        url: '/system/tag',
        method: 'put',
        data: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

// 获取所有标签类别
export function tagCategoryList() {
    return request({
        url: '/system/tag/tag-category-list',
        method: 'get',
    })
}

// 根据tagId获取标签信息
export function tagInfo(tagId) {
    return request({
        url: `/system/tag/info/${tagId}`,
        method: 'get',
    })
}