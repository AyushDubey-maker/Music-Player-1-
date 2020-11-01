package com.example.musicplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

   static MediaPlayer mediaPlayer;
    AudioManager audioManager;
    boolean isSongRunning;
    ImageButton playNext,playPrev;
    int currentIndex=0;
    TextView song_Title;
    ImageView songView;
    //Implementing play button
    public void play(View view) {
        mediaPlayer.start();
        SongName();
    }
  //Implementing pause Button
    public void pause(View view) {
        mediaPlayer.pause();
        SongName();
    }





    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
         //Implementing maxVol and curVol to get Maximum Volume and Streaming Volume.
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curvVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        song_Title=findViewById(R.id.song_title);
        playNext=findViewById(R.id.playNext_button);
        playPrev=findViewById(R.id.playPrev_button);
        songView=findViewById(R.id.song_view);

        //Initializing arrayList which will store music at particular indexes
         final ArrayList<Integer> songs=new ArrayList<>();

        songs.add(0,R.raw.song);
        songs.add(1,R.raw.dopalkizindagi);
        songs.add(2,R.raw.songsample);

        mediaPlayer=MediaPlayer.create(getApplicationContext(),songs.get(currentIndex));

       //When you click play Next Button
       playNext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(currentIndex<songs.size()-1){
                   currentIndex++;
               }else{
                   currentIndex=0;
               }
               if(mediaPlayer.isPlaying()){
                   mediaPlayer.stop();
               }
               mediaPlayer=MediaPlayer.create(getApplicationContext(),songs.get(currentIndex));
               mediaPlayer.start();
               SongName();
           }
       });

       //When you click previous button
     playPrev.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(currentIndex>0){
                 currentIndex--;
             }else {
                 currentIndex=songs.size()-1;
             }
             if(mediaPlayer.isPlaying()){
                 mediaPlayer.stop();
             }
             mediaPlayer=MediaPlayer.create(getApplicationContext(),songs.get(currentIndex));
             mediaPlayer.start();
             SongName();
         }
     });




   //Code to implement seekBar that can change volume for song..
        SeekBar seekvol = findViewById(R.id.seekVol);
        seekvol.setMax(maxVol);
        seekvol.setProgress(curvVol);
        seekvol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //Code to implement seekbar that will show you the progress of your current song.
        final SeekBar seekProg = findViewById(R.id.seekProg);
        seekProg.setMax(mediaPlayer.getDuration());
        //Timer object which will change the seekbar per duration time.
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                isSongRunning = true;
                seekProg.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 1000);
        seekProg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!isSongRunning) {
                    mediaPlayer.seekTo(progress);
                } else {
                    isSongRunning = !isSongRunning;
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

    //Function SongName which will provide Title and Song Image.
private void SongName(){
        if(currentIndex==0){
         song_Title.setText("Hum-Dum SuniyoRe");
         songView.setImageResource(R.drawable.humdum);
        }
        if(currentIndex==1){
            song_Title.setText("Do Pal Ki Zindagi Hai");
            songView.setImageResource(R.drawable.scam);
        }
        if(currentIndex==2){
            song_Title.setText("Song Sample");
            songView.setImageResource(R.mipmap.hey_1);
        }
}



    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.pause();

    }

}