package com.example.android_security;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Logs extends Activity implements View.OnClickListener {
    private FileHelper fileHelper;         //io操作工具类
    public static  final int UPDATE_TEXT=1;         //静态常量
    String text;
   private TextView textView;
   //在hander中更新界面
   private Handler handler=new Handler(new Handler.Callback() {
       public boolean handleMessage(Message msg)
       {
           switch (msg.what)
           {
               case UPDATE_TEXT:
                   textView.setText("");
                   break;
                default:
                   break;
           }
           return true;
       }
   });
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs);
        fileHelper=new FileHelper(this);
        try{
           text = fileHelper.read();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        textView= findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(text);
        Button b=findViewById(R.id.button4);
        b.setOnClickListener(this);
    }
    @Override
    public void onClick(View arg0) {
        fileHelper.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=UPDATE_TEXT;
                handler.sendMessage(message);
            }
        }).start();
    }
}
