package com.example.vsiyp.ui.mediaeditor.graffiti;

public class GraffitiInfo {
    public int stokeWidth;

    public int stokeColor;

    public int stokeAlpha;

    public Shape shape = Shape.LINE;

    public TYPE type = TYPE.DEFAULT;

    public int visible;

    public enum TYPE {
        DEFAULT,
        COLOR,
        WIDTH,
        ALPHA,
        SHAPE,
        SAVE
    }

    public enum Shape {
        LINE,
        RECTANGLE,
        CIRCLE,
        TRIANGLE,
        STRACTLINE,
        FIVESTAR,
        DIAMOND,
        ARROW
    }
}

