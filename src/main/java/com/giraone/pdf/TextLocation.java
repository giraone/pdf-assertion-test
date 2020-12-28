package com.giraone.pdf;

public class TextLocation {

    final String text;
    final int page;
    final float x;
    final float y;
    final float width;
    final float height;

    public TextLocation(String text, int page, float x, float y, float width, float height) {
        this.text = text;
        this.page = page;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public int getPage() {
        return page;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }


    public float getRightLowerX() {
        return x + width;
    }

    public float getRightLowerY() {
        return y + height;
    }

    public String printPos() {
        return String.format("[%5.1f, %5.1f] - [%5.1f, %5.1f]", getX(), getY(), getRightLowerX(), getRightLowerY());
    }


}
