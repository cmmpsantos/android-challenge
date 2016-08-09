/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.cmmps.android.widgets;

import android.widget.ImageView;

import android.util.AttributeSet;
import android.util.Log;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import android.os.AsyncTask;

/**
 *
 * @author csantos
 */
public class WebImageView extends ImageView {

    //private Bitmap bmImage;

    public WebImageView(Context context) {
        super(context);
    }

    public WebImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebImageView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
    }

    public void loadURL(String url) {
        new DownloadImageTask().execute(url);
    }

    private class DownloadImageTask  extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            InputStream inStream = null;

            try {
                inStream = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(inStream);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            finally {
                if (inStream!=null) {
                    try {
                        inStream.close();
                    }
                    catch (Exception e) {}
                }
            }

            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result!=null) {
                setImageBitmap(result);
            }
        }

    }

}
