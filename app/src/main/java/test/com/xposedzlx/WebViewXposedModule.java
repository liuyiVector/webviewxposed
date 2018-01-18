package test.com.xposedzlx;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.reflect.Field;
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
                Log.v("liang", "loadData 1");
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
                Log.v("liang", "loadDataWithBaseURL 2");
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
                Log.v("liang", "loadUrl 3");

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
                Log.v("liang", "loadUrl 4");
                setWebViewClient((WebView) param.thisObject);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookMethod("android.webkit.WebView", loadPackageParam.classLoader, "setWebViewClient", WebViewClient.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedHelpers.setAdditionalInstanceField((WebView) param.thisObject, "pkuWebViewClient", param.args[0]);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.v("liang", "setWebViewClient 5");
                setWebViewClient((WebView) param.thisObject);
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
        WebViewClient pre = (WebViewClient) XposedHelpers.getAdditionalInstanceField(webview, "pkuWebViewClient");
        if(pre == null || !(pre instanceof OptimizedWebViewClient)) {
            if(pre == null) {
                wv = new OptimizedWebViewClient(wv);
                Log.v("liang", "no wvc set");
            }
            else {
                Log.v("liang", "===have set wvc before====");
                wv = new OptimizedWebViewClient(pre);
            }

            Log.v("liang", "===try to set the optwvc====");
            XposedHelpers.setAdditionalInstanceField(webview, "pkuWebViewClient", wv);
            webview.setWebViewClient(wv);
            WebSettings websettings = webview.getSettings();
            websettings.setJavaScriptEnabled(true);
            websettings.setBuiltInZoomControls(true);
            websettings.setDomStorageEnabled(false);
            websettings.setDatabaseEnabled(false);
            websettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }else{
            Log.v("liang", "===have set optwvc before=====");
        }

    }


}
