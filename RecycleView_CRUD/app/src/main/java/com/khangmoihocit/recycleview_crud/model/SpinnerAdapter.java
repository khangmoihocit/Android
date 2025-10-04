package com.khangmoihocit.recycleview_crud.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.khangmoihocit.recycleview_crud.R;

public class SpinnerAdapter extends BaseAdapter {
    private int []imgs = {R.drawable.img, R.drawable.img_1, R.drawable.img_2, R.drawable.img_3};
    private Context context;

    public SpinnerAdapter(Context context){
        this.context = context;
    }


    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public Object getItem(int position) {
        return imgs[position];
    }

    @Override
    public long getItemId(int position) {
        return imgs[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        ImageView img = itemView.findViewById(R.id.img);
        img.setImageResource(imgs[position]);
        return itemView;
    }
}
