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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HottopicShow extends AppCompatActivity {
    public Handler handler;
    public ListView hottopiclist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hottopic_show);
        setTitle("热门话题");
        hottopiclist=findViewById(R.id.hottopiclist);
        handler=new Handler() {
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case 1:
                        hottopicadapter adapter=new hottopicadapter(HottopicShow.this,R.layout.hottopic_item,(List<Hottopic>)msg.obj);
                        hottopiclist.setAdapter(adapter);
                        break;
                    default:
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://m.weibo.cn/api/container/getIndex?containerid=100803")//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = null;
                    response = client.newCall(request).execute();
                    if(response.isSuccessful()) {
                        List<Hottopic> hottopics=new ArrayList<Hottopic>();
                        JSONObject json=new JSONObject(response.body().string());
                        Log.d("TAG","ok?:"+json.getString("ok"));
                        JSONObject data=new JSONObject(json.getString("data"));
                        JSONArray cards=new JSONArray(data.getString("cards"));
                        for(int i=0;i<cards.length();i++){
                            JSONObject card=cards.getJSONObject(i);
                            if(card.getString("card_type_name").equals("trendtop_hot")||card.getString("card_type_name").equals("trendtop_hot_more")){
                                JSONArray card_group=new JSONArray(card.getString("card_group"));
                                for(int j=0;j<card_group.length();j++){
                                    JSONObject item=card_group.getJSONObject(j);
                                    Hottopic hottopic=new Hottopic();
                                    hottopic.title_sub=item.getString("title_sub");
                                    hottopic.pic=item.getString("pic");
                                    hottopic.extparam= URLEncoder.encode(item.getString("card_type_name"));
                                    Log.d("TAG",hottopic.extparam);
                                    hottopic.desc1=item.getString("desc1");
                                    hottopic.desc2=item.getString("desc2");
                                    hottopics.add(hottopic);

                                }
                            }
                            else continue;
                        }
                        Message msg=new Message();
                        msg.what=1;
                        msg.obj=hottopics;
                        handler.sendMessage(msg);
                    }
                }catch (Exception t){
                }
            }
        }).start();

    }
}
