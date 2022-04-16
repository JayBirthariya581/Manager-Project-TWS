package com.geartocare.ManageMechanicP.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.github.chrisbanes.photoview.PhotoView;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.geartocare.ManageMechanicP.R;

public class ImageViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);


        getWindow().setStatusBarColor(ContextCompat.getColor(ImageViewerActivity.this,R.color.black));

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        //photoView.setImageResource(R.drawable.ic_main2);
        Glide.with(ImageViewerActivity.this)
                .load(getIntent().getStringExtra("imageUrl"))
                .placeholder(R.drawable.manage_icon)
                .into(photoView);
    }
}