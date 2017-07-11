package com.example.android.portugueseapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RemoteControlClient;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.bluetooth.BluetoothClass.Service.AUDIO;
import static android.media.AudioManager.AUDIOFOCUS_LOSS;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;


public class Numbers extends Fragment {
    //Handles play back of all sound file
    private MediaPlayer mMediaPlayer;

    //audio focus when playing sound file
    private AudioManager mAudioManager;

    //handles the Audio Focus happening within the phone
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        //pause playback
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        //resume playback
                        mMediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaPlayer();
                    }

                }
            };

    //this listener is trigger when media has completed playing audio
    private MediaPlayer.OnCompletionListener mCompletionListener = (new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    });

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_list, container, false);

        // create and setup the audio manager to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("Um", "One", R.drawable.one, R.raw.one));
        words.add(new Word("Dois", "Two", R.drawable.two, R.raw.two));
        words.add(new Word("Três", "Three", R.drawable.three, R.raw.three));
        words.add(new Word("Quatro", "Four", R.drawable.four, R.raw.four));
        words.add(new Word("Cinco", "Five", R.drawable.five, R.raw.five));
        words.add(new Word("Seis", "Six", R.drawable.six, R.raw.six));
        words.add(new Word("Sete", "Seven", R.drawable.seven, R.raw.seven));
        words.add(new Word("Oito", "Eight", R.drawable.eight, R.raw.eight));
        words.add(new Word("Nove", "Nine", R.drawable.nine, R.raw.nine));
        words.add(new Word("Dez", "Ten", R.drawable.ten, R.raw.ten));
        words.add(new Word("Dez", "Ten", R.drawable.ten, R.raw.ten));


        WordAdapter wordAdapter = new WordAdapter(Numbers.this.getActivity(), words);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(wordAdapter);

        //release media player if it currently exists because we are about to play a different sound
        releaseMediaPlayer();


        //creating music player
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);

                //start playback;
                //request audio focus
                int results = mAudioManager.requestAudioFocus(mOnAudioFocusListener,
                        //use the music stream
                        AudioManager.STREAM_MUSIC,
                        //request permanent focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (results == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // validate if a term doesn't have a not voice

                    //Create and setup the MediaPlayer for the audio resource associate with the current  word object
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getmAudioResource());

                    //Start the audio file
                    mMediaPlayer.start();

                    //to release the memory when the music  has finished playing
                    //Setup  a listener on the media player, so that we can stop and release the
                    //media player once the sounds has finished playing
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);

                }
            }
        });

        return rootView;
    }


    public void onStop() {
        super.onStop();

        //when the activity is stopped, release the media play
        releaseMediaPlayer();
    }

    //clean up the media player by releasing its resource
    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;

            //Abondon focus when play back is complete
            mAudioManager.abandonAudioFocus(mOnAudioFocusListener);
        }
    }
}



