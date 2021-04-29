package com.amr.braintraining;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Handler;

import java.util.Random;

//------------------------------------------------------------------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity {
    int correctAnsLocation;
    TextView quesTv;
    TextView tv0;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    LinearLayout blackBg;
    LinearLayout downLo;
    LinearLayout introBg;
    TextView finalScore;
    TextView timerTv;
    ImageView introImg;
    ImageView iconScore;

    Button btnStart;
    Button btnReset;
    Button btnIntro;
    TextView scoreTv;
    SeekBar timerSeekBar;

    Button btnPlayAgain;
    CountDownTimer countDownTimer;
    int score = 0;
    int numOfQues = 0;
    boolean choiceActive = false;
    boolean btnStartResetActive = true;
    boolean lastSecond = false;

    //---------------------------------------------------------------------------------------------------------------------------------------------------**
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defineAllViews();
        changeSeekBar();
        introAnimation();
    }


    //------------------------------------------ ALL FUNCTION ------------------------------------------------------------------------------------------
    public void generateQues() {

        int num1 = setRandom(0, 30);
        int num2 = setRandom(0, 15);
        int correctAns = num1 + num2;

        int[] choice = {0, 0, 0, 0}; //generate 4 different numbers in the array
        choice[0] = generateUnduplicated(choice[0], choice[1], choice[2], choice[3], correctAns);
        choice[1] = generateUnduplicated(choice[1], choice[0], choice[2], choice[3], correctAns);
        choice[2] = generateUnduplicated(choice[2], choice[0], choice[1], choice[3], correctAns);
        choice[3] = generateUnduplicated(choice[3], choice[0], choice[1], choice[3], correctAns);

        //put the correct answer in a random position
        correctAnsLocation = setRandom(0, 3);
        choice[correctAnsLocation] = correctAns;

        quesTv.setText(Integer.toString(num1) + " + " + Integer.toString(num2));
        tv0.setText(Integer.toString(choice[0]));
        tv1.setText(Integer.toString(choice[1]));
        tv2.setText(Integer.toString(choice[2]));
        tv3.setText(Integer.toString(choice[3]));
        //you have to clear them in order not to compare with the previous values
        choice[0] = 0;
        choice[1] = 0;
        choice[2] = 0;
        choice[3] = 0;
    }


    //------------------------------------------------------------------------------------------------------------
    public void checkAns(final View v) {

        if (choiceActive && lastSecond == false) {
            // the scoreMessage make choiceIsActive=false , but if user click in the last second it make choiceIsActive=true after 800ms
            //as there is 800ms after pressing  choice ,so you have to disable that when timer is <2second
            choiceActive = false;
            if (v.getTag().equals(Integer.toString(correctAnsLocation))) {
                v.setBackgroundResource(R.drawable.truebg);
                playSound(R.raw.truesound);
                score++;
                numOfQues++;

            } else {
                v.setBackgroundResource(R.drawable.falsebg);
                playSound(R.raw.falsesound);
                numOfQues++;
            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {//execute the following after these  seconds
                    v.setBackgroundResource(R.drawable.choices);
                    generateQues();
                    choiceActive = true;
                    scoreTv.setText("score: " + Integer.toString(score) + "/" + Integer.toString(numOfQues));
                }
            }, 800);

            //focus :it must be here in order not to generate new question
        }

    }
