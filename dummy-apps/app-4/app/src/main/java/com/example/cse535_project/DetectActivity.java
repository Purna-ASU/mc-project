package com.example.cse535_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;

@SuppressWarnings("ALL")
public class DetectActivity{
    // Add local IP address here in SERVER_URL
    public static final String SERVER_URL = "http://172.20.10.9:80";

    ImageView imageView;

    protected float[] onCreate(Bundle savedInstanceState) {
        DigitsDetector digitsDetector = new DigitsDetector(imageView.getContext());
        Bitmap photo = Bitmap.createBitmap(28, 28, Bitmap.Config.ARGB_8888);
        return digitsDetector.detectDigit(photo);
    }
}