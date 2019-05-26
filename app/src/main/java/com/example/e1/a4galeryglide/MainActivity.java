package com.example.e1.a4galeryglide;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // проверяем права на доступ

        //Log.d("== permit_before", "" + checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE));
        //Log.d("== permit_before", "" + PackageManager.PERMISSION_GRANTED);


        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        }

        //Log.d("== permit", "" + checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE));
        //Log.d("== permit", "" + PackageManager.PERMISSION_GRANTED);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            showImages();
        } else finish();

    }

    /* альбом с фотографиями */
    private void showImages() {

        ArrayList<ViewPhoto> photos;
        String albumPath = getString(R.string.album_path);
        setTitle(albumPath);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        RecyclerView recyclerView = findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //ToDo взять каталог из настроек, пока жестко прописано
        // photos = ViewPhoto.getViewPhotos(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "");
        photos = ViewPhoto.getViewPhotos(albumPath);

        // for debug only
        photos.add (new ViewPhoto("file:///android_asset/mountain.jpg", "mountain.jpg"));
        photos.add (new ViewPhoto("file:///android_asset/IMG_20151204_155452.vr_.jpg", "IMG_20151204_155452.vr_.jpg"));

        ImageGalleryAdapter adapter = new ImageGalleryAdapter(this, photos);
        recyclerView.setAdapter(adapter);
    }

    private class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View photoView = inflater.inflate(R.layout.item_photo, parent, false);
            ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

            ViewPhoto ViewPhoto = mViewPhotos.get(position);
            ImageView imageView = holder.mPhotoImageView;

            Glide.with(mContext)
                    .load(ViewPhoto.getUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return (mViewPhotos.size());
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;
            public MyViewHolder(View itemView) {

                super(itemView);
                mPhotoImageView = itemView.findViewById(R.id.iv_photo);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                // выбрали фото - вызываем просмотр в новой активности ViewVRActivity
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    ViewPhoto ViewPhoto = mViewPhotos.get(position);
                    Intent intent = new Intent(mContext, ViewVRActivity.class);
                    intent.putExtra(ViewVRActivity.EXTRA_VIEW_PHOTO, ViewPhoto);
                    startActivity(intent);
                }
            }
        }

        private ArrayList<ViewPhoto> mViewPhotos;
        private Context mContext;

        public ImageGalleryAdapter(Context context, ArrayList<ViewPhoto> ViewPhotos) {
            mContext = context;
            mViewPhotos = ViewPhotos;
        }
    }
}