//------------------------------------------------------------------------------------------------------------


    public void changeSeekBar() {

        timerSeekBar.setMax(99);// number in second
        timerSeekBar.setProgress(30);
        timerTv.setText("30s");

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                timerTv.setText(Integer.toString(progress) + "s");
                if (progress > 10) {
                    timerTv.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    timerTv.setTextColor(Color.parseColor("#FF0000"));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    //------------------------------------------------------------------------------------------------------------*
    public void startTimer() {
        countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000 + 100, 1000) {
            @Override
            public void onTick(long secondUntillFinish) {
                if ((int) secondUntillFinish < 10000) {
                    if ((int) secondUntillFinish < 1100) {
                        lastSecond = true;
                    }
                    timerTv.setTextColor(Color.parseColor("#FF0000"));
                    timerTv.setText(Integer.toString((int) secondUntillFinish / 1000) + "s");
                } else {
                    timerTv.setText(Integer.toString((int) secondUntillFinish / 1000) + "s");
                }
            }

            @Override
            public void onFinish() {
                scoreMessage();
                timerTv.setText("0s");

            }
        }.start();
    }
//------------------------------------------------------------------------------------------------------------**

    public void introAnimation() {
        introImg.setVisibility(View.VISIBLE);
        btnIntro.setVisibility(View.VISIBLE);
        introImg.setScaleX(.3f);
        introImg.setScaleY(0.3f);

        btnIntro.setTranslationX(-900);
        introImg.animate().scaleXBy(0.7f).scaleYBy(0.7f).alpha(1).setDuration(3501);
        btnIntro.animate().translationXBy(900).alpha(1).setDuration(3500);


    }

    //------------------------------  BUTTONS  ------------------------------------------------------------------------------
    public void btnAfterIntro(View v) {
        introBg.animate().translationXBy(-900).alpha(0).setDuration(2000);
        timerTv.setTextColor(Color.parseColor("#FFFFFF"));

    }

    public void btnPlayAgain(View v) {

        btnStart.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.INVISIBLE);
        blackBg.animate().alpha(0f).setDuration(1400);
        iconScore.animate().alpha(0f).setDuration(1400);
        downLo.animate().translationYBy(-1000f).setDuration(1500);
        btnStart.animate().alpha(1).setDuration(700);
        btnReset.animate().alpha(1).setDuration(700);

        btnStartResetActive = true;
        timerSeekBar.setEnabled(true);
        resetAll();

    }

    //------------------------------------------
    public void btnStart(View v) {

        if (btnStartResetActive && timerSeekBar.getProgress() > 0) {
            startTimer();
            generateQues();

            choiceActive = true;
            lastSecond = false;
            timerSeekBar.setEnabled(false);

            btnStart.setVisibility(View.INVISIBLE);
            btnReset.setVisibility(View.VISIBLE);
        }
    }

    //-------------------------------------------
    public void btnReset(View v) {
        if (btnStartResetActive) {
            btnStart.setVisibility(View.VISIBLE);
            choiceActive = false;
            lastSecond = false;
            timerSeekBar.setEnabled(true);
            resetAll();

        }
    }

    //------------------------------------------------------------------------------------------------------------*
    public void playSound(int b) {

        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), b);
        mediaPlayer.start();
    }

    //------------------------------------------------------------------------------------------------------------*
    public int generateUnduplicated(int ch0, int ch1, int ch2, int ch3, int correctANS) {
//if the generated number = equal any of others >>generate new one
        while (ch0 == ch1 || ch0 == ch2 || ch0 == ch3 || ch0 == correctANS) {
            ch0 = setRandom(0, 20);
        }
        return ch0;
    }

    //------------------------------------------------------------------------------------------------------------*
    private int setRandom(int min, int max) {
        //get number from 0 to (min-max) then added to min>> so all will be between max and min
        int random = (new Random()).nextInt((max - min) + 1) + min;
        return random;
    }

    //------------------------------------------------------------------------------------------------------------*
    public void scoreMessage() {

        downLo.setVisibility(View.VISIBLE);
        downLo.setTranslationY(-900f);
        blackBg.setVisibility(View.VISIBLE);
        blackBg.animate().alpha(0.8f).setDuration(1500);
        iconScore.setVisibility(View.VISIBLE);
        iconScore.animate().alpha(1f).setDuration(1500);
        downLo.animate().translationYBy(900f).setDuration(1700);
        btnStart.animate().alpha(0.2f).setDuration(700);
        btnReset.animate().alpha(0.2f).setDuration(700);
        finalScore.setText("your score: " + Integer.toString(score) + "/" + Integer.toString(numOfQues));
        choiceActive = false;
        btnStartResetActive = false;
        playSound(R.raw.score_sound);

    }
//---------------------------------------------------------------------------------------------------------------*

    public void resetAll() {
        countDownTimer.cancel();
        timerSeekBar.setProgress(30);
        timerTv.setText("30s");
        score = 0;
        numOfQues = 0;
        scoreTv.setText("score: 0/0");
        quesTv.setText("? + ?");
        tv0.setText("?");
        tv1.setText("?");
        tv2.setText("?");
        tv3.setText("?");

    }

    //---------------------------------------------------------------------------------------------------------------*
    public void defineAllViews() {
        timerTv = findViewById(R.id.timerTv);
        tv0 = findViewById(R.id.tv0);
        tv0.setText("?");
        tv1 = findViewById(R.id.tv1);
        tv1.setText("?");
        tv2 = findViewById(R.id.tv2);
        tv2.setText("?");
        tv3 = findViewById(R.id.tv3);
        tv3.setText("?");
        scoreTv = findViewById(R.id.scoreTv);
        quesTv = findViewById(R.id.quesTv);
        quesTv.setText("? + ?");
        blackBg = findViewById(R.id.blackBg);
        iconScore = findViewById(R.id.iconScore);

        downLo = findViewById(R.id.downLayout);
        finalScore = findViewById(R.id.finalScore);
        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);
        timerSeekBar = findViewById(R.id.timerSb);
        btnIntro = findViewById(R.id.btnIntro);
        introBg = findViewById(R.id.introBg);
        introImg = findViewById(R.id.intoImg);
    }
}
