package com.example.user.zomatoeng;

import java.util.ArrayList;

/**
 * Created by user on 4/1/2018.
 */

public class model {
    String name;
    String imgs;
    String rate;
    String local;

    public model(String name,String imgs, String rate, String local) {
        this.name = name;
        this.imgs = imgs;
        this.rate = rate;
        this.local = local;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
