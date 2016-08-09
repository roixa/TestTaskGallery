package com.tomleto.roix.testtaskgallery;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

//our page, fragment whis image and text view
public class ScreenSidePage extends Fragment {

    private ImageItem currItem;
    public void setInfo(ImageItem currItem){
        this.currItem=currItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_side_page, container, false);
        ImageView imageView=(ImageView) rootView.findViewById(R.id.imageView);
        TextView textView=(TextView)  rootView.findViewById(R.id.textView);
        textView.setText(currItem.getComment());
        //load image
        Picasso.with(getActivity())
                .load(currItem.getUrl())
                .into(imageView);
        return rootView;
    }
}