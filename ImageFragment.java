
package com.google.devrel.vrviewapp;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ImageFragment extends Fragment {

    private VrPanoramaView panoWidgetView;
    private ImageLoaderTask backgroundImageLoaderTask;

    private Button btnGallery ;
    private Button btnURL ;
    private static final int RESULT_LOAD_GALLERY = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.image_fragment, container,false);
        panoWidgetView = (VrPanoramaView) v.findViewById(R.id.pano_view);
        btnGallery = (Button) v.findViewById(R.id.btnGallery);
        btnURL = (Button) v.findViewById(R.id.btnUrl);


        // add a listener to the btnGallery (it will open the MediaStore.Images.Media
        btnGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Create intentPICK to open the image from the Photos' Gallery
                /*Intent intentPICK = new Intent (
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );
                intentPICK.setType("image/*");
                // Start the Intent
                startActivityForResult(intentPICK, RESULT_LOAD_GALLERY);  */
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_GALLERY);
            }
        });

        btnURL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent myIntent = new Intent(getContext(), SearchURL.class);
                startActivity(myIntent);
            }
        });

        return v;
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
    private synchronized void loadPanoImage() {
        ImageLoaderTask task = backgroundImageLoaderTask;
        if (task != null && !task.isCancelled()) {
            // Cancel any task from a previous loading.
            task.cancel(true);
        }

        // pass in the name of the image to load from assets.
        VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
        viewOptions.inputType = VrPanoramaView.Options.TYPE_MONO;

        // use the name of the image in the assets/ directory.
        String panoImageName = "pano.jpg";

        // create the task passing the widget view and call execute to start.
        task = new ImageLoaderTask(panoWidgetView, viewOptions, panoImageName);
        task.execute(getActivity().getAssets());
        backgroundImageLoaderTask = task;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadPanoImage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) {
            Toast.makeText(getContext(), "Result NOK or missing data", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Bitmap bitmap;
            switch (requestCode) {

                case RESULT_LOAD_GALLERY:

                    // Get the Image from data
                    Uri imageURI = data.getData();
                    try {
                        // get image real path
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = new CursorLoader(getContext(), imageURI, proj, null, null, null).loadInBackground();

                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String imageRealPath = cursor.getString(column_index);
                        Toast.makeText(getContext(), imageRealPath, Toast.LENGTH_LONG).show();

                        VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
                        viewOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
                        // get image bitmap
                        bitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageURI);
                        // add image to image view
                        panoWidgetView.loadImageFromBitmap(bitmap, viewOptions);
                    }catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;


                default:
                    Toast.makeText(getContext(), "You haven' picked an image",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }
}
