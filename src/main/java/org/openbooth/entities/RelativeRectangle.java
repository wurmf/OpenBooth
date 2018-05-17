package org.openbooth.entities;

/**
 * Entity Relative Rectangle
 * Inspired by java.awt.Rectangle Class
 * It is intended to be used to define independent of the screen resolution a rectangle-shaped sub area
 * at the same relative position and same relative size relative to the total screen
 */
public class RelativeRectangle {
    private double x;
    private double y;
    private double width;
    private double height;


    public RelativeRectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public boolean equals(Object object){
        return object instanceof RelativeRectangle
                && this.x == ((RelativeRectangle) object).getX()
                && this.y == ((RelativeRectangle) object).getY()
                && this.width == ((RelativeRectangle) object).getWidth()
                && this.height == ((RelativeRectangle) object).getHeight();
    }

    @Override
    public String toString() {
        return "RelativeRectangle{" +
                "x=" + x +
                ", y=" + y +
                ", height=" + height +
                ", width=" + width +
                '}';
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(width);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(height);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}