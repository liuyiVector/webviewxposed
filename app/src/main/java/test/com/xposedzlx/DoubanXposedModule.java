package test.com.xposedzlx;

import android.os.Bundle;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class DoubanXposedModule implements IXposedHookLoadPackage{

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {


        if(!loadPackageParam.packageName.equals("com.douban.movie"))
            return;

        XposedHelpers.findAndHookMethod("com.douban.movie.app.HomeActivity", loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Class ma = loadPackageParam.classLoader.loadClass("com.douban.movie.MovieApplication");
                XposedHelpers.setStaticBooleanField(ma,"sSplashShowed", true);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookMethod("com.douban.movie.MovieApplication", loadPackageParam.classLoader, "initDoubanAndCrystalAd", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                XposedBridge.log("hook the douban movie application");
                return null;
            }
        });
    }
}
