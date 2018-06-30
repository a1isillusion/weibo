package com.github.nullguo.weibo;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Eases.EaseType;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.ClickEffectType;
import com.nightonke.boommenu.Types.DimType;
import com.nightonke.boommenu.Types.OrderType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.jar.JarException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    BoomMenuButton boomMenuButton;
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
        setContentView(R.layout.activity_main);
        final WaveSwipeRefreshLayout mWaveSwipeRefreshLayout = findViewById(R.id.main_swipe);
        final List<Weibo> listdata=new ArrayList<Weibo>();
        final weiboadapter adapter=new weiboadapter(MainActivity.this,R.layout.weibo_item,listdata);
        final ListView listView=findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch(scrollState){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                        imageResume();
                        if (listView.getLastVisiblePosition() == listView.getCount() - 1) {
                            weibothread(handler,++page,1);
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
                    case 0:
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                        break;
                    case 1:
                        listdata.addAll((List<Weibo>) msg.obj);
                        adapter.notifyDataSetChanged();
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                        break;
                    case 2:
                        listdata.clear();
                        listdata.addAll((List<Weibo>) msg.obj);
                        adapter.notifyDataSetChanged();
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                        break;
                    default:
                        break;
                }
            }
        };
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                weibothread(handler,1,2);

            }
        });

         boomMenuButton = (BoomMenuButton)findViewById(R.id.boom);

        mWaveSwipeRefreshLayout.setRefreshing(true);
        new virtuallogin().init(MainActivity.this,handler);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int[][] subButtonColors={{Color.parseColor("#00000000"),Color.parseColor("#00000000")},{Color.parseColor("#00000000"),Color.parseColor("#00000000")},{Color.parseColor("#00000000"),Color.parseColor("#00000000")}};
        new BoomMenuButton.Builder()
                // set all sub buttons with subButtons method
                //.subButtons(subButtonDrawables, subButtonColors, subButtonTexts)
                // or add each sub button with addSubButton method
                .addSubButton(this, R.drawable.boom_menu_button, subButtonColors[0], "发微博")
                .addSubButton(this, R.drawable.boom_menu_button, subButtonColors[1], "热门话题")
                .addSubButton(this, R.drawable.boom_menu_button, subButtonColors[2], "搜索")
                .frames(80)
                .duration(800)
                .delay(100)
                .showOrder(OrderType.RANDOM)
                .hideOrder(OrderType.RANDOM)
                .button(ButtonType.HAM)
                .boom(BoomType.PARABOLA_2)
                .place(PlaceType.HAM_3_1)
                .showMoveEase(EaseType.EaseOutBack)
                .hideMoveEase(EaseType.EaseOutCirc)
                .showScaleEase(EaseType.EaseOutBack)
                .hideScaleType(EaseType.EaseOutCirc)
                .rotateDegree(720)
                .showRotateEase(EaseType.EaseOutBack)
                .hideRotateType(EaseType.Linear)
                .autoDismiss(true)
                .cancelable(true)
                .dim(DimType.DIM_6)
                .clickEffect(ClickEffectType.RIPPLE)
                .boomButtonShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .subButtonTextColor(Color.BLACK)
                .onBoomButtonBlick(null)
                .animator(null)
                .onSubButtonClick(null)
                // this only work when the place type is SHARE_X_X
                .shareStyle(0, 0, 0)
                .init(boomMenuButton);
        boomMenuButton.setOnSubButtonClickListener(new BoomMenuButton.OnSubButtonClickListener() {
            @Override
            public void onClick(int buttonIndex) {
                // 返回被点击的子按钮下标
                Log.d("TAG","点击了"+buttonIndex);
                if(buttonIndex==0){
                    Intent intent=new Intent(MainActivity.this,WeiboSend.class);
                    startActivity(intent);
                }
                if(buttonIndex==1){
                    Intent intent=new Intent(MainActivity.this,HottopicShow.class);
                    startActivity(intent);
                }
            }
        });
    }
    public void weibothread(final Handler handler, final int page, final int msgwhat){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    Request request = new Request.Builder()
                            .url("https://weibo.cn/?since_id=0&max_id=G8qV1DjVK&prev_page=1&page="+page)//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = null;
                    response = virtuallogin.client.newCall(request).execute();//得到Response 对象
                    String str=response.body().string();

                    String pattern = "<div class=\"c\" id=\"M_[a-zA-Z0-9]+\">";
                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(str);
                    List<String> widlist=new ArrayList<String>();
                    while (m.find( )) {
                        String wid=m.group().substring(21, m.group().indexOf("\">"));
                        Log.d("TAG",wid);
                        widlist.add(wid);
                    }
                    List<Weibo>weiboList=new ArrayList<Weibo>();
                    for(int i=0;i<widlist.size();i++){
                        String texturl="http://m.weibo.cn/status/"+widlist.get(i);
                        Request textrequest = new Request.Builder()
                                .url(texturl)
                                .build();
                        Response textresponse =client.newCall(textrequest).execute();//得到Response 对象
                        if(textresponse.isSuccessful()) {
                            Weibo weibo=new Weibo();
                            String texthtml = textresponse.body().string();
                            if(texthtml.indexOf("var $render_data =")==-1)continue;
                            texthtml = texthtml.substring(texthtml.indexOf("var $render_data =") + 19, texthtml.lastIndexOf("[0] || {};"));
                            JSONArray textarray=new JSONArray(texthtml);
                            JSONObject jsonObject=textarray.getJSONObject(0);
                            JSONObject status=new JSONObject(jsonObject.getString("status"));
                            Log.d("TAG","创造时间"+status.getString("created_at"));
                            weibo.created_at=status.getString("created_at");

                            weibo.wid=status.getString("id");

                            weibo.wtext=status.getString("text");

                            weibo.reposts_count=status.getInt("reposts_count");

                            weibo.comments_count=status.getInt("comments_count");

                            weibo.attitudes_count=status.getInt("attitudes_count");
                            if(status.has("pics")){
                                List<String> picurllist=new ArrayList<String>();
                                JSONArray pics=new JSONArray(status.getString("pics"));

                                for(int count=0;count<pics.length();count++){
                                    JSONObject pic=pics.getJSONObject(count);
                                    String picurl=pic.getString("url");
                                    picurllist.add(picurl);

                                }
                                weibo.images=picurllist;
                            }
                            JSONObject user=new JSONObject(status.getString("user"));

                            weibo.userid=user.getString("id");

                            weibo.screen_name=user.getString("screen_name");

                            weibo.profile_image_url=user.getString("profile_image_url");
                            if(status.has("retweeted_status")){
                                weibo.isretweeted=true;
                                JSONObject retweeted_status=new JSONObject(status.getString("retweeted_status"));

                                weibo.retweeted_created_at=retweeted_status.getString("created_at");

                                weibo.retweeted_id=retweeted_status.getString("id");

                                weibo.retweeted_wtext=retweeted_status.getString("text");

                                weibo.retweeted_reposts_count=retweeted_status.getInt("reposts_count");

                                weibo.retweeted_comments_count=retweeted_status.getInt("comments_count");

                                weibo.retweeted_attitudes_count=retweeted_status.getInt("attitudes_count");
                                if(retweeted_status.has("pics")){
                                    List<String> picurllist=new ArrayList<String>();
                                    JSONArray pics=new JSONArray(retweeted_status.getString("pics"));

                                    for(int count=0;count<pics.length();count++){
                                        JSONObject pic=pics.getJSONObject(count);
                                        String picurl=pic.getString("url");
                                        picurllist.add(picurl);

                                    }
                                    weibo.retweeted_images=picurllist;
                                }
                                JSONObject retweeted_user=new JSONObject(retweeted_status.getString("user"));

                                weibo.retweeted_userid=retweeted_user.getString("id");

                                weibo.retweeted_screen_name=retweeted_user.getString("screen_name");



                            }
                            weiboList.add(weibo);
                        }
                        else  Log.d("TAG", "FAIL!!!");

                    }
                    Message msg=new Message();
                    msg.what=msgwhat;
                    msg.obj=weiboList;
                    handler.sendMessage(msg);
                }
                catch (Exception t){

                }
            }
        }).start();

    }
}
