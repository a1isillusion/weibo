package com.github.nullguo.weibo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VideoShow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_show);
        Intent intent = getIntent();
        String dat=intent.getStringExtra("dat");
        Log.d("TAG","Viedo!!!!"+dat);
        final Handler handler=new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
                        jzVideoPlayerStandard.setUp((String)msg.obj
                                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "秒拍视频");
                        break;
                    default:
                        break;
                }
            }
        };
        final int witch;
        if(dat.indexOf("miaopai")!=-1)
            witch=0;
        else witch=1;
        if (dat.lastIndexOf("\\\"")!=-1){
        dat=dat.substring(2, dat.lastIndexOf("\\\""));
        }
        Log.d("TAG","URL!!"+dat);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(dat)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG","failure!!!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html=response.body().string();
                if(witch==0){
                    html=html.substring(html.indexOf(" \"videoSrc\":\"")+13,html.indexOf("\"poster\""));
                    html=html.substring(0,html.lastIndexOf("\","));

                }
                else {
                    html=html.substring(html.indexOf("flashvars=\"list=")+16,html.indexOf("<img src ="));
                    html=html.substring(0,html.lastIndexOf("\" />"));
                    html=java.net.URLDecoder.decode(html);
                }

                Log.d("TAG","videosource!!! :"+html);
                Message msg=new Message();
                msg.what=1;
                msg.obj=html;
                handler.sendMessage(msg);
            }
        });

    }
    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
    }

