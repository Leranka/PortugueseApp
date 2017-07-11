package com.example.android.portugueseapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;


public class Months extends Fragment {
    //handles playback of sounds
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_list, container, false);

        // create and setup the audio manager to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("Janeiro", "January", R.raw.jan));
        words.add(new Word("Fevereiro", "February", R.raw.feb));
        words.add(new Word("Março", "March", R.raw.mar));
        words.add(new Word("Abril", "April", R.raw.apr));
        words.add(new Word("pode", "May", R.raw.may));
        words.add(new Word("Junho", "June", R.raw.june));
        words.add(new Word("Julho", "July", R.raw.july));
        words.add(new Word("Agosto", "August", R.raw.aug));
        words.add(new Word("Setembro", "September", R.raw.sept));
        words.add(new Word("Outubro", "October", R.raw.oct));
        words.add(new Word("Novembro", "November", R.raw.nov));
        words.add(new Word("Dezembro", "December", R.raw.dec));
        words.add(new Word("Dezembro", "December", R.raw.dec));


        WordAdapter wordAdapter = new WordAdapter(getActivity(), words);

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

    @Override
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
            //Abandon focus when play back is complete
            mAudioManager.abandonAudioFocus(mOnAudioFocusListener);
        }
    }


}