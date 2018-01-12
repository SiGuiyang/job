import request from '@/request/index'

/**
 查询
*/
export function getList (params) {
  return request({
    url: '/job/query',
    method: 'post',
    params: params
  })
}

/**
 保存
*/
export function saveJob (params) {
  return request({
    url: '/job/save',
    method: 'post',
    params: params
  })
}

/**
 更新
*/
export function updateJob (params) {
  return request({
    url: '/job/update',
    method: 'post',
    params: params
  })
}

/**
 删除
*/
export function deleteJob (params) {
  return request({
    url: '/job/delete/' + params.id,
    method: 'post'
  })
}

/**
 立即执行
*/
export function executeJob (params) {
  return request({
    url: '/job/execute/' + params.id,
    method: 'post'
  })
}

/**
 暂停
*/
export function pauseJob (params) {
  return request({
    url: '/job/pause/' + params.id,
    method: 'post'
  })
}

/**
 恢复
*/
export function resumeJob (params) {
  return request({
    url: '/job/resume/' + params.id,
    method: 'post'
  })
}
