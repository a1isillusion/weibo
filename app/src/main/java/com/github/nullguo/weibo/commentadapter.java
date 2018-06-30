package com.github.nullguo.weibo;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by nullguo on 2018/3/25 0025.
 */

public class commentadapter extends ArrayAdapter<Comment> {
    private int resourceId;
    private Context contextId;
    public commentadapter(Context context, int textViewResourceId,
                          List<Comment> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        contextId = context;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final Comment comment=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        SimpleDraweeView image=view.findViewById(R.id.profile_image);
        image.setImageURI(comment.profile_image_url);
        TextView screen_name=view.findViewById(R.id.screen_name);
        screen_name.setText(comment.screen_name);
        TextView created_at=view.findViewById(R.id.created_at);
        created_at.setText(comment.created_at);
        TextView text=view.findViewById(R.id.text);
        text.setText(SafeURLSpan.parseSafeHtml(comment.text));
        text.setMovementMethod(LinkMovementMethod.getInstance());
        TextView like_counts=view.findViewById(R.id.like_counts);
        like_counts.setText("点赞 "+comment.like_counts);
        return view;

    }
}
