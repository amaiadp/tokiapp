package com.example.amaia.grupo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by zihara on 9/05/18.
 */

public class ImagePagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    String[] imagenes;

    public ImagePagerAdapter(Context context, String[] images) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imagenes = images;
    }

    @Override
    public int getCount() {
        return imagenes.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.imagen, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imagenMostrar);
        String imagenString = imagenes[position];
        byte[] imagenByte = Base64.decode(imagenString, Base64.DEFAULT);
        Bitmap imagenBitmap = BitmapFactory.decodeByteArray(imagenByte, 0, imagenByte.length);
        imageView.setImageBitmap(imagenBitmap);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

}
