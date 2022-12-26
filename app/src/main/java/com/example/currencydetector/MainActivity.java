package com.example.currencydetector;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.ml.vision.FirebaseVision;
//import com.google.firebase.ml.vision.common.FirebaseVisionImage;
//import com.google.firebase.ml.vision.text.FirebaseVisionText;
//import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class MainActivity extends AppCompatActivity {

    private Button txtBtn, currencyBtn;
    ImageView imageView;
    int flag;
    private final int REQ_CODE = 100;
    private Button replaybtn;
    TextToSpeech tts;
    private Bitmap imageBitmap;
    Intent intent;

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
//                    t1.speak("result code is " + result.getResultCode(), TextToSpeech.QUEUE_ADD, null, null);
//                    t1.speak("result code is idk",TextToSpeech.QUEUE_ADD,null,null);
                    if (result.getResultCode() == RESULT_OK) {
//                        t1.speak("inside activity result",TextToSpeech.QUEUE_ADD,null,null);
//                        Bundle extras = result.getData().getExtras();
////                                        result.getData().getExtras()
//                        imageBitmap = (Bitmap) extras.get("data");
//                        imageView.setImageBitmap(imageBitmap);
//                        detecttxt();
                        intent = result.getData();
//                        t1.speak("got Data "+intent,TextToSpeech.QUEUE_ADD,null,null);
                        Bundle extras = intent.getExtras();
//                        t1.speak("got extras "+extras,TextToSpeech.QUEUE_ADD,null,null);
                        imageBitmap = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(imageBitmap);
//                        t1.speak("imagaview set "+extras,TextToSpeech.QUEUE_ADD,null,null);
                        detectTxt(imageBitmap);
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // speech recognition
        /* intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
        try {
            startActivityForResult(intent, REQ_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry your device not supported",
                    Toast.LENGTH_SHORT).show();
        }*/
        // end of speech recognition
        currencyBtn = findViewById(R.id.button2);
        currencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak("opening currency detector", TextToSpeech.QUEUE_ADD, null, null);
                flag = 3;
                dispatchTakePictureIntent();
            }
        });

        txtBtn = findViewById(R.id.button3);
        imageView = findViewById(R.id.imageView2);
        txtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak("opening text reader", TextToSpeech.QUEUE_ADD, null, null);
                flag = 4;
                dispatchTakePictureIntent();
            }
        });

        Button timeDateButton = findViewById(R.id.button4);
        tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });
        timeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                if (hour == 0) {
                    hour = 12;
                }
                int minutes = calendar.get(Calendar.MINUTE);
                String currentTime = "Time is" + String.valueOf(hour) + DateUtils.getAMPMString(calendar.get(Calendar.AM_PM)) + String.valueOf(minutes) + "minutes";
                String today = "Today date is" + new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault()).format(new Date());
                tts.speak(currentTime, TextToSpeech.QUEUE_ADD, null, null);
                if (true)
                    tts.speak(today, TextToSpeech.QUEUE_ADD, null, null);
            }
        });
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra("android.intent.extra.quickCapture", true);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityIfNeeded(takePictureIntent,REQUEST_IMAGE_CAPTURE);
//            ActivityResultContracts.StartActivityForResult
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE,null);
//            startActivityForResultAsUser(takePictureIntent, REQUEST_IMAGE_CAPTURE,null);
//            t1.speak("inside if",TextToSpeech.QUEUE_ADD,null,null);

            startForResult.launch(takePictureIntent);
        }


    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//            detecttxt();
//        }
//    }
        /*switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result.contains("open text reader")) {
                        t1.speak("opening text reader", TextToSpeech.QUEUE_ADD, null);

                        dispatchTakePictureIntent();
                        flag = 4;
                    } else if (result.contains("open currency detector")) {
                        t1.speak("opening currency detector", TextToSpeech.QUEUE_ADD, null);

                        dispatchTakePictureIntent();
                        flag = 3;
                    } else if (result.contains("replay")) {
                        detecttxt();
                    } else if (result.contains("time")) {
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR);
                        if (hour == 0) {
                            hour = 12;
                        }
                        int minutes = calendar.get(Calendar.MINUTE);
                        String currenttime = "Time is" + String.valueOf(hour) + DateUtils.getAMPMString(calendar.get(Calendar.AM_PM)) + String.valueOf(minutes) + "minutes";
                        String today = "Today date is" + new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault()).format(new Date());
                        t1.speak(currenttime, TextToSpeech.QUEUE_ADD, null);
                        if (true)
                            t1.speak(today, TextToSpeech.QUEUE_ADD, null);
                    }
                }
                break;
            }

        }
        try {
            startActivityForResult(intent, REQ_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry your device not supported",
                    Toast.LENGTH_SHORT).show();
        }*/
//    }
    public void detectTxt(Bitmap imageBitmap) {
        InputImage image = InputImage.fromBitmap(imageBitmap, 0);
        TextRecognizer textRecognizer = TextRecognition.getClient();

        Task<Text> text = textRecognizer.process(image);
        text.addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                processResultText(text);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tts.speak("Text recognition Failed",TextToSpeech.QUEUE_ADD,null,null);
            }
        });


