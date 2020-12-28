package com.giraone.pdf;

public class PointAdaption {

    final float x;
    final float y;

    public PointAdaption(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "PointAdaption{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}
