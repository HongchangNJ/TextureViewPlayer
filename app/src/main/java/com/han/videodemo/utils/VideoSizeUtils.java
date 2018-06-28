package com.han.videodemo.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import com.han.videodemo.model.Size;

/**
 * created by HanHongchang
 * 2018/6/28
 */
public class VideoSizeUtils {

    public static Size caculateViewSize(Context context, @Nullable int videoWidth, @Nullable int videoHeight) {
        int screenWidth = ScreenUtils.getScreenWidth(context);
        int screenHeight = ScreenUtils.getScreenHeight(context);

        float screenRation = ((float) screenWidth) / screenHeight;
        float videoRation = ((float) videoWidth) / videoHeight;

        int viewWidth = 0;
        int viewHeight = 0;

        if (videoRation > screenRation) {
            viewWidth = screenWidth;
            viewHeight = videoHeight * viewWidth / videoWidth;
        } else if (videoRation < screenRation) {
            viewHeight = screenHeight;
            viewWidth = viewHeight * videoWidth / videoHeight;
        } else {
            viewWidth = screenWidth;
            viewHeight = screenHeight;
        }

        return new Size(viewWidth, viewHeight);
    }

}
