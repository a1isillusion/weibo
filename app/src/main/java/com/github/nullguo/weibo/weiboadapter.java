package com.github.nullguo.weibo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nullguo on 2018/3/17 0017.
 */

public class weiboadapter extends ArrayAdapter<Weibo> {
    private int resourceId;
    private Context contextId;
    public weiboadapter(Context context, int textViewResourceId,
                        List<Weibo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        contextId=context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Weibo weibo = getItem(position);
        if (!weibo.isretweeted) {
            View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            SimpleDraweeView simpleDraweeView = view.findViewById(R.id.profile_image);
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
            TextView screen_name = view.findViewById(R.id.screen_name);
            screen_name.setText(weibo.screen_name);
            TextView created_at = view.findViewById(R.id.created_at);
            created_at.setText(weibo.created_at);
            TextView text = view.findViewById(R.id.text1);
            text.setText(SafeURLSpan.parseSafeHtml(weibo.wtext));
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(contextId,WeiboMainShow.class);
                    intent.putExtra("isretweeted","false");
                    intent.putExtra("id",weibo.wid);
                    contextId.startActivity(intent);
                }
            });
            text.setMovementMethod(LinkMovementMethod.getInstance());
            TextView reposts_count = view.findViewById(R.id.reposts_count);
            reposts_count.setText("转发:" + weibo.reposts_count);
            TextView comments_count = view.findViewById(R.id.comments_count);
            comments_count.setText("评论:" + weibo.comments_count);
            TextView attitudes_count = view.findViewById(R.id.attitudes_count);
            attitudes_count.setText("点赞:" + weibo.attitudes_count);
            SimpleDraweeView image1 = view.findViewById(R.id.image1);
            SimpleDraweeView image2 = view.findViewById(R.id.image2);
            SimpleDraweeView image3 = view.findViewById(R.id.image3);
            SimpleDraweeView image4 = view.findViewById(R.id.image4);
            SimpleDraweeView image5 = view.findViewById(R.id.image5);
            SimpleDraweeView image6 = view.findViewById(R.id.image6);
            SimpleDraweeView image7 = view.findViewById(R.id.image7);
            SimpleDraweeView image8 = view.findViewById(R.id.image8);
            SimpleDraweeView image9 = view.findViewById(R.id.image9);
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

            return view;
        }else {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.retweetedweibo_item, null);
            SimpleDraweeView simpleDraweeView = view.findViewById(R.id.profile_image);
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
            TextView screen_name = view.findViewById(R.id.screen_name);
            screen_name.setText(weibo.screen_name);
            TextView created_at = view.findViewById(R.id.created_at);
            created_at.setText(weibo.created_at);
            TextView text = view.findViewById(R.id.text1);
            text.setText(SafeURLSpan.parseSafeHtml(weibo.wtext));
            text.setMovementMethod(LinkMovementMethod.getInstance());
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(contextId,WeiboMainShow.class);
                    intent.putExtra("isretweeted","true");
                    intent.putExtra("id",weibo.wid);
                    contextId.startActivity(intent);
                }
            });
            TextView reposts_count = view.findViewById(R.id.reposts_count);
            reposts_count.setText("转发:" + weibo.reposts_count);
            TextView comments_count = view.findViewById(R.id.comments_count);
            comments_count.setText("评论:" + weibo.comments_count);
            TextView attitudes_count = view.findViewById(R.id.attitudes_count);
            attitudes_count.setText("点赞:" + weibo.attitudes_count);
            TextView retweetedtext=view.findViewById(R.id.retweetedtext);
            retweetedtext.setText(SafeURLSpan.parseSafeHtml(weibo.retweeted_screen_name+": "+weibo.retweeted_wtext+"<br>转发 "+weibo.retweeted_reposts_count+" | 评论 "+weibo.retweeted_comments_count+" | 点赞 "+weibo.retweeted_attitudes_count));
            retweetedtext.setMovementMethod(LinkMovementMethod.getInstance());
            retweetedtext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(contextId,WeiboMainShow.class);
                    intent.putExtra("isretweeted","false");
                    intent.putExtra("id",weibo.retweeted_id);
                    contextId.startActivity(intent);
                }
            });
            SimpleDraweeView image1 = view.findViewById(R.id.image1);
            SimpleDraweeView image2 = view.findViewById(R.id.image2);
            SimpleDraweeView image3 = view.findViewById(R.id.image3);
            SimpleDraweeView image4 = view.findViewById(R.id.image4);
            SimpleDraweeView image5 = view.findViewById(R.id.image5);
            SimpleDraweeView image6 = view.findViewById(R.id.image6);
            SimpleDraweeView image7 = view.findViewById(R.id.image7);
            SimpleDraweeView image8 = view.findViewById(R.id.image8);
            SimpleDraweeView image9 = view.findViewById(R.id.image9);
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

            return view;
        }
    }

}