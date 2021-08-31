package com.example.opticalcharacterrecognizer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;

import me.drakeet.materialdialog.MaterialDialog;

public class TextRecognizer extends AppCompatActivity {



    private Button captureImage,detectText;  //buttons
    private ImageView imageView;   //display captured image
    private TextView textView;     //display the text
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final int SELECT_PHOTO = 101;
    private final int CAPTURE_PHOTO = 102;
    final private int REQUEST_CODE_WRITE_STORAGE = 1;
    private int check=0;
    Uri uri;
    FirebaseVisionImage firebaseVisionImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recognizer);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.applogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        captureImage=findViewById(R.id.Capture_Image);
        detectText=findViewById(R.id.detect_text_from_image);
        imageView=findViewById(R.id.image_view);
        textView=findViewById(R.id.textview);
        textView.setMovementMethod(new ScrollingMovementMethod());


        //----------------------------------------ON CLICKING CAPTURE IMAGE BUTTON------------------------------------------------------------------------------------------------------------
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* dispatchTakePictureIntent();
                textView.setText("");*/

                //CAMERA PERMISSION


                int hasWriteStoragePermission = 0;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hasWriteStoragePermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

                if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_WRITE_STORAGE);
                    }
                    //return;
                }

                listDialogue();






            }
        });


       //------------------------------------------ON CLICKING DETECT TEXT BUTTON------------------------------------------------------------------------------------------------------------------
        detectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectTextFromImage();
            }
        });
    }


    //-----------------------------------SUBSIDARY FUNCTIONS----------------------------------------------------------------------------------------------------
    //function to open camera
/*    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            check=1;
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(),"Error: ", Toast.LENGTH_SHORT).show();
        }
    }*/
    //function to convert image to bitmap
    //@Override
   /* protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap  imageBitmap = (Bitmap) extras.get("data");


            imageView.setImageBitmap(imageBitmap);
            firebaseVisionImage= FirebaseVisionImage.fromBitmap(imageBitmap);

        }
    }*/


    public void listDialogue(){
        final ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        arrayAdapter.add("Take Photo");
        arrayAdapter.add("Select Gallery");

        ListView listView = new ListView(this);
        listView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (8 * scale + 0.5f);
        listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
        listView.setDividerHeight(0);
        listView.setAdapter(arrayAdapter);

        final MaterialDialog alert = new MaterialDialog(this).setContentView(listView);

        alert.setPositiveButton("Cancel", new View.OnClickListener() {
            @Override public void onClick(View v) {
                alert.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){

                    alert.dismiss();
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //Uri uri  = Uri.parse("file:///sdcard/photo.jpg");
                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "img.jpg";
                    uri = Uri.parse(root);
                    //i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(i, CAPTURE_PHOTO);

                }else {

                    alert.dismiss();
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);

                }
            }
        });

        alert.show();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {

                    try {
                    Uri imageUri = imageReturnedIntent.getData();
                    String selectedImagePath = getPath(imageUri);
                    File f = new File(selectedImagePath);
                    Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
                    check=1;
                    imageView.setImageBitmap(bmp);
                    firebaseVisionImage= FirebaseVisionImage.fromBitmap(bmp);
                    }
                    catch (Exception e){

                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case CAPTURE_PHOTO:
                if (resultCode == RESULT_OK) {


                        Bitmap bmp = imageReturnedIntent.getExtras().getParcelable("data");
                        check = 1;
                        imageView.setImageBitmap(bmp);
                        firebaseVisionImage = FirebaseVisionImage.fromBitmap(bmp);



                }

                break;
        }
    }




    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null,
                null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }


//------------------------------------Text Recognizer-------------------------------------------------------------------------------------------

    private void detectTextFromImage() {

        if (check == 1) {
            FirebaseVision firebaseVision = FirebaseVision.getInstance();
            FirebaseVisionTextRecognizer firebaseVisionTextDetector = firebaseVision.getOnDeviceTextRecognizer();
            Task<FirebaseVisionText> task = firebaseVisionTextDetector.processImage(firebaseVisionImage);
            task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    String s = firebaseVisionText.getText();
                    if(!s.isEmpty()) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(s);
                        check = 0;
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No text detected " , Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener((OnFailureListener) e -> {
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                Log.d("Error: ", e.getMessage());
            });

        }
        else{
            Toast.makeText(getApplicationContext(), "Error: " , Toast.LENGTH_SHORT).show();
        }
    }




}