package com.github.nullguo.weibo;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.Normalizer;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WeiboSend extends AppCompatActivity {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weibo_send);
        final EditText weibotext=findViewById(R.id.weibotext);
        Button send=findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Tag","sendweibo!!!");
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           FormBody body=new FormBody.Builder()
                                .add("content",weibotext.getText().toString())
                                 .add("visible","0")
                                   .add("settop","1").build();

                           Request request = new Request.Builder()
                                   .url("https://weibo.cn/?since_id=0&max_id=G8qV1DjVK&prev_page=1&page=1")
                                   .build();//创建Request 对象
                           Response response = null;
                           response = virtuallogin.client.newCall(request).execute();//得到Response 对象
                           String str=response.body().string();
                           Log.d("TAG",str);
                           str=str.substring(str.indexOf("/mblog/sendmblog?st=")+17,str.indexOf(" accept-charset=\"UTF-8\" method=\"post\">")-1);
                           str="https://weibo.cn/mblog/sendmblog?rl=0&"+str;
                           Log.d("TAG",str);
                           Request sendrequest = new Request.Builder()
                                   .url(str)
                                   .post(body)
                                   .build();//创建Request 对象
                           response = virtuallogin.client.newCall(sendrequest).execute();
                           if(response.isSuccessful())Log.d("TAG","SENDWEIBOSUCCESS");
                           finish();
                       }catch (Exception e) {
                           e.printStackTrace();
                       }
                   }
               }).start();
            }
        });
    }
}
