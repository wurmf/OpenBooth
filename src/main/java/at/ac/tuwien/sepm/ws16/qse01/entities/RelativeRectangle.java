package at.ac.tuwien.sepm.ws16.qse01.entities;

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
                && this.getHeight() == ((RelativeRectangle) object).getHeight();
    }
}
