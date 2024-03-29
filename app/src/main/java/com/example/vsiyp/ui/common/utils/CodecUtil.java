package com.example.vsiyp.ui.common.utils;

import android.graphics.Point;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.util.Range;

import com.huawei.hms.videoeditor.sdk.bean.HVEEncodeRange;
import com.huawei.hms.videoeditor.sdk.util.SmartLog;

import java.io.IOException;

public class CodecUtil {
    private static final String TAG = "CodecUtil";

    public static HVEEncodeRange getEncodeRange(String type) {
        HVEEncodeRange range = null;
        try {
            MediaCodec codec = MediaCodec.createEncoderByType(type);
            MediaCodecInfo info = codec.getCodecInfo();
            MediaCodecInfo.CodecCapabilities capabilities = info.getCapabilitiesForType(type);
            Range<Integer> width = capabilities.getVideoCapabilities().getSupportedWidths();
            Range<Integer> height = capabilities.getVideoCapabilities().getSupportedHeights();
            range = new HVEEncodeRange();
            range.setHeightRange(height);
            range.setWidthRange(width);
            codec.release();
        } catch (IOException | IllegalStateException | IllegalArgumentException | NullPointerException e) {
            SmartLog.e(TAG, "getEncodeRange Error = " + e.getMessage());
        }
        return range;
    }

    public static Point calculateEnCodeWH(int srcWidth, int srcHeight) {
        int minWidth = 0;
        int minHeight = 0;
        boolean rotate = false;
        if (srcHeight > srcWidth) {
            rotate = true;
            int tmp = srcWidth;
            srcWidth = srcHeight;
            srcHeight = tmp;
        }

        float scale = BigDecimalUtil.div(srcHeight, srcWidth);
        int maxWidth = Integer.MAX_VALUE;
        int maxHeight = Integer.MAX_VALUE;
        HVEEncodeRange range = CodecUtil.getEncodeRange(MediaFormat.MIMETYPE_VIDEO_HEVC);
        if (range != null) {
            if (range.getWidthRange() != null) {
                minWidth = range.getWidthRange().getLower();
                maxWidth = range.getWidthRange().getUpper();
            }
            if (range.getHeightRange() != null) {
                minHeight = range.getHeightRange().getLower();
                maxHeight = range.getHeightRange().getUpper();
            }
        }

        if (srcWidth > maxWidth) {
            srcWidth = maxWidth;
            SmartLog.w(TAG, "Wrong tracking src Width,Is Too Big");
        }
        if (srcWidth < minWidth) {
            srcWidth = minWidth;
            SmartLog.w(TAG, "Wrong tracking src Width,Is Too Small");
        }

        int preHeight = (int) BigDecimalUtil.mul(srcWidth, scale);

        if (minHeight < preHeight && preHeight < maxHeight) {
            srcHeight = preHeight;
        } else {
            if (srcHeight > maxHeight) {
                srcHeight = maxHeight;
                SmartLog.w(TAG, "Wrong tracking src Height,Is Too Big");
            }
            if (srcHeight < minHeight) {
                srcHeight = minHeight;
                SmartLog.w(TAG, "Wrong tracking src Height,Is Too Small");
            }
            int preWidth = (int) BigDecimalUtil.div(srcHeight, scale);

            if (minWidth < preWidth && preWidth < maxWidth) {
                srcWidth = (int) preWidth;
            }
        }
        int heightPercent = srcHeight % 4;
        int widthPercent = srcWidth % 4;
        if (heightPercent != 0) {
            srcHeight = srcHeight + (4 - heightPercent);
        }
        if (widthPercent != 0) {
            srcWidth = srcWidth + (4 - widthPercent);
        }

        if (rotate) {
            int tmp = srcWidth;
            srcWidth = srcHeight;
            srcHeight = tmp;
        }
        return new Point(srcWidth, srcHeight);
    }
}
