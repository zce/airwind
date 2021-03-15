const path = require('path')
const fs = require('fs/promises')

module.exports = async () => {
  const target = path.join(__dirname, '../platforms/android/app/src/main/java/me/zce/airwind/MainActivity.java')

  const original = await fs.readFile(target, 'utf8')

  if (original.includes('import android.webkit.WebView;')) return

  const contents = original
    .replace('import android.os.Bundle;', 'import android.os.Bundle;\nimport android.webkit.WebView;')
    .replace('loadUrl(launchUrl);', `loadUrl(launchUrl);

        // Viewport adaptation
        WebView webView = (WebView) appView.getView();
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);`)

  await fs.writeFile(target, contents)
}
