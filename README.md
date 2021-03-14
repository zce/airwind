# airwind

## 环境配置

- Node.js 14.x (内置 npm)
- Cordova 10.0.0

```shell
$ npm install cordova -g
```

## 浏览器运行步骤

```shell
$ cd path/to/project
$ npm install # 安装依赖
$ npm run serve
```

浏览器测试：http://localhost:8080

## Android 运行步骤

```shell
$ npm run dev
```

## 屏幕适配问题

platforms/android/app/src/main/java/me/zce/airwind/MainActivity.java onCreate 最后添加以下代码：

```java
import android.webkit.WebView;

// Viewport adaptation
WebView webView = (WebView) appView.getView();
webView.getSettings().setLoadWithOverviewMode(true);
webView.getSettings().setUseWideViewPort(true);
```
