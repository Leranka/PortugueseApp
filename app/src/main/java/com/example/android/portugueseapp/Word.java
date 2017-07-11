package com.example.android.portugueseapp;

/**
 * Created by Admin on 6/27/2017.
 */

public class Word {
    private String mDefaultTranslation;
    private String mPortugueseTranslation;
    private int mImageResourceId;
    private int mAudioResource;
//    private int mImageId;

    private static final int NO_IMAGE_PROVIDED = -1;

    public Word(String defaultTranslation, String portugueseTranslation, int audioResource) {
        this.mDefaultTranslation = defaultTranslation;
        this.mPortugueseTranslation = portugueseTranslation;
        this.mAudioResource = audioResource;
    }

    public Word(String defaultTranslation, String portugueseTranslation, int imageResource, int audioResource) {
        mDefaultTranslation = defaultTranslation;
        mPortugueseTranslation = portugueseTranslation;
        mImageResourceId = imageResource;
        mAudioResource = audioResource;
//        mImageId = imageId;
    }

    //get english translation
    public String getmDefaultTranslation() {
        return mDefaultTranslation;
    }

    //get portuguese translation
    public String getmPortugueseTranslation() {
        return mPortugueseTranslation;
    }

    //get image
    public int getmImageResourceId() {
        return mImageResourceId;
    }

    //get audio
    public int getmAudioResource() {
        return mAudioResource;
    }

    //get play button image
//    public int getmImageId(){return mImageId;}

    //return whether or not there is an image for this word
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}
