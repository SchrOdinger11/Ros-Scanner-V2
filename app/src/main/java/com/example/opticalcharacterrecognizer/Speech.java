package com.example.opticalcharacterrecognizer;


import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.io.IOException;
import java.util.Locale;

import static android.Manifest.permission.CAMERA;
import static android.content.ContentValues.TAG;

//import com.google.mlkit.nl.languageid.LanguageIdentification;
//import com.google.mlkit.nl.languageid.LanguageIdentifier;

/*
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
*/

public class Speech extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {



        String[] languages={"NONE","BENGALI","URDU","KANNADA","MARATHI","HINDI"};
        private TextView textView;
        private SurfaceView surfaceView;
        private TextView languageidentify;

        private TextView textViewTranslatedText;

        private CameraSource cameraSource;
        private TextRecognizer textRecognizer;
        Spinner spin;
        private TextToSpeech textToSpeech;
        private String stringResult = null;
        private int checkTextView=0;
        private String languageName=null;
        private TextView sourceLanguage;
        private TextView displaytext;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_speech);


            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.applogo);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PackageManager.PERMISSION_GRANTED);
            textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                }
            });
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
           // cameraSource.release();
        }
//----------------------------------Detect Text-----------------------------------------------------------------------------------------------------
        private void textRecognizer(){
            textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setRequestedPreviewSize(1280, 1024)
                    .build();

            surfaceView = findViewById(R.id.surfaceView);
            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @SuppressLint("MissingPermission")
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });


            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    SparseArray<TextBlock> sparseArray = detections.getDetectedItems();
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 0; i<sparseArray.size(); ++i){
                        TextBlock textBlock = sparseArray.valueAt(i);
                        if (textBlock != null && textBlock.getValue() !=null){
                            stringBuilder.append(textBlock.getValue() + " ");
                        }
                    }

                    final String stringText = stringBuilder.toString();

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            stringResult = stringText;
                            resultObtained();
                        }
                    });
                }
            });
        }
//----------------------Display Text and  Convert to speech-------------------------------------------------------------------------------------

        private void resultObtained(){

            setContentView(R.layout.activity_speech);
            textView = findViewById(R.id.textView);
            textView.setMovementMethod(new ScrollingMovementMethod());
            if(!stringResult.isEmpty()){
            textView.setVisibility(View.VISIBLE);
            textView.setText(stringResult);

            //textViewTranslatedText.setText(stringResult);
            checkTextView=1;
            textToSpeech.speak(stringResult, TextToSpeech.QUEUE_FLUSH, null, null);
            identifyLanguage();

            //Getting the instance of Spinner and applying OnItemSelectedListener on it
            spin = (Spinner) findViewById(R.id.spinner);
            spin.setOnItemSelectedListener(this);

            //Creating the ArrayAdapter instance having the bank name list
            ArrayAdapter aa = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,languages); //my_selected_item
            aa.setDropDownViewResource(R.layout.dropdownitem);

            //Setting the ArrayAdapter data on the Spinner
            spin.setAdapter(aa);

            displaytext=findViewById(R.id.textView4);

            displaytext.setVisibility(View.VISIBLE);

            spin.setVisibility(View.VISIBLE);}
            else{
                Toast.makeText(getApplicationContext(),"TRY AGAIN NOTHING CAN BE READ! ",Toast.LENGTH_SHORT).show();
            }

        }
//--------------------------------Read Button-------------------------------------------------------------------------------------------------------
        public void Read(View view){
            setContentView(R.layout.surfaceview);
            for(int i=0;i<100;i++)
            {
                for(int j=0;j<100;j++){

                }
            }
            textRecognizer();
        }



//-----------------------------------Translation Button-------------------------------------------------------------------------------------------
        public void translation(View view){
            if(checkTextView==1) {

                //translateText();
            }
            else{
                Toast.makeText(getApplicationContext(),"Text view is empty ",Toast.LENGTH_LONG).show();
            }

        }
