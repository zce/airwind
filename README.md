# airwind

## 环境配置

- Node.js 14.x (内置 npm)

```shell
$ cd path/to/project
$ npm install # 安装依赖
```

## 浏览器运行步骤

```shell
$ npm run serve
```

浏览器测试：http://localhost:8080

## Android 运行步骤

```shell
$ npm run develop
```

> 第一次需要先执行 build 任务

## 打包 APK

打包 debug 版本

```shell
$ npm run build
```

打包发行版本

```shell
$ npm run build -- --release
```
