package com.jagtarsingh.bulletdistance;



import android.content.Context;
import android.graphics.Rect;

public class Square {

    private int xPosition;
    private int yPosition;
    private int width;
    Rect hitBox;

    public Square(Context context, int x, int y, int width) {
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;

        hitBox = new Rect(this.xPosition,
                            this.yPosition,
                        this.xPosition + this.width,
                    this.yPosition + this.width);
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rect hitBox) {
        this.hitBox = hitBox;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}

