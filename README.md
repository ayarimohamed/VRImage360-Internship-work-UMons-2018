# VRImage360
This project is based on another project I found in github.I tried to add some features like opening and viewing 360 images from the mediastore and viewing these images from the URL link in case there is an internet connection.

View the image from the mediaStore:

At the level of the class ImageFragment.java just add a listener to the button "btnGallery" in which we build an intent that takes us directly to the mediastore to choose the image we want to display in the panoramaView.
Do not forget to add the image type for the photoPickerIntent.

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_GALLERY);
            }
        });
        
        
View the image from a URL link:

At the level of the class ImageFragment.java just add a listener to the button "btnURL" in which we build an intent that takes us directly to another activity "searchURL" which contains an editText, a button and a vrpanoramaView.
The user finds a Url link by default, just click on the "view" button to display the corresponding image.
Of course, permission to access the internet is mandatory or an exception will be thrown and an error message appears as a Toast.

• Recover the contents of the edittext

        final String chaine = etUrl.getText().toString();
        Log.v("Essai",chaine);
        
 • The treatment to be done will be done at Asynctask.
So in the doInBackground method just grab the URL of the image and the inputStream of the image: 

        URL imageURL = new URL(chaine);
                        InputStream imageInputStream = imageURL.openStream();
                        bitmap = BitmapFactory.decodeStream(imageInputStream);

• At the onPostExecute method, just add the bitmap to our panoWidgetView.

        VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
                        viewOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
                        panoWidgetView1.loadImageFromBitmap(bitmap, viewOptions);
