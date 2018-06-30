package com.github.nullguo.weibo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TopicShow extends AppCompatActivity {
    public Handler handler;
    public ListView topicweibolist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_show);
        setTitle("#"+URLDecoder.decode(getIntent().getStringExtra("extparam"))+"#");
        final WaveSwipeRefreshLayout mWaveSwipeRefreshLayout = findViewById(R.id.topicshow);
        topicweibolist=findViewById(R.id.topicweibolist);
        handler=new Handler() {
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case 1:
                        weiboadapter adapter=new weiboadapter(TopicShow.this,R.layout.weibo_item,(List<Weibo>)msg.obj);
                        topicweibolist.setAdapter(adapter);
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                        break;
                    default:
                        break;
                }
            }
        };
        topicshowthread(handler);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                topicshowthread(handler);
            }
        });

    }
    public void topicshowthread(final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Log.d("TAG","https://m.weibo.cn/api/container/getindex?containerid=100808topic&extparam="+getIntent().getStringExtra("extparam"));
                    Request request = new Request.Builder()
                            .url("https://m.weibo.cn/api/container/getindex?containerid=100808topic&extparam="+getIntent().getStringExtra("extparam")+"&count=50")//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = null;
                    response = virtuallogin.client.newCall(request).execute();
                    if(response.isSuccessful()) {
                        JSONObject json=new JSONObject(response.body().string());
                        JSONObject data=new JSONObject(json.getString("data"));
                        JSONArray cards=new JSONArray(data.getString("cards"));
                        List<Weibo> weibos=new ArrayList<Weibo>();
                        for(int i=0;i<cards.length();i++){
                            JSONObject card=cards.getJSONObject(i);
                            if(card.has("_cur_filter")){
                                JSONArray card_group=new JSONArray(card.getString("card_group"));
                                for(int j=0;j<card_group.length();j++){
                                    JSONObject mblog=new JSONObject(card_group.getJSONObject(j).getString("mblog"));
                                    Weibo weibo=new Weibo();
                                    weibo.created_at=mblog.getString("created_at");
                                    weibo.wid=mblog.getString("id");
                                    weibo.wtext=mblog.getString("text");
                                    List<String>picslist=new ArrayList<String>();
                                    if(mblog.has("pics")){
                                        JSONArray pics=new JSONArray(mblog.getString("pics"));
                                        for (int count = 0; count < pics.length(); count++) {
                                            JSONObject image = pics.getJSONObject(count);
                                            String imageurl = image.getString("url");
                                            Log.d("TAG", imageurl);
                                            picslist.add(imageurl);
                                        }
                                    }
                                    weibo.images=picslist;
                                    weibo.reposts_count=mblog.getInt("reposts_count");
                                    weibo.comments_count=mblog.getInt("comments_count");
                                    weibo.attitudes_count=mblog.getInt("attitudes_count");
                                    JSONObject user=new JSONObject(mblog.getString("user"));
                                    weibo.userid=user.getString("id");
                                    weibo.screen_name=user.getString("screen_name");
                                    weibo.profile_image_url=user.getString("profile_image_url");
                                    weibos.add(weibo);
                                }

                            }
                        }

                        Message msg=new Message();
                        msg.what=1;
                        msg.obj=weibos;
                        handler.sendMessage(msg);
                    }
                }catch (Exception t){
                }
            }
        }).start();
    }
}
