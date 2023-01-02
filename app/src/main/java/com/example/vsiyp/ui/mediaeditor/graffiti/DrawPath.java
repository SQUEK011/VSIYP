package com.example.vsiyp.ui.mediaeditor.graffiti;

import android.graphics.Paint;
import android.graphics.Path;

public class DrawPath {
    public Path path;

    public Paint paint;

    public Position position;

    public GraffitiInfo.Shape shape = GraffitiInfo.Shape.LINE;

    public static class Position {
        public float startX;

        public float startY;

        public float endX;

        public float endY;
    }
}
