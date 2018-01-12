import axios from 'axios'
import Message from 'element-ui'

const service = axios.create({
  baseURL: process.env.API_URL,
  timeout: 5000
})

console.log(process.env.API_URL)

// http request 拦截器
service.interceptors.request.use(
  config => {
    config.data = JSON.stringify(config.data)
    config.headers = {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// http response 拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      Message({
        message: res.message,
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(new Error('Error'))
    } else {
      return res
    }
  },
  error => {
    return Promise.reject(error)
  }
)

export default service
