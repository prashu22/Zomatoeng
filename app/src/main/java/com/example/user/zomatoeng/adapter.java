package com.example.user.zomatoeng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.viewHolder> {
    ArrayList arrayList;
    Context c;
    public adapter(Context context,ArrayList list) {
        this.arrayList=list;
        c=context;
    }

    @Override
    public adapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView itemView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        final model add = (model) arrayList.get(position);
        CardView cardView = holder.cardView;
        TextView name = cardView.findViewById(R.id.name);
        name.setText(add.getName());
        TextView ward = cardView.findViewById(R.id.local);
        ward.setText(add.getLocal());
        TextView rate = cardView.findViewById(R.id.rate);
        rate.setText(add.getRate());
        final ImageView imageView = cardView.findViewById(R.id.image);
        final URL[] url = {null};

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    url[0] = new URL(add.getImgs());
                    Bitmap bmp = BitmapFactory.decodeStream(url[0].openConnection().getInputStream());
                    imageView.setImageBitmap(bmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        Button edit, remove;
        Context context;
        private CardView cardView;

        public viewHolder(CardView Views) {
            super(Views);
            cardView = Views;
            this.context = Views.getContext();
        }
    }
}