//-----------------------------------------------Language Identify-------------------------------------------------------------------------------
        public void identifyLanguage()
        {
            sourceLanguage=findViewById(R.id.textView2);
            String src=stringResult;
            LanguageIdentifier languageIdentifier =
                    LanguageIdentification.getClient();
            languageIdentifier.identifyLanguage(src)
                    .addOnSuccessListener(
                            new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(@Nullable String languageCode) {
                                    if (languageCode.equals("und")) {

                                        //Toast.makeText(getApplicationContext(),"Trying... ",Toast.LENGTH_LONG).show();
                                        sourceLanguage.setVisibility(View.VISIBLE);
                                        sourceLanguage.setText("Language not detected");

                                    } else {
                                        Log.i(TAG, "Language: " + languageCode);
                                        Locale loc = new Locale(languageCode);
                                         languageName = loc.getDisplayLanguage(loc);
                                       // Toast.makeText(getApplicationContext(),"Language: "+languageName,Toast.LENGTH_LONG).show();
                                        sourceLanguage.setVisibility(View.VISIBLE);
                                        sourceLanguage.setText("Language detected is "+languageName);
                                    }
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Sorry couldn't understand ",Toast.LENGTH_SHORT).show();
                                }
                            });



        }



//--------------------------------------------------Translation-----------------------------------------------------------------------------------------
        private void translateText(int i)
        {



            textViewTranslatedText=findViewById(R.id.textView3);
            textViewTranslatedText.setMovementMethod(new ScrollingMovementMethod());



            if(i==1) {
                //-----------------------English -Bengali Translator--------------------------------
                // Create an  translator:
                //MODEL FOR ENGLISH TO BENGALI
                TranslatorOptions options =
                        new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.BENGALI)
                                .build();
                final Translator englishBengaliTranslator =
                        Translation.getClient(options);

                //DOWNLOAD THE MODEL
                DownloadConditions conditions = new DownloadConditions.Builder()
                        .requireWifi()
                        .build();
                englishBengaliTranslator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener(
                                new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {

                                    }

                                    //@Override
                                    public void onSuccess(Void v) {
                                        // Model downloaded successfully. Okay to start translating.
                                        // (Set a flag, unhide the translation UI, etc.)
                                        Toast.makeText(getApplicationContext(), "Model Downloaded.Translation will start ", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Model couldn’t be downloaded or other internal error.
                                        // ...
                                        Toast.makeText(getApplicationContext(), "Model could not be Downloaded. ", Toast.LENGTH_SHORT).show();
                                    }
                                });



                englishBengaliTranslator.translate(stringResult)
                        .addOnSuccessListener(
                                new OnSuccessListener<String>() {


                                    @Override
                                    public void onSuccess(@NonNull String translatedText1) {
                                        if(translatedText1!=null) {
                                            textViewTranslatedText.setVisibility(View.VISIBLE);
                                            textViewTranslatedText.setText(translatedText1);


                                        }

                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error.
                                        // ...
                                        Toast.makeText(getApplicationContext(), "Text could not be translated. Model Downloading", Toast.LENGTH_LONG).show();
                                    }
                                });


            }
            //-----------------------English -Hindi Translator---------------------------------------------------------------------
            if(i==5) {
                // Create an  translator:
                //MODEL FOR ENGLISH TO BENGALI
                TranslatorOptions options =
                        new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.HINDI)
                                .build();
                final Translator englishHindiTranslator =
                        Translation.getClient(options);

                //DOWNLOAD THE MODEL
                DownloadConditions conditions = new DownloadConditions.Builder()
                        .requireWifi()
                        .build();
                englishHindiTranslator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener(
                                new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {

                                    }

                                    //@Override
                                    public void onSuccess(Void v) {
                                        // Model downloaded successfully. Okay to start translating.
                                        // (Set a flag, unhide the translation UI, etc.)
                                        Toast.makeText(getApplicationContext(), "Model Downloaded.Translation will start ", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Model couldn’t be downloaded or other internal error.
                                        // ...
                                        Toast.makeText(getApplicationContext(), "Model could not be Downloaded. ", Toast.LENGTH_SHORT).show();
                                    }
                                });



                englishHindiTranslator.translate(stringResult)
                        .addOnSuccessListener(
                                new OnSuccessListener<String>() {


                                    @Override
                                    public void onSuccess(@NonNull String translatedText1) {
                                        if(translatedText1!=null) {
                                            textViewTranslatedText.setVisibility(View.VISIBLE);
                                            textViewTranslatedText.setText(translatedText1);
                                        }

                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error.
                                        // ...
                                        Toast.makeText(getApplicationContext(), "Text could not be translated. ", Toast.LENGTH_LONG).show();
                                    }
                                });


            }
            //------------------------English -Kannada Translator-------------------------------------------------------------------
            if(i==3){

                TranslatorOptions options =
                        new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.KANNADA)
                                .build();
                final Translator englishKannadaTranslator =
                        Translation.getClient(options);

                //DOWNLOAD THE MODEL
                DownloadConditions conditions = new DownloadConditions.Builder()
                        .requireWifi()
                        .build();
                englishKannadaTranslator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener(
                                new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {

                                    }

                                    //@Override
                                    public void onSuccess(Void v) {
                                        // Model downloaded successfully. Okay to start translating.
                                        // (Set a flag, unhide the translation UI, etc.)
                                        Toast.makeText(getApplicationContext(), "Model Downloaded.Translation will start ", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Model couldn’t be downloaded or other internal error.
                                        // ...
                                        Toast.makeText(getApplicationContext(), "Model could not be Downloaded. ", Toast.LENGTH_SHORT).show();
                                    }
                                });


                englishKannadaTranslator.translate(stringResult)
                        .addOnSuccessListener(
                                new OnSuccessListener<String>() {


                                    @Override
                                    public void onSuccess(@NonNull String translatedText1) {
                                        textViewTranslatedText.setVisibility(View.VISIBLE);
                                        textViewTranslatedText.setText(translatedText1);
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error.
                                        // ...

                                    }
                                });





            }

            //-----------------------English -Marathi Translator--------------------------------

            if(i==4) {
                // Create an  translator:
                //MODEL FOR ENGLISH TO MARATHI
                TranslatorOptions options =
                        new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.MARATHI)
                                .build();
                final Translator englishMarathiTranslator =
                        Translation.getClient(options);

                //DOWNLOAD THE MODEL
                DownloadConditions conditions = new DownloadConditions.Builder()
                        .requireWifi()
                        .build();
                englishMarathiTranslator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener(
                                new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {

                                    }

                                    //@Override
                                    public void onSuccess(Void v) {
                                        // Model downloaded successfully. Okay to start translating.
                                        // (Set a flag, unhide the translation UI, etc.)
                                        Toast.makeText(getApplicationContext(), "Model Downloaded.Translation will start ", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Model couldn’t be downloaded or other internal error.
                                        // ...
                                        Toast.makeText(getApplicationContext(), "Model could not be Downloaded. ", Toast.LENGTH_SHORT).show();
                                    }
                                });



                englishMarathiTranslator.translate(stringResult)
                        .addOnSuccessListener(
                                new OnSuccessListener<String>() {


                                    @Override
                                    public void onSuccess(@NonNull String translatedText1) {
                                        if(translatedText1!=null) {
                                            textViewTranslatedText.setVisibility(View.VISIBLE);
                                            textViewTranslatedText.setText(translatedText1);
                                        }

                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error.
                                        // ...
                                        Toast.makeText(getApplicationContext(), "Text could not be translated. ", Toast.LENGTH_LONG).show();
                                    }
                                });


            }

                              //--------------------------English -Urdu Translator--------------------------------
            if(i==2) {
                // Create an  translator:
                //MODEL FOR ENGLISH TO URDU
                TranslatorOptions options =
                        new TranslatorOptions.Builder()
                                .setSourceLanguage(TranslateLanguage.ENGLISH)
                                .setTargetLanguage(TranslateLanguage.URDU)
                                .build();
                final Translator englishUrduTranslator =
                        Translation.getClient(options);

                //DOWNLOAD THE MODEL
                DownloadConditions conditions = new DownloadConditions.Builder()
                        .requireWifi()
                        .build();
                englishUrduTranslator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener(
                                new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {

                                    }

                                    //@Override
                                    public void onSuccess(Void v) {
                                        // Model downloaded successfully. Okay to start translating.
                                        // (Set a flag, unhide the translation UI, etc.)
                                        Toast.makeText(getApplicationContext(), "Model Downloaded.Translation will start ", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Model couldn’t be downloaded or other internal error.
                                        // ...
                                        Toast.makeText(getApplicationContext(), "Model could not be Downloaded. ", Toast.LENGTH_SHORT).show();
                                    }
                                });



                englishUrduTranslator.translate(stringResult)
                        .addOnSuccessListener(
                                new OnSuccessListener<String>() {


                                    @Override
                                    public void onSuccess(@NonNull String translatedText1) {
                                        if(translatedText1!=null) {
                                            textViewTranslatedText.setVisibility(View.VISIBLE);
                                            textViewTranslatedText.setText(translatedText1);
                                        }

                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error.
                                        // ...
                                        Toast.makeText(getApplicationContext(), "Text could not be translated. ", Toast.LENGTH_LONG).show();
                                    }
                                });


            }



        }

//-------------------------------------------------Spinner Methods----------------------------------------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            if(i!=0 && stringResult!=null) {
                translateText(i);
            }
            else if(stringResult==null)
            {
                Toast.makeText(getApplicationContext(),"Source Text is empty ",Toast.LENGTH_LONG).show();

            }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}