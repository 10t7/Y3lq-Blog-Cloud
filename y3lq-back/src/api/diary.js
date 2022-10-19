import request from '@/utils/request'

// 获取日记列表
export function getDiaryList(pageNum, pageSize, username, status) {
    return request({
        url: '/system/diary/list',
        method: 'get',
        params: {
            pageNum: pageNum,
            pageSize: pageSize,
            username: username,
            status: status
        }
    })
}

// 改变日记状态
export function changeStatus(diaryId, status) {
    return request({
        url: '/system/diary/change-status',
        method: 'put',
        params: {
            diaryId: diaryId,
            status: status
        }
    })
}


// 改变日记排序
export function changeDiaryOrderNum(diaryId, orderNum) {
    return request({
        url: '/system/diary/change-diary-order-num',
        method: 'put',
        params: {
            diaryId: diaryId,
            orderNum: orderNum
        }
    })
}

// 批量删除日记
export function deleteDiary(diaryIds) {
    return request({
        url: `/system/diary/${diaryIds}`,
        method: 'delete',
    })
}