package com.hector.luis.spaceapp2016.earthlivelh.Helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hector.luis.spaceapp2016.earthlivelh.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hector Arredondo on 23/04/2016.
 */
public class Utils {
    private final static String TAG = Utils.class.getSimpleName();

    public static Drawable getDrawableById(Context context, int idResource) {
        Drawable myDrawable;
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            myDrawable = context.getResources().getDrawable(idResource, context.getTheme());
        } else {
            myDrawable = context.getResources().getDrawable(idResource);
        }

        return myDrawable;
    }

    public static Uri storeImage(Context context, Bitmap image) {
        Uri lUri = null;
        File pictureFile = getOutputMediaFile(context);

        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());

            Toast.makeText(context, R.string.err_create_file, Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 75, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
            Toast.makeText(context, R.string.err_file_not_found, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
            Toast.makeText(context, R.string.err_access_dained, Toast.LENGTH_SHORT).show();
        }

        lUri = Uri.fromFile(pictureFile);
        return lUri;
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(Context context){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public static void sharedImg(Context context, Uri uriF) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/jpeg");

        sharingIntent.putExtra(Intent.EXTRA_STREAM, uriF);
        context.startActivity(sharingIntent);
    }
}
