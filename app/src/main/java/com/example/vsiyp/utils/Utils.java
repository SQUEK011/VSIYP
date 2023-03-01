package com.example.vsiyp.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.example.vsiyp.ui.common.utils.FileUtil;

import java.io.IOException;


public class Utils {
    private static final String TAG = "Utils";

    public interface ThumbnailCallback {
        void onBitmap(Bitmap bitmap);

        void onSuccess();

        void onFail(String errorCode, String errMsg);
    }

    public static boolean isMateX(Activity context) {
        Point p = new Point();
        if (context.getWindowManager() != null && context.getWindowManager().getDefaultDisplay() != null) {
            context.getWindowManager().getDefaultDisplay().getSize(p);
        }
        Log.i(TAG, "isMateX: " + p.x + "y:" + p.y);
        return p.x > 2000;
    }

    public static void getThumbnails(String path, long spaceTime, int width, int height, ThumbnailCallback callback) {
        new Thread(() -> {
            Thread.currentThread().setName("UtilThumbnail");
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            try {
                media.setDataSource(path);
                String durationStr = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                if (durationStr == null) {
                    if (callback != null) {
                        callback.onFail("1", "Illegal Video");
                    }
                    return;
                }
                long duration = Long.parseLong(durationStr);
                long time = 0L;
                while (time < duration) {
                    Bitmap bitmap = media.getFrameAtTime(time * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    Bitmap result = scaleAndCutBitmap(bitmap, width, height);
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                    if (callback != null) {
                        callback.onBitmap(result);
                    }
                    time = time + spaceTime;
                }
                if (callback != null) {
                    callback.onSuccess();
                }
            } catch (IllegalArgumentException e) {
                SmartLog.e(TAG, "getThumbnail error");
                if (callback != null) {
                    callback.onFail("IllegalArgumentException", "");
                }
            } finally {
                try {
                    media.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static Bitmap scaleAndCutBitmap(Bitmap bitmap, int width, int height) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        double scale = getScale(bitmap.getWidth(), bitmap.getHeight(), width, height);

        matrix.postScale((float) scale, (float) scale);
        matrix.postRotate(0);
        Bitmap bitmap1;
        int space;
        if (bitmap.getHeight() > bitmap.getWidth()) {
            space = (bitmap.getHeight() - bitmap.getWidth()) / 2;
            bitmap1 = Bitmap.createBitmap(bitmap, 0, space, bitmap.getWidth(), bitmap.getWidth(), matrix, true);
        } else {
            space = (bitmap.getWidth() - bitmap.getHeight()) / 2;
            bitmap1 = Bitmap.createBitmap(bitmap, space, 0, bitmap.getHeight(), bitmap.getHeight(), matrix, true);
        }
        return bitmap1;
    }

    private static double getScale(int oriWidth, int oriHeight, int targetWidth, int targetHeight) {
        double scale;
        if (oriHeight <= oriWidth) {
            scale = 1.0 * targetHeight / oriHeight;
        } else {
            scale = 1.0 * targetWidth / oriWidth;
        }

        return scale > 1 ? 1 : scale;
    }

    public static boolean isVideoByPath(String filePath) {
        return FileUtil.isVideo(filePath);
    }
}
