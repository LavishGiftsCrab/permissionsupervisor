package com.example.android_security;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Tuto implements IXposedHookLoadPackage {
    private Context context=null;

    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        //第一个钩子，负责获取当前应用的Context，从而利用当前应用的Context向我们的应用发送广播
        XposedHelpers.findAndHookMethod(ContextThemeWrapper.class, "attachBaseContext", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                 context=(Context) param.args[0];
            }
        });

        //对权限申请函数的hook
        XposedHelpers.findAndHookMethod(
                "android.content.ContextWrapper",//要hook的类
                ClassLoader.getSystemClassLoader(),//获取classLoader
                "checkSelfPermission",//要hook的方法（函数）checkPermission(String var1, int var2, int var3);
                //    Context.class,
                String.class,
              /*  int.class,
                int.class,*/
            /*   String.class,//第一个参数
               StateCallback.class,//第二个参数
               Handler.class,*/
                new XC_MethodHook() {
                    //这里是hook回调函数
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("检查权限:" +param.args[0] );
                        //当权限涉及位置信息的时候，向我们的监控app发送广播
                        if (param.args[0].toString().contains("LOCATION")){
                            if(context!=null){

                                Intent intent=new Intent();
                                intent.setAction("com.example.sec.BroadcastReceiverTest");
                                intent.setComponent( new ComponentName( "com.example.android_security" ,
                                        "com.example.android_security.MyReceiver") );
                                intent.putExtra("name", getAppName(context));
                                intent.putExtra("permission","位置");
                                context.sendBroadcast(intent);
                            }
                        }
//                        if(context!=null){
//                    Intent intent=new Intent();
//                    intent.setAction("com.example.sec.BroadcastReceiverTest");
//                    intent.setComponent( new ComponentName( "com.example.android_security" ,
//                            "com.example.android_security.MyReceiver") );
//                    intent.putExtra("name", getAppName(context));
//                    intent.putExtra("permission",permission);
//                    context.sendBroadcast(intent);
//                }
                    }
                }
        );
        XposedHelpers.findAndHookMethod(
                "android.content.ContextWrapper",//要hook的类
                ClassLoader.getSystemClassLoader(),//获取classLoader
                "checkCallingOrSelfPermission",//要hook的方法（函数）checkPermission(String var1, int var2, int var3);

                String.class,

                new XC_MethodHook() {
                    //这里是hook回调函数
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                        XposedBridge.log("检查权限2:" +param.args[0] );

                    }
                }
        );
        //监控应用对麦克风硬件的使用
       findAndHookMethod("android.media.AudioRecord", ClassLoader.getSystemClassLoader(),
               "startRecording",
        new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("=========AUDIO====================");
                if(context!=null){
                    Intent intent=new Intent();
                    intent.setAction("com.example.sec.BroadcastReceiverTest");
                    intent.setComponent( new ComponentName( "com.example.android_security" ,
                            "com.example.android_security.MyReceiver") );
                    intent.putExtra("name", getAppName(context));
                    intent.putExtra("permission","录音");
                    context.sendBroadcast(intent);
                }
            }
       });
       //监控应用对摄像机硬件的使用
        findAndHookMethod("android.hardware.Camera", ClassLoader.getSystemClassLoader(), "open",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("=========CAMERA====================");
                        if(context!=null){
                            Intent intent=new Intent();
                            intent.setAction("com.example.sec.BroadcastReceiverTest");
                            intent.setComponent( new ComponentName( "com.example.android_security" ,
                                    "com.example.android_security.MyReceiver") );
                            intent.putExtra("name", getAppName(context));
                            intent.putExtra("permission","相机");
                            context.sendBroadcast(intent);
                        }
                    }});

//        findAndHookMethod("android.location.LocationManager", ClassLoader.getSystemClassLoader(), "getLastKnownLocation",
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log("=========LOCATION====================");
////                XposedBridge.log("======================================="+(String)param.args[1]);
//                        if(context!=null){
//
//                            Intent intent=new Intent();
//                            intent.setAction("com.example.sec.BroadcastReceiverTest");
//                            intent.setComponent( new ComponentName( "com.example.android_security" ,
//                                    "com.example.android_security.MyReceiver") );
//                            intent.putExtra("name", getAppName(context));
//                            intent.putExtra("permission","位置信息");
//                            context.sendBroadcast(intent);
//                        }
//                    }});

    }

    }

//public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable{
//    HookMethod(TelephonyManager.class, "getDeviceId", "00000000000000");
//}
//    private void HookMethod(final Class clazz, final String method, final String result){
//        try{
//            XposedHelpers.findAndHookMethod(clazz, method, new Object[] { new XC_MethodHook() {
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    param.setResult(result);
//                }
//            } });
//        } catch (Throwable e){
//            e.printStackTrace();
//        }
//    }



