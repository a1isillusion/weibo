package com.github.nullguo.weibo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by nullguo on 2018/3/24 0024.
 */

public class hottopicadapter extends ArrayAdapter<Hottopic> {
    private int resourceId;
    private Context contextId;
    public hottopicadapter(Context context, int textViewResourceId,
                        List<Hottopic> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        contextId=context;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final Hottopic hottopic=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("android.intent.action.TopicShow");
                intent.putExtra("extparam",hottopic.extparam);
                contextId.startActivity(intent);
            }
        });
        SimpleDraweeView hottopic_pic=view.findViewById(R.id.hottopic_pic);
        hottopic_pic.setImageURI(hottopic.pic);
        TextView hottopic_name=view.findViewById(R.id.hottopic_name);
        hottopic_name.setText(hottopic.title_sub);
        TextView desc1=view.findViewById(R.id.hottopic_desc1);
        desc1.setText(hottopic.desc1);
        TextView desc2=view.findViewById(R.id.hottopic_desc2);
        desc2.setText(hottopic.desc2);
        return view;

    }
}
