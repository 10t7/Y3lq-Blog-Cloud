module.exports = {
  // 关闭检查
  lintOnSave: false,
  devServer: {
    port: 8000,
    disableHostCheck: true,
    // 配置代理跨域
    proxy: {
      '/dev-api': {
        target: 'http://localhost:8080',
        pathRewrite: { '^/dev-api': '', },
        // changeOrigin: true,
        // ws: true, 
      },
    }
  },

  configureWebpack: {
    resolve: {
      symlinks: false,
    },
  },

  transpileDependencies: ['vuetify'],
}
