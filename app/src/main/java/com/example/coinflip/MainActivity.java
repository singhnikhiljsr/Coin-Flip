package com.example.coinflip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ObjectAnimator animator;
    ImageView coin;
    TextView result;
    Button toss;
    SpeechRecognizer speechRecognizer;
    Intent intentRecognizer;
    Handler handler;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);
        coin = (ImageView)findViewById(R.id.coinImageView) ;
        result = findViewById(R.id.textView);
        toss = findViewById(R.id.toss_button);
        progressBar = findViewById(R.id.progressbar);
        animator= ObjectAnimator.ofFloat(coin,"rotationY",0.0f,360f);
        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> voice = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String string = "";
                if(voice!=null){
                    string = voice.get(0);
                    Log.i("tag",string);
                    if(string.equals("heads") || string.equals("head") || string.contains("d") || string.contains("h") || string.equals("ats")){
                        handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                                resultTails();
                            }
                        },1500);
                    }
                    else{
                        handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                                resultHeads();
                            }
                        },1500);

                    }
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    public void flip(View view){
        animator.setDuration(3000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
        result.setVisibility(View.INVISIBLE);
        speechRecognizer.startListening(intentRecognizer);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void resultHeads(){
        coin.setImageResource(R.drawable.ic_heads_24);
        result.setTextColor(getResources().getColor(R.color.red));
        result.setText("Heads");
        result.setVisibility(View.VISIBLE);
        animator.cancel();
        speechRecognizer.stopListening();
    }
    public void resultTails(){
        coin.setImageResource(R.drawable.ic_tails_24);
        result.setTextColor(getResources().getColor(R.color.blue));
        result.setText("Tails");
        result.setVisibility(View.VISIBLE);
        animator.cancel();
        speechRecognizer.stopListening();
    }
}