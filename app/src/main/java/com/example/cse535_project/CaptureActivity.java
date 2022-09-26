package com.example.cse535_project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;

public class CaptureActivity extends AppCompatActivity {
    int CAMERA_PIC_REQUEST = 102;
    public Uri mImageCaptureUri;
    ImageView imv;
    String finalPath;
    String dropDownValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imv = (ImageView) findViewById(R.id.imageView);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_acitivity);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, CAMERA_PIC_REQUEST );
    }

    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == CAMERA_PIC_REQUEST) {

            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            // Set the image in imageview for display
            imv = (ImageView) findViewById(R.id.imageView);
            imv.setImageBitmap(photo);
            Log.i("CAPTURE", String.valueOf(mImageCaptureUri));
            String root = Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DCIM + "/";
            File myDir = new File(root);
            myDir.mkdirs();
            String fname = System.currentTimeMillis() +"_image.png";
            File file = new File(myDir, fname);
            Log.i("CAPTURE", String.valueOf(myDir));
            if (file.exists()) file.delete();
            Log.i("CAPTURE", root + fname);
            finalPath = root+fname;
            try {
                FileOutputStream out = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("CAPTURE", String.valueOf(e));
            }
        }
    }
    public void uploadToServer(View view) {
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        dropDownValue = spinner.getSelectedItem().toString();
        if (!dropDownValue.contains("Choose a category")) {
            ServerUpload();
            Intent intent = new Intent(CaptureActivity.this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(CaptureActivity.this, "Choose a value", Toast.LENGTH_SHORT).show();
        }
    }

    //     Method to upload file to server
    public void ServerUpload(){
        String node_server_url = "http://192.168.0.64:80/uploadFile";

        String serverFilePath = finalPath;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String fls1 = serverFilePath;
        File file = new File(fls1);
        Log.i("CAPTURE","Delta path new - "+fls1);

        RequestParams params1 = new RequestParams();

        params1.put("categoryvalue", dropDownValue);
        try {
            params1.put("choosefile", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        asyncHttpClient.post(CaptureActivity.this, node_server_url, params1, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast toast = Toast.makeText(getApplicationContext(), "Successfully upload", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error while uploading ", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}