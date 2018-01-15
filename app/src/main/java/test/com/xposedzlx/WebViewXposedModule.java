package test.com.xposedzlx;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class WebViewXposedModule implements IXposedHookLoadPackage{

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        XposedHelpers.findAndHookMethod("android.webkit.WebView", loadPackageParam.classLoader, "loadData", String.class, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                setWebViewClient((WebView) param.thisObject);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookMethod("android.webkit.WebView", loadPackageParam.classLoader, "loadDataWithBaseURL", String.class, String.class, String.class, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                setWebViewClient((WebView) param.thisObject);

            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("android.webkit.WebView", loadPackageParam.classLoader, "loadUrl", String.class,  new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                setWebViewClient((WebView) param.thisObject);

            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });


        XposedHelpers.findAndHookMethod("android.webkit.WebView", loadPackageParam.classLoader, "loadUrl", String.class, Map.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                setWebViewClient((WebView) param.thisObject);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });

    }



    public void setWebViewClient(WebView webview){

        //WebViewClient oldClient = webview.getWebViewClient();

//        if(oldClient instanceof OptimizedWebViewClient)
//            return;


        WebViewClient wv = new WebViewClient() {
            // Load opened URL in the application instead of standard browser
            // application
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("liang/OverrideUrl", url);
                view.loadUrl(url);
                return true;
            }
            public void onPageFinished(WebView view, String url) {
                Log.d("liang/endtime", String.valueOf(System.currentTimeMillis()));
            }
			/*
			public WebResourceResponse shouldInterceptRequest(WebView view,
		            String url) {
				Log.d("liang/shouldInterceptRequest", url);
				WebResourceResponse response = null;

			    if (url.contains("jpg")) {
			          try {
			              InputStream localCopy = getAssets().open("a.jpg");
			              response = new WebResourceResponse("image/jpg", "UTF-8", localCopy);
			          } catch (IOException e) {
			              e.printStackTrace();
			          }
			    }
			    return response;
		    }
		    */

        };
        wv = new OptimizedWebViewClient(wv);
        webview.setWebViewClient(wv);
        /*
        webview.setWebChromeClient(new WebChromeClient() {
			// Set progress bar during loading
			public void onProgressChanged(WebView view, int progress) {
				MainActivity.this.setProgress(progress * 100);
			}
		});
		*/
        WebSettings websettings = webview.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setBuiltInZoomControls(true);
        websettings.setDomStorageEnabled(false);
        websettings.setDatabaseEnabled(false);
        websettings.setCacheMode(WebSettings.LOAD_DEFAULT);
    }
}
