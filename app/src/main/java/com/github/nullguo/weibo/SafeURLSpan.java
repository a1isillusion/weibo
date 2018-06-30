package com.github.nullguo.weibo;

/**
 * Created by nullguo on 2018/3/19 0019.
 */
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.provider.Browser;
import android.text.Html;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class SafeURLSpan extends URLSpan {
    public SafeURLSpan(String url) {
        super(url);
    }

    @Override
    public void onClick(View widget) {
        try {
            final Uri uri = Uri.parse(getURL());
            final Context context = widget.getContext();
            final Intent intent = new Intent("android.intent.action.VideoShow");
            Log.d("TAG","Span"+getURL());
            if (context != null && intent != null&&(getURL().indexOf("miaopai")!=-1||getURL().indexOf("video.weibo.com/show")!=-1)) {
                intent.putExtra("dat", getURL());
                Log.d("TAG","Span"+getURL());
                context.startActivity(intent);
            }
            else if(getURL().indexOf("m.weibo.cn/n/")!=-1){
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(getURL())
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("TAG","failure!!!");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String html= response.request().url().toString();
                        Log.d("TAG","HEADER :"+html);
                        html=html.substring(html.indexOf("u/")+2,html.length());
                        Log.d("TAG","str :"+html);
                        Intent intent=new Intent("android.intent.action.IdWeibo");
                        intent.putExtra("title",getURL().substring(getURL().lastIndexOf("n/")+2,getURL().length()));
                        intent.putExtra("userid",html);
                        context.startActivity(intent);
                    }
                });
            }
            else if(getURL().indexOf("m.weibo.cn/k/")!=-1&&getURL().indexOf("?from=feed")!=-1){
                String extparam=getURL().substring(getURL().indexOf("m.weibo.cn/k/")+13,getURL().indexOf("?from=feed"));
                Intent topicintent=new Intent("android.intent.action.TopicShow");
                topicintent.putExtra("extparam",extparam);
                context.startActivity(topicintent);
            }
            else {
                Log.d("TAG","URL!!"+getURL());
               Intent defaultintent=new Intent("android.intent.action.DefaultWebView");
               String url;
               if(getURL().startsWith("\\\"")){
                url=getURL().substring(2,getURL().length()-2);
               }
               else{
                 url=getURL();
               }
               defaultintent.putExtra("url",url);
               context.startActivity(defaultintent);
            }
        } catch (Throwable ex) {
        }
    }

    public static CharSequence parseSafeHtml(CharSequence html) {
        return replaceURLSpans(Html.fromHtml(html.toString()));
    }

    public static CharSequence replaceURLSpans(CharSequence text) {
        if (text instanceof Spannable) {
            final Spannable s = (Spannable)text;
            final URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
            if (spans != null && spans.length > 0) {
                for (int i = spans.length - 1; i >= 0; i--) {
                    final URLSpan span = spans[i];
                    final int start = s.getSpanStart(span);
                    final int end = s.getSpanEnd(span);
                    final int flags = s.getSpanFlags(span);
                    s.removeSpan(span);
                    s.setSpan(new SafeURLSpan(span.getURL()), start, end, flags);
                }
            }
        }
        return text;
    }
}