package com.github.nullguo.weibo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.File;

public class ImageShow extends AppCompatActivity {
    public float getInitImageScale(String imagePath){
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        // 拿到图片的宽和高
        int dw = bitmap.getWidth();
        int dh = bitmap.getHeight();
        float scale = 1.0f;
        //图片宽度大于屏幕，但高度小于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh <= height) {
            scale = width * 1.0f / dw;
        }
        //图片宽度小于屏幕，但高度大于屏幕，则放大图片至填满屏幕宽
        if (dw <= width && dh > height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都小于屏幕，则放大图片至填满屏幕宽
        if (dw < width && dh < height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都大于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh > height) {
            scale = width * 1.0f / dw;
        }
        return scale;
    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.image_show);
        final SubsamplingScaleImageView imageView =  findViewById(R.id.bigimage);
        final  SimpleDraweeView gifimage = findViewById(R.id.gifimage);
        imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        imageView.setMinScale(1.0F);
        imageView.setMaxScale(4.0F);//最大显示比例（太大了图片显示会失真，因为一般微博长图的宽度不会太宽）
        Intent intent=getIntent();
        String bigimageurl=intent.getStringExtra("bigimageurl");
        Log.d("TAG","imageshow!!"+bigimageurl);
        if(bigimageurl.indexOf(".gif")==-1) {
            gifimage.setVisibility(View.GONE);
            Glide.with(this)
                    .load(bigimageurl).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    // 将保存的图片地址给SubsamplingScaleImageView,这里注意设置ImageViewState设置初始显示比例
                    imageView.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(getInitImageScale(resource.getAbsolutePath()), new PointF(0, 0), 0));
                }
            });
        }
        else{
            imageView.setVisibility(View.GONE);
            if(bigimageurl.indexOf("orj360")!=-1)bigimageurl=bigimageurl.replace("orj360","large");
            Log.d("TAG","bigimagegifurl！！！！"+bigimageurl);
            PipelineDraweeController gifcontroller =(PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setUri(bigimageurl)
                    .setAutoPlayAnimations(true) //自动播放gif动画
                    .build();
            gifimage.setController(gifcontroller);
        }
    }


}
