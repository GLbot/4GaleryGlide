package com.example.e1.a4galeryglide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;
import android.content.res.AssetManager;
import android.content.Context;
import android.util.Log;

// просмотр фото через стандартную библиотеку Google
public class ViewVRActivity extends AppCompatActivity {

    public static final String EXTRA_VIEW_PHOTO = "ViewVRActivity.VIEW_PHOTO";

    private VrPanoramaView panoWidgetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vr);

        // получаем выбранный файл
        final ViewPhoto mviewPhoto = getIntent().getParcelableExtra(EXTRA_VIEW_PHOTO);

        panoWidgetView = findViewById(R.id.vr_view);
        setTitle(mviewPhoto.getTitle());
        loadPanoImage(mviewPhoto);
    }

    @Override
    public void onPause() {
        panoWidgetView.pauseRendering();
        super.onPause();
    }

    @Override
    public void onResume() {
        panoWidgetView.resumeRendering();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // Destroy the widget and free memory.
        panoWidgetView.shutdown();
        super.onDestroy();
    }

    // загружаем фото для просмотра
    private void loadPanoImage(ViewPhoto vph) {

        Bitmap bitmap;
        String url = vph.getUrl();

        if (url.startsWith("file:///android_asset/")) {
            bitmap = getBitmapFromAsset(this, vph.getTitle());
        }
        else {
            bitmap = BitmapFactory.decodeFile(url);
        };

        // Log.d("==url", url);
        // Log.d("==title", vph.getTitle());

        VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();

        // ToDo сделать разбор фото и переключить TYPE_MONO / TYPE_STEREO
        viewOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
        panoWidgetView.loadImageFromBitmap(bitmap, viewOptions);
    }

    // From assets (for debug)
    public static Bitmap getBitmapFromAsset (Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(strName);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            return null;
        }
        return bitmap;
    }
}