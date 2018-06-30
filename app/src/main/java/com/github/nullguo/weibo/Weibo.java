package com.github.nullguo.weibo;

import java.util.List;

/**
 * Created by nullguo on 2018/3/17 0017.
 */

public class Weibo {
    public String wid;
    public String userid;
    public String profile_image_url;
    public String screen_name;
    public String created_at;
    public String wtext;
    public int reposts_count;
    public int comments_count;
    public int attitudes_count;
    public List<String> images;
    public boolean isretweeted=false;
    public String retweeted_id;
    public String retweeted_userid;
    public String retweeted_screen_name;
    public String retweeted_created_at;
    public String retweeted_wtext;
    public int retweeted_reposts_count;
    public int retweeted_comments_count;
    public int retweeted_attitudes_count;
    public List<String> retweeted_images;
}
