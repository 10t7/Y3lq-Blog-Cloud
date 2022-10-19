import request from '@/utils/request'

// 新增日记
export function addDiary(content){
    return request ({
        url: '/system/diary',
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(content)
    })
}


// 获取推荐日记
export function getRecommendDiary(){
    return request ({
        url: '/system/diary/recommend-diary',
        method: 'get',
    })
}

// 评论日记
export function commentDiary(diaryId, toUserId, content) {
    return request({
        url: '/system/diary/comment',
        method: 'post',
        params: {
            diaryId,
            toUserId,
            content,
        }
    })
}


// 获取日记列表
export function diaryList(pageNum, pageSize) {
    return request({
        url: '/system/diary/blog-list',
        method: 'get',
        params: {
            pageNum: pageNum,
            pageSize: pageSize,
        }
    })
}

// 点赞
export function postLike(diaryId) {
    return request({
        url: `/system/diary/like/${diaryId}`,
        method: 'put',
    })
}

// 取消点赞
export function cancelLike(diaryId) {
    return request({
        url: `/system/diary/cancel-like/${diaryId}`,
        method: 'put',
    })
}

// 删除日记
export function deleteDiary(diaryId) {
    return request({
        url: `/system/diary/deleteById/${diaryId}`,
        method: 'delete',
    })
}

// 删除日记
export function deleteDiaryComment(commentId) {
    return request({
        url: `/system/diary/deleteComment/${commentId}`,
        method: 'delete',
    })
}