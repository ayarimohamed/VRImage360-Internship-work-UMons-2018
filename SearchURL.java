package com.google.devrel.vrviewapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import java.io.InputStream;
import java.net.URL;

public class SearchURL extends AppCompatActivity implements View.OnClickListener {

    // declaration des variables : etUrl,btnViewUrl,panoWidgetView1
    EditText etUrl;
    Button btnViewUrl;
    private VrPanoramaView panoWidgetView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_url);

        etUrl=(EditText)findViewById(R.id.searchEdt);
        btnViewUrl=(Button)findViewById(R.id.btnSearch);
        panoWidgetView1 = (VrPanoramaView)findViewById(R.id.pano_view1);

        btnViewUrl.setOnClickListener(this);
    }


    public void onClick(View v) {

        // recuperer le contenu de l'edittext
        final String chaine = etUrl.getText().toString();
        Log.v("Essai",chaine);


        // traitement à faire :
            new AsyncTask<Void, Void, Void>() {

                private Bitmap bitmap=null;

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        // get the image URL and input stream (try https://lc.cx/m2Xh)
                        URL imageURL = new URL(chaine);
                        InputStream imageInputStream = imageURL.openStream();
                        // get the bitmap from the input stream
                        bitmap = BitmapFactory.decodeStream(imageInputStream);
                    } catch (Exception e) {
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    if (bitmap != null) {
                        // add the bitmap to the  panoWidgetView1
                        VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
                        viewOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
                        panoWidgetView1.loadImageFromBitmap(bitmap, viewOptions);

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "cannot open URL " + chaine,
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }.execute();
    }
}
