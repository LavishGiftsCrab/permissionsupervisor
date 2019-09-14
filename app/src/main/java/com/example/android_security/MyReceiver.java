package com.example.android_security;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.text.SimpleDateFormat;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

//import androidx.core.app.NotificationCompat;

import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyReceiver extends BroadcastReceiver {
    private Context context;
    private FileHelper fileHelper;  //自己写的文件工具类，封装了io操作的代码
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        showNotification(intent);  //弹出通知
        saveLog(intent);            //记录日志
    }
    //日志记录的函数
    private void saveLog(Intent intent)
    {
        String name= intent.getStringExtra("name");
        String permission=intent.getStringExtra("permission");
        String time ;
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date  curDate =new Date(System.currentTimeMillis());//获取当前时间
        time  = formatter.format(curDate);
        fileHelper=new FileHelper(context);
        fileHelper.save(name,time,permission);
    }
    //弹出通知的函数
    private void showNotification(Intent intent)
    {
        String name= intent.getStringExtra("name");
        String permission=intent.getStringExtra("permission");
        NotificationManager manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Intent intent2=new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent2,0);
        if(Build.VERSION.SDK_INT >= 26)
        {
            //当sdk版本大于26
            String id = "channel_1";
            String description = "222";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
//                     channel.enableLights(true);
            channel.enableVibration(true);//
            channel.setSound(null, Notification.AUDIO_ATTRIBUTES_DEFAULT);

            manager.createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(context, id)
//                        .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("监控助手")
                    .setContentText(name+"使用了"+permission)
                    .setContentIntent(pendingIntent)
                    .setFullScreenIntent(pendingIntent,true)
                    .setAutoCancel(true)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .build();
            manager.notify(1, notification);
        }
        else
        {
        //当sdk版本小于26
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("监控助手")
                .setContentText(name+"使用了"+permission)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setFullScreenIntent(pendingIntent, true)
                .build();
        manager.notify(1,notification);
    }
    }
}
