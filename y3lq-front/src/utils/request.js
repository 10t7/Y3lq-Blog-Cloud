import axios from 'axios'
import { getToken,removeToken } from '@/utils/auth'
import store from '@/store'

// create an axios instance
const service = axios.create({
  baseURL: "/dev-api", // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 7000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    // console.log(store.user.token)
    // console.log(this.$store.user.token)
    // console.log(store.getters.token)
    if (store.state.user.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data
    if(res.code == 401){
      removeToken()
    }
    if (res.code !== 200) {
      store.dispatch('snackbar/openSnackbar', {
        msg: res.msg || "未知错误",
        color: 'error'
      })
      return Promise.reject(new Error(res.msg || '未知错误'))
    } else {
      return res
    }

    // if the custom code is not 200, it is judged as an error.
    // if (res.code !== 200) {


    // Message({
    //   message: res.msg || 'Error',
    //   type: 'error',
    //   duration: 4 * 1000
    // })


    // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
    // if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
    //   // to re-login
    //   MessageBox.confirm('登录已过期，请重新登录', {
    //     confirmButtonText: '登录',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   }).then(() => {
    //     store.dispatch('user/resetToken').then(() => {
    //       location.reload()
    //     })
    //   })
    // }
    //   return Promise.reject(new Error(res.msg || 'Error'))
    // } else {
    //   return res
    // }
  },
  // error => {
  //   console.log('err' + error) // for debug
  //   Message({
  //     message: error.message,
  //     type: 'error',
  //     duration: 5 * 1000
  //   })
  //   return Promise.reject(error)
  // }
)

export default service
