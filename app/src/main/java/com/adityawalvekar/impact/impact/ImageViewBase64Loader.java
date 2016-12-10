package com.adityawalvekar.impact.impact;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by vvvro on 8/10/2016.
 */
public class ImageViewBase64Loader {
    Context context;
    public ImageViewBase64Loader(Context mContext){
        context = mContext;
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String base64,
                                                         int reqWidth, int reqHeight) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        //BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //return BitmapFactory.decodeResource(res, resId, options);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public void loadBitmap(String base64, ImageView imageView) {
        /*BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(resId);*/
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(base64);
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;
        String base64 = "";

        public BitmapWorkerTask(ImageView circleImageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(circleImageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... strings) {
            base64 = strings[0];
            return decodeSampledBitmapFromResource(base64,200,200);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}

