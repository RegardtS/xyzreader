package com.example.xyzreader;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class NewDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);
//        getActionBar().setDisplayHomeAsUpEnabled(true);


        Glide.with(getApplicationContext())
//                    .load(mCursor.getString(ArticleLoader.Query.PHOTO_URL))
                .load(R.drawable.temp)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .dontTransform()
                .error(R.drawable.no_image_available)
                .into((ImageView) findViewById(R.id.imageMain));

    }

}
