import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/vue-element-admin/article/list',
    method: 'get',
    params: query
  })
}

export function fetchArticle(id) {
  return request({
    url: '/vue-element-admin/article/detail',
    method: 'get',
    params: { id }
  })
}

export function fetchPv(pv) {
  return request({
    url: '/vue-element-admin/article/pv',
    method: 'get',
    params: { pv }
  })
}

export function createArticle(data) {
  return request({
    url: '/vue-element-admin/article/create',
    method: 'post',
    data
  })
}

export function updateArticlea(data) {
  return request({
    url: '/vue-element-admin/article/update',
    method: 'post',
    data
  })
}

// 获取文章列表
export function getArticleList({ pageNum, pageSize, title, tagId, authorUsername }) {
  return request({
    url: '/system/article/list',
    method: 'get',
    params: {
      pageNum: pageNum,
      pageSize: pageSize,
      title: title,
      tagId: tagId,
      authorUsername: authorUsername
    }
  })
}

// 新增文章
export function addArticle(data) {
  return request({
    url: '/system/article',
    method: 'post',
    data: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// 更新文章
export function updateArticle(data) {
  return request({
    url: '/system/article',
    method: 'put',
    data: JSON.stringify(data),
    headers: {
      'Content-Type': 'application/json'
    }
  })
}


// 删除文章
export function deleteArticle(articleIds) {
  return request({
    url: `/system/article/${articleIds}`,
    method: 'delete',
  })
}

// 改变文章状态
export function changeArticleStatus(articleId,status) {
  return request({
    url: '/system/article/change-status',
    method: 'put',
    params: {
      articleId: articleId,
      status: status
    }
  })
}