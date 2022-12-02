package com.example.cse535_project;

import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;

public class PreProcessImage extends AppCompatActivity {
    public static Bitmap greyscale(Bitmap colorPhoto) {
        int[] pixels = new int[28 * 28];
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(colorPhoto, 28, 28, false);
        resizedBitmap.getPixels(pixels, 0, 28, 0, 0, 28, 28);
        float[] retPixels = createInputPixels(pixels);

        int[] previewPixels = createPixelsPreview(pixels, retPixels);
        return Bitmap.createBitmap(previewPixels, 28, 28, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap[] splitBitmap(Bitmap picture) {
        Bitmap[] splitImages = new Bitmap[4];
        splitImages[0] = Bitmap.createBitmap(picture, 0, 0, picture.getWidth()/2 , picture.getHeight()/2);
        splitImages[1] = Bitmap.createBitmap(picture, picture.getWidth()/2, 0, picture.getWidth()/2, picture.getHeight()/2);
        splitImages[2] = Bitmap.createBitmap(picture,0, picture.getHeight()/2, picture.getWidth()/2,picture.getHeight()/2);
        splitImages[3] = Bitmap.createBitmap(picture, picture.getWidth()/2, picture.getHeight()/2, picture.getWidth()/2, picture.getHeight()/2);

        return splitImages;
    }

    private static int[] createPixelsPreview(int[] pixels, float[] retPixels) {
        int[] again = new int[pixels.length];
        for (int a = 0; a < pixels.length; a++) {
            again[a] = ColorConverter.tfToPixel(retPixels[a]);
        }
        return again;
    }

    private static float[] createInputPixels(int[] pixels) {
        return ColorConverter.convertToTfFormat(pixels);
    }
}
