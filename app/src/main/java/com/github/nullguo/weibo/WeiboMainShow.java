package com.github.nullguo.weibo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeiboMainShow extends AppCompatActivity {
    public Handler handler;
    public ListView commentlist;
    public int page=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getStringExtra("isretweeted").equals("true")){
            setContentView(R.layout.retweetedweibo_main);
        }
        else {
            setContentView(R.layout.weibo_main);
        }
        final WaveSwipeRefreshLayout mWaveSwipeRefreshLayout = findViewById(R.id.weibomain);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                initcommentthread(handler,1);
            }
        });
        final RelativeLayout relativeLayout2=findViewById(R.id.relativeLayout2);
        commentlist=findViewById(R.id.commentlist);
        final List<Comment>commentdata=new ArrayList<Comment>();
        final commentadapter commentadapter=new commentadapter(WeiboMainShow.this,R.layout.comment_item,commentdata);
        commentlist.setAdapter(commentadapter);
        commentlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch(scrollState){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                        if (commentlist.getLastVisiblePosition() == commentlist.getCount() - 1) {
                          Log.d("TAG","add!!!!");
                          initcommentthread(handler,2);
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动状态

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                        relativeLayout2.setVisibility(View.GONE);
                        break;
                }
            }
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {



            }});
     handler=new Handler() {
         public void handleMessage(Message msg) {

             switch (msg.what) {
                 case 0:
                     final Context contextId=WeiboMainShow.this;
                     final Weibo weibo = (Weibo)msg.obj;
                     if (!weibo.isretweeted) {
                         SimpleDraweeView simpleDraweeView = findViewById(R.id.profile_image);
                         simpleDraweeView.setImageURI(weibo.profile_image_url);
                         simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View view) {
                                 Intent intent=new Intent("android.intent.action.IdWeibo");
                                 intent.putExtra("userid",weibo.userid);
                                 intent.putExtra("title",weibo.screen_name);
                                 contextId.startActivity(intent);
                             }
                         });
                         TextView screen_name = findViewById(R.id.screen_name);
                         screen_name.setText(weibo.screen_name);
                         TextView created_at = findViewById(R.id.created_at);
                         created_at.setText(weibo.created_at);
                         TextView text = findViewById(R.id.text1);
                         text.setText(SafeURLSpan.parseSafeHtml(weibo.wtext));
                         text.setMovementMethod(LinkMovementMethod.getInstance());
                         TextView reposts_count = findViewById(R.id.reposts_count);
                         reposts_count.setText("转发:" + weibo.reposts_count);
                         TextView comments_count = findViewById(R.id.comments_count);
                         comments_count.setText("评论:" + weibo.comments_count);
                         TextView attitudes_count = findViewById(R.id.attitudes_count);
                         attitudes_count.setText("点赞:" + weibo.attitudes_count);
                         SimpleDraweeView image1 = findViewById(R.id.image1);
                         SimpleDraweeView image2 = findViewById(R.id.image2);
                         SimpleDraweeView image3 = findViewById(R.id.image3);
                         SimpleDraweeView image4 = findViewById(R.id.image4);
                         SimpleDraweeView image5 = findViewById(R.id.image5);
                         SimpleDraweeView image6 = findViewById(R.id.image6);
                         SimpleDraweeView image7 = findViewById(R.id.image7);
                         SimpleDraweeView image8 = findViewById(R.id.image8);
                         SimpleDraweeView image9 = findViewById(R.id.image9);
                         if (weibo.images != null && 1 <= weibo.images.size()) {
                             image1.setImageURI(weibo.images.get(0));
                             image1.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.images.get(0).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image1.setVisibility(View.GONE);
                         }
                         if (weibo.images != null && 2 <= weibo.images.size()) {
                             image2.setImageURI(weibo.images.get(1));
                             image2.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.images.get(1).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image2.setVisibility(View.GONE);
                         }
                         if (weibo.images != null && 3 <= weibo.images.size()) {
                             image3.setImageURI(weibo.images.get(2));
                             image3.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.images.get(2).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image3.setVisibility(View.GONE);
                         }
                         if (weibo.images != null && 4 <= weibo.images.size()) {
                             image4.setImageURI(weibo.images.get(3));
                             image4.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.images.get(3).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image4.setVisibility(View.GONE);
                         }
                         if (weibo.images != null && 5 <= weibo.images.size()) {
                             image5.setImageURI(weibo.images.get(4));
                             image5.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.images.get(4).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image5.setVisibility(View.GONE);
                         }
                         if (weibo.images != null && 6 <= weibo.images.size()) {
                             image6.setImageURI(weibo.images.get(5));
                             image6.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.images.get(5).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image6.setVisibility(View.GONE);
                         }
                         if (weibo.images != null && 7 <= weibo.images.size()) {
                             image7.setImageURI(weibo.images.get(6));
                             image7.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.images.get(6).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image7.setVisibility(View.GONE);
                         }
                         if (weibo.images != null && 8 <= weibo.images.size()) {
                             image8.setImageURI(weibo.images.get(7));
                             image8.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.images.get(7).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image8.setVisibility(View.GONE);
                         }
                         if (weibo.images != null && 9 <= weibo.images.size()) {
                             image9.setImageURI(weibo.images.get(8));
                             image9.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.images.get(8).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image9.setVisibility(View.GONE);
                         }


                     }else {
                         SimpleDraweeView simpleDraweeView = findViewById(R.id.profile_image);
                         simpleDraweeView.setImageURI(weibo.profile_image_url);
                         simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View view) {
                                 Intent intent=new Intent("android.intent.action.IdWeibo");
                                 intent.putExtra("userid",weibo.userid);
                                 intent.putExtra("title",weibo.screen_name);
                                 contextId.startActivity(intent);
                             }
                         });
                         TextView screen_name = findViewById(R.id.screen_name);
                         screen_name.setText(weibo.screen_name);
                         TextView created_at = findViewById(R.id.created_at);
                         created_at.setText(weibo.created_at);
                         TextView text = findViewById(R.id.text1);
                         text.setText(SafeURLSpan.parseSafeHtml(weibo.wtext));
                         text.setMovementMethod(LinkMovementMethod.getInstance());
                         TextView reposts_count = findViewById(R.id.reposts_count);
                         reposts_count.setText("转发:" + weibo.reposts_count);
                         TextView comments_count = findViewById(R.id.comments_count);
                         comments_count.setText("评论:" + weibo.comments_count);
                         TextView attitudes_count = findViewById(R.id.attitudes_count);
                         attitudes_count.setText("点赞:" + weibo.attitudes_count);
                         TextView retweetedtext=findViewById(R.id.retweetedtext);
                         retweetedtext.setText(SafeURLSpan.parseSafeHtml(weibo.retweeted_screen_name+": "+weibo.retweeted_wtext+"<br>转发 "+weibo.retweeted_reposts_count+" | 评论 "+weibo.retweeted_comments_count+" | 点赞 "+weibo.retweeted_attitudes_count));
                         retweetedtext.setMovementMethod(LinkMovementMethod.getInstance());

                         SimpleDraweeView image1 = findViewById(R.id.image1);
                         SimpleDraweeView image2 = findViewById(R.id.image2);
                         SimpleDraweeView image3 = findViewById(R.id.image3);
                         SimpleDraweeView image4 = findViewById(R.id.image4);
                         SimpleDraweeView image5 = findViewById(R.id.image5);
                         SimpleDraweeView image6 = findViewById(R.id.image6);
                         SimpleDraweeView image7 = findViewById(R.id.image7);
                         SimpleDraweeView image8 = findViewById(R.id.image8);
                         SimpleDraweeView image9 = findViewById(R.id.image9);
                         if (weibo.retweeted_images != null && 1 <= weibo.retweeted_images.size()) {
                             image1.setImageURI(weibo.retweeted_images.get(0));
                             image1.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.retweeted_images.get(0).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image1.setVisibility(View.GONE);
                         }
                         if (weibo.retweeted_images != null && 2 <= weibo.retweeted_images.size()) {
                             image2.setImageURI(weibo.retweeted_images.get(1));
                             image2.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.retweeted_images.get(1).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image2.setVisibility(View.GONE);
                         }
                         if (weibo.retweeted_images != null && 3 <= weibo.retweeted_images.size()) {
                             image3.setImageURI(weibo.retweeted_images.get(2));
                             image3.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.retweeted_images.get(2).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image3.setVisibility(View.GONE);
                         }
                         if (weibo.retweeted_images != null && 4 <= weibo.retweeted_images.size()) {
                             image4.setImageURI(weibo.retweeted_images.get(3));
                             image4.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.retweeted_images.get(3).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image4.setVisibility(View.GONE);
                         }
                         if (weibo.retweeted_images != null && 5 <= weibo.retweeted_images.size()) {
                             image5.setImageURI(weibo.retweeted_images.get(4));
                             image5.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.retweeted_images.get(4).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image5.setVisibility(View.GONE);
                         }
                         if (weibo.retweeted_images != null && 6 <= weibo.retweeted_images.size()) {
                             image6.setImageURI(weibo.retweeted_images.get(5));
                             image6.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.retweeted_images.get(5).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image6.setVisibility(View.GONE);
                         }
                         if (weibo.retweeted_images != null && 7 <= weibo.retweeted_images.size()) {
                             image7.setImageURI(weibo.retweeted_images.get(6));
                             image7.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.retweeted_images.get(6).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image7.setVisibility(View.GONE);
                         }
                         if (weibo.retweeted_images != null && 8 <= weibo.retweeted_images.size()) {
                             image8.setImageURI(weibo.retweeted_images.get(7));
                             image8.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.retweeted_images.get(7).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image8.setVisibility(View.GONE);
                         }
                         if (weibo.retweeted_images != null && 9 <= weibo.retweeted_images.size()) {
                             image9.setImageURI(weibo.retweeted_images.get(8));
                             image9.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {
                                     String bigimageurl = weibo.retweeted_images.get(8).replace("thumbnail", "bmiddle");
                                     Intent intent = new Intent("android.intent.action.ImageShow");
                                     intent.putExtra("bigimageurl", bigimageurl);
                                     contextId.startActivity(intent);
                                 }
                             });
                         } else {
                             image9.setVisibility(View.GONE);
                         }

                     }
                     mWaveSwipeRefreshLayout.setRefreshing(true);
                     initcommentthread(handler,1);
                     break;
                 case 1:
                     commentdata.clear();
                     commentdata.addAll((List<Comment>)msg.obj);
                     commentadapter.notifyDataSetChanged();
                     mWaveSwipeRefreshLayout.setRefreshing(false);
                     break;
                 case 2:
                     commentdata.addAll((List<Comment>)msg.obj);
                     commentadapter.notifyDataSetChanged();
                     break;
                 default:
                     break;
             }
         }
     };

        initweibothrend(handler);

    }
    public void initweibothrend(final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    String texturl="http://m.weibo.cn/status/"+getIntent().getStringExtra("id");
                    Request textrequest = new Request.Builder()
                            .url(texturl)
                            .build();
                    Response textresponse =client.newCall(textrequest).execute();//得到Response 对象
                    if(textresponse.isSuccessful()) {
                        Weibo weibo=new Weibo();
                        String texthtml = textresponse.body().string();
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
                        Message msg=new Message();
                        msg.what=0;
                        msg.obj=weibo;
                        handler.sendMessage(msg);
                    }
                    else  Log.d("TAG", "FAIL!!!");
                }
                catch (Exception t){}
            }
        }
        ).start();

}
    public void initcommentthread(final Handler handler, final int msgwhat){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.d("TAG","start commentinit");
                    String url="https://m.weibo.cn/api/comments/show?id="+getIntent().getStringExtra("id")+"&page="+page++;
                    Log.d("TAG",url);
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response =virtuallogin.client.newCall(request).execute();
                    if(response.isSuccessful()){
                        List<Comment> comments=new ArrayList<Comment>();
                        JSONObject json=new JSONObject(response.body().string());
                        if(json.has("data")) {
                            JSONObject totaldata = new JSONObject(json.getString("data"));
                            if (totaldata.has("hot_data")) {
                                JSONArray hotdata = new JSONArray(totaldata.getString("hot_data"));
                                for(int i=0;i<hotdata.length();i++){
                                    Comment comment=new Comment();
                                    JSONObject item=hotdata.getJSONObject(i);
                                    comment.id=item.getString("id");
                                    comment.created_at=item.getString("created_at");
                                    comment.text=item.getString("text");
                                    comment.like_counts=item.getInt("like_counts");
                                    comment.liked=item.getString("liked");
                                    JSONObject user=new JSONObject(item.getString("user"));
                                    comment.userid=user.getString("id");
                                    comment.screen_name=user.getString("screen_name");
                                    comment.profile_image_url=user.getString("profile_image_url");
                                    comments.add(comment);
                                }
                            }
                            JSONArray data = new JSONArray(totaldata.getString("data"));
                            for(int i=0;i<data.length();i++){
                                Comment comment=new Comment();
                                JSONObject item=data.getJSONObject(i);
                                comment.id=item.getString("id");
                                comment.created_at=item.getString("created_at");
                                comment.text=item.getString("text");
                                comment.like_counts=item.getInt("like_counts");
                                comment.liked=item.getString("liked");
                                JSONObject user=new JSONObject(item.getString("user"));
                                comment.userid=user.getString("id");
                                comment.screen_name=user.getString("screen_name");
                                comment.profile_image_url=user.getString("profile_image_url");
                                comments.add(comment);
                            }
                        }
                        Message msg=new Message();
                        msg.what=msgwhat;
                        msg.obj=comments;
                        handler.sendMessage(msg);

                    }
                }
                catch (Exception t){

                }
            }
        }).start();
    }
}