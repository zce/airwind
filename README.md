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

platforms/android/app/src/main/java/me/zce/airwind/MainActivity.java 修改为以下代码：

```java
/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package me.zce.airwind;

import android.os.Bundle;
import android.webkit.WebView;

import org.apache.cordova.*;

public class MainActivity extends CordovaActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }

        // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);

        WebView webView = (WebView) appView.getView();
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
    }
}
```
