import request from '@/utils/request'

// 获取所有标签 tree
export function tagTreeList() {
    return request({
        url: '/system/tag/tag-tree-list',
        method: 'get',
    })
}

// 新增文章
export function addArticle(data) {
    return request({
        url: '/system/article',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(data),
    })
}

// 获取文章列表
export function articleList(pageNum, pageSize) {
    return request({
        url: '/system/article/blog-list',
        method: 'get',
        params: {
            pageNum: pageNum,
            pageSize: pageSize,
        }
    })
}


// 获取文章
export function getArticleById(articleId) {
    return request({
        url: `/system/article/get`,
        method: 'get',
        params:{
            articleId:articleId
        }
    })

}

// 获取推荐文章
export function getRecommendArticle() {
    return request({
        url: '/system/article/recommend',
        method: 'get',
    })
}

// 获取指定文章一级评论评论
export function getArticleCommentList(articleId, pageNum, pageSize) {
    return request({
        url: `/system/article/articleCommentList`,
        method: 'get',
        params: {
            articleId:articleId,
            pageNum: pageNum,
            pageSize: pageSize,
        }
    })
}

// 获取指定评论所有子评论
export function getArticleCommentChildren(commentId) {
    return request({
        url: `/system/article/articleCommentChildren`,
        method: 'get',
        params:{
            commentId:commentId
        }
    })
}


// 评论指定文章
export function commentArticle(data) {
    return request({
        url: '/system/article/comment',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(data),
    })
}

// 点赞文章
export function postLikeArticle(articleId) {
    return request({
        url: '/system/article/post-like-article',
        method: 'put',
        params:{
            articleId
        }
    })
}

// 取消点赞文章
export function cancelLikeArticle(articleId) {
    return request({
        url: '/system/article/cancel-like-article',
        method: 'put',
        params:{
            articleId
        }
    })
}

// 点赞文章评论
export function postLikeArticleComment(commentId) {
    return request({
        url: '/system/article/post-like-article-comment',
        method: 'put',
        params:{
            commentId
        }
    })
}

// 取消点赞文章评论
export function cancelLikeArticleComment(commentId) {
    return request({
        url: '/system/article/cancel-like-article-comment',
        method: 'put',
        params:{
            commentId
        }
    })
}

// 删除文章评论
export function deleteArticleComment(commentId) {
    return request({
        url: '/system/article/article-comment',
        method: 'delete',
        params:{
            commentId
        }
    })
}

// 获取上传图片的签名
export function getUploadPictureSignature() {
    return request({
        url: '/thirdparty/oss/signature',
    })
}




