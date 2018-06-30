package com.github.nullguo.weibo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import com.facebook.drawee.backends.pipeline.Fresco;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IdWeibo extends AppCompatActivity {
    public int page=1;
    public Handler handler;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    // 暂停图片请求
    public static void imagePause() {
        Fresco.getImagePipeline().pause();
    }

    // 恢复图片请求
    public static void imageResume() {
        Fresco.getImagePipeline().resume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.id_weibo);
        setTitle(getIntent().getStringExtra("title")+"的微博");
        final WaveSwipeRefreshLayout mWaveSwipeRefreshLayout = findViewById(R.id.idweiboreflash);
        final List<Weibo> listdata=new ArrayList<Weibo>();
        final weiboadapter adapter=new weiboadapter(IdWeibo.this,R.layout.weibo_item,listdata);
        final ListView listView=findViewById(R.id.idweibolist);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch(scrollState){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                        imageResume();
                        if (listView.getLastVisiblePosition() == listView.getCount() - 1) {
                            idweibothread(handler,getIntent().getStringExtra("userid"),++page);
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动状态
                        imagePause();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                        break;
                }
            }
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {



            }});
        handler=new Handler() {
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case 1:
                        listdata.addAll((List<Weibo>) msg.obj);
                        adapter.notifyDataSetChanged();
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                        break;
                    default:
                        break;
                }
            }
        };
        idweibothread(handler,getIntent().getStringExtra("userid"),1);

        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
           idweibothread(handler,getIntent().getStringExtra("userid"),1);

            }
        });




    }
public void idweibothread(final Handler handler,final String userid,final int page){
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Log.d("TAG","userid!!!!!"+userid);
                final OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                //String url="https://api.weibo.com/2/statuses/user_timeline.json?access_token=2.00SWrEID9m4VaEc23ceb5106mj1KPD";
                String url="https://m.weibo.cn/api/container/getIndex?type=uid&value="+userid;
                Log.d("TAG",url);
                Request request = new Request.Builder()
                        .url(url)
                        .build();//创建Request 对象
                Response response = null;
                Log.d("Tag","!!");
                response =client.newCall(request).execute();//得到Response 对象
                if (response.isSuccessful()) {
                    Log.d("Tag","!!!!!!");
                    List<Weibo> weibolist=new ArrayList<Weibo>();
                    String html=response.body().string();
                    html=html.substring(html.indexOf("\"tab_type\":\"weibo\""),html.indexOf("\"apipath\""));
                    html=html.substring(html.indexOf(" \"containerid\":")+35,html.lastIndexOf("\","));
                    String lasturl="https://m.weibo.cn/api/container/getIndex?type=uid&value=5372556014&containerid="+html+"&page="+page;
                    Log.d("TAG",lasturl);
                    Request lastrequest = new Request.Builder()
                            .url(lasturl)
                            .build();//创建Request 对象
                    Response lastresponse = null;
                    Log.d("Tag","!!");
                    lastresponse =client.newCall(lastrequest).execute();
                    if(lastresponse.isSuccessful()){
                        String json=lastresponse.body().string();
                        JSONObject jsonObject=new JSONObject(json);
                        JSONObject data=new JSONObject(jsonObject.getString("data"));
                        JSONArray weibos=new JSONArray(data.getString("cards"));
                        for(int i=0;i<weibos.length();i++){
                            Weibo weibo=new Weibo();
                            JSONObject item=weibos.getJSONObject(i);
                            if(item.getInt("card_type")==9) {
                                JSONObject jsonObject1 = new JSONObject(item.getString("mblog"));
                                weibo.wid = jsonObject1.getString("id");
                                JSONObject user = new JSONObject(jsonObject1.getString("user"));
                                weibo.userid = user.getString("id");
                                weibo.profile_image_url = user.getString("profile_image_url");
                                weibo.screen_name = user.getString("screen_name");
                                weibo.created_at = jsonObject1.getString("created_at");
                                weibo.wtext = jsonObject1.getString("text");
                                weibo.reposts_count = jsonObject1.getInt("reposts_count");
                                weibo.comments_count = jsonObject1.getInt("comments_count");
                                weibo.attitudes_count = jsonObject1.getInt("attitudes_count");
                                List<String> images = new ArrayList<String>();
                                if(jsonObject1.has("pics")) {
                                    JSONArray imagearray = new JSONArray(jsonObject1.getString("pics"));
                                    for (int j = 0; j < imagearray.length(); j++) {
                                        JSONObject image = imagearray.getJSONObject(j);
                                        String imageurl = image.getString("url");
                                        Log.d("TAG", imageurl);
                                        images.add(imageurl);
                                    }
                                }
                                weibo.images = images;
                                if (jsonObject1.has("retweeted_status")) {
                                    JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("retweeted_status"));
                                    user = new JSONObject(jsonObject2.getString("user"));
                                    weibo.isretweeted = true;
                                    weibo.retweeted_id = jsonObject2.getString("id");
                                    weibo.retweeted_screen_name = user.getString("screen_name");
                                    weibo.retweeted_created_at = jsonObject2.getString("created_at");
                                    weibo.retweeted_wtext = jsonObject2.getString("text");
                                    weibo.retweeted_reposts_count = jsonObject2.getInt("reposts_count");
                                    weibo.retweeted_comments_count = jsonObject2.getInt("comments_count");
                                    weibo.retweeted_attitudes_count = jsonObject2.getInt("attitudes_count");
                                    List<String> retweetedimages = new ArrayList<String>();
                                    if(jsonObject2.has("pics")) {
                                        JSONArray retweetedimagearray = new JSONArray(jsonObject2.getString("pics"));
                                        for (int k = 0; k < retweetedimagearray.length(); k++) {
                                            JSONObject image = retweetedimagearray.getJSONObject(k);
                                            String imageurl = image.getString("url");
                                            Log.d("TAG", imageurl);
                                            retweetedimages.add(imageurl);
                                        }
                                    }
                                    weibo.retweeted_images = retweetedimages;
                                }
                                weibolist.add(weibo);
                                Log.d("TAG",weibo.wtext);
                            }
                        }
                        Message msg=new Message();
                        msg.what=1;
                        msg.obj=weibolist;
                        handler.sendMessage(msg);
                    }
                }
            }
            catch(Exception t) {
                t.printStackTrace();
            }
        }
    }).start();

}
}