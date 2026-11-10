const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({ transpileDependencies: true, devServer: { host: '127.0.0.1', port: 8089, proxy: { '/tijiancms': { target: 'http://127.0.0.1:8088', changeOrigin: true } } } })
