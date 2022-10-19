import request from '@/utils/request'

// 留言
export function commentMessage(content, type, fromNickname, fromEmail) {
    return request({
        url: '/system/message/leave-message',
        method: 'post',
        params: {
            content,
            type,
            fromNickname,
            fromEmail
        }
    })
}

// 获取留言列表
export function messageList(pageNum, pageSize) {
    return request({
        url: '/system/message/blog-list',
        method: 'get',
        params: {
            pageSize,
            pageNum,
        }
    })
}

// 评论留言
export function comment(content, type, fromNickname, fromEmail, firstMessageId, toUserId,toNickname) {
    return request({
        url: '/system/message/comment',
        method: 'post',
        params: {
            content,
            type,
            fromNickname,
            fromEmail,
            firstMessageId,
            toUserId,
            toNickname
        }
    })
}

// 获取指定留言所有的评论
export function messageChildren(messageId) {
    return request({
        url: `/system/message/message-children/${messageId}`,
        method: 'get',
    })
}