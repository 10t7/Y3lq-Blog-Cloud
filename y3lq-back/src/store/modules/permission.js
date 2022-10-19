import { constantRoutes } from '@/router'
import { getMenuList } from '@/api/user'
import Layout from '@/layout'
/**
 * Use meta.role to determine if the current user has permission
 * 判断是否有角色菜单
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * 过滤拥有的菜单信息
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    // 判断是否有权限
    if (hasPermission(roles, tmp)) {
      // 获取该路由对于组件
      const component = tmp.component;
      // 判断是否有相应组件
      if (route.component) {
        // 判断是否是根组件
        if (component === 'Layout') {
          tmp.component = Layout;
        } else {
          // 获取对应具体组件信息
          tmp.component = (resolve) => require([`@/views/${component}`], resolve);
        }
      }
      // 判断是否有子菜单
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  // 将路由信息保存到 store (vuex)
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  // 动态生成路由
  generateRoutes({ commit }, roles) {
    return new Promise((resolve, reject) => {
      // 发送查询菜单数据
      getMenuList().then(res => {
        console.log(res);
        // 存放对应权限的路由信息
        let accessedRoutes;
        if (res.code === 200) {
          accessedRoutes = filterAsyncRoutes(res.data, roles)
        }
        // 将路由信息保存到 store
        commit('SET_ROUTES', accessedRoutes);
        // 放行
        resolve(accessedRoutes);
      }).catch(error => {
        // 拒绝
        reject(error);
      })
    })
  }
  // generateRoutes({ commit }, roles) {
  //   return new Promise(resolve => {
  //     let accessedRoutes
  //     if (roles.includes('admin')) {
  //       accessedRoutes = asyncRoutes || []
  //     } else {
  //       accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
  //     }
  //     commit('SET_ROUTES', accessedRoutes)
  //     resolve(accessedRoutes)
  //   })
  // }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
