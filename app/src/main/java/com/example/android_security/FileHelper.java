package com.example.android_security;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {
    private Context mContext;
    public String mylog="mylog.txt";   //文件名
    public FileHelper(Context mContext) {
        this.mContext = mContext;
    }
    /**
     * 定义文件保存的方法，写入到文件中，所以是输出流
     */
    public void save(String name, String time,String permission) {
        String content = name+"在"+time+"使用了"+permission;
        FileOutputStream fos = null;
        try {
            // Context.MODE_PRIVATE私有权限，Context.MODE_APPEND追加写入到已有内容的后面
            fos = mContext.openFileOutput(mylog, Context.MODE_APPEND);
            fos.write(content.getBytes());
            fos.write("\r\n".getBytes());//写入换行
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 定义文件读取的方法
     */
    public String read() throws FileNotFoundException {

        FileInputStream fis = mContext.openFileInput(mylog);

        byte[] buff = new byte[1024];
        StringBuilder sb = new StringBuilder("");
        int len = 0;
       try {
           while ((len = fis.read(buff)) > 0) {
               sb.append(new String(buff, 0, len));
           }
       }catch (IOException e) {
           e.printStackTrace();
       } finally {
           if (fis != null) {
               try {
                   fis.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
        return sb.toString();
    }
    /**
     * 定义文件清空的方法
     */
    public void clear()
    {
        String kong="";
        FileOutputStream fos = null;
        try {
            // Context.MODE_PRIVATE私有权限，Context.MODE_APPEND追加写入到已有内容的后面
            fos = mContext.openFileOutput(mylog, Context.MODE_PRIVATE);
            fos.write(kong.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