//        t1.speak("inside detecttxt",TextToSpeech.QUEUE_ADD,null,null);
//        FirebaseVisionImage img=FirebaseVisionImage.fromBitmap(imageBitmap);
////        FirebaseVisionTextRecognizer detector= FirebaseVision.getInstance().getOnDeviceTextRecognizer();
//        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
//        detector.processImage(img).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
//            @Override
//            public void onSuccess(FirebaseVisionText firebaseVisionText) {
////                processtxt(firebaseVisionText);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(MainActivity.this, "Fail to detect the text from image..", Toast.LENGTH_SHORT);
//            }
//        });
    }

    private void processResultText(Text resultText) {
        List<Text.TextBlock> textBlocks = resultText.getTextBlocks();

        if (flag == 4) { // if text extractor button pressed
            if (textBlocks.size() == 0) {
                tts.speak("No text found", TextToSpeech.QUEUE_ADD, null, null);
                return;
            }
//            Text.TextBlock block
//            for (textBlocks: Text.TextBlock block) {
//                String s = block.getText();
//                t1.speak(s, TextToSpeech.QUEUE_ADD, null,null);
//
//            }
            for (Text.TextBlock textBlock : textBlocks) { // read each text element from each line from each textblock
                for (Text.Line line : textBlock.getLines()) {
                    for (Text.Element element : line.getElements()) {
                        tts.speak(element.getText(), TextToSpeech.QUEUE_ADD, null, null);
                    }
                }
            }
//            for (int i = 0; i < textBlocks.size(); i++) {
//                String text = textBlocks.get(i).getText().toString();
//                t1.speak(text, TextToSpeech.QUEUE_ADD, null, null);
//            }
        } else if (flag == 3) { //if currency detector button pressed
            String currency = "No text found";
            String text="";
            for (int i = 0; i < textBlocks.size(); i++)
                {
                     text += textBlocks.get(i).getText();
                }
                    if (text.contains("R2000")||text.contains("2000")) {
                        currency = "2000 rupees";
                    } else if (text.contains("R500")||text.contains("500")) {
                        currency = "500 rupees";
                    } else if (text.contains("R200")||text.contains("200")) {
                        currency = "200 rupees";
                    } else if (text.contains("R100") || text.equals("100")) {
                        currency = "100 rupees";
                    } else if (text.contains("R50") || text.equals("50")) {
                        currency = "50 rupees";
                    } else if (text.contains("R20") || text.equals("20")) {
                        currency = "20 rupees";
                    } else if (text.contains("R10") || text.equals("10")) {
                        currency = "10 rupees";
                    }

                tts.speak(currency, TextToSpeech.QUEUE_ADD, null, null);

            }
            replaybtn = findViewById(R.id.button5);
            replaybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detectTxt(imageBitmap);
                }
            });
        }
    }

//
//    private void processtxt(FirebaseVisionText Text)
//    {
//        List<FirebaseVisionText.TextBlock> blcks=Text.getTextBlocks();
//        t1.speak("inside processtxt", TextToSpeech.QUEUE_ADD, null,null);
//        if(flag==4)
//        {
//            if (blcks.size() == 0) {
//                t1.speak("No text found", TextToSpeech.QUEUE_ADD, null,null);
//                return;
//            }
//            for (FirebaseVisionText.TextBlock blck : Text.getTextBlocks()) {
//                String s = blck.getText();
//                t1.speak(s, TextToSpeech.QUEUE_ADD, null,null);
//
//            }
//        }
//        else if(flag==3)
//        { String s1="No text found";
//            for(FirebaseVisionText.TextBlock blck:Text.getTextBlocks())
//            {
//                String s=blck.getText();
//                if(s.contains("R2000"))
//                {
//                    s1="2000 rupees";
//                    break;
//                }
//                else if(s.contains("R500"))
//                {
//                    s1="500 rupees";
//                    break;
//                }
//                else if(s.contains("R200"))
//                {
//                    s1="200 rupees";
//                    break;
//                }
//                else if(s.contains("R100")||s.equals("100"))
//                {
//                    s1="100 rupees";
//                    break;
//                }
//                else if(s.contains("R50")||s.equals("50"))
//                {
//                    s1="50 rupees";
//                    break;
//                }
//                else if(s.contains("R20")||s.equals("20"))
//                {
//                    s1="20 rupees";
//                    break;
//                }
//                else if(s.contains("R10")||s.equals("10"))
//                {
//                    s1="10 rupees";
//                    break;
//                }
//            }
//            t1.speak(s1, TextToSpeech.QUEUE_ADD, null,null);
//
//        }
//        replaybtn=findViewById(R.id.button5);
//        replaybtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                detecttxt();
//            }
//        });
//    }
//}

        /*protected void onPause(){
            if (t1 != null) {
                t1.stop();
                t1.shutdown();
            }
            super.onPause();
        }*/


