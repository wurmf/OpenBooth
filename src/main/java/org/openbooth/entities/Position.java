package org.openbooth.entities;

/**
 * Position entity
 */
public class Position {
    private int id;
    private String name;
    private String buttonImagePath;
    private boolean isDeleted;

    /**
     * Constructor with unknown ID
     *
     * @param name - no empty position name
     */
    public Position(String name){
        this.id = Integer.MIN_VALUE;
        this.name = name;
        this.isDeleted = false;
    }

    /**
     * Constructor with known ID
     *
     * @param id - positive position id
     * @param name - no empty position name
     * @param isDeleted - mark this position with true value as deleted
     */
    public Position(int id, String name, String buttonImagePath, boolean isDeleted){
        this.id = id;
        this.name = name;
        this.buttonImagePath = buttonImagePath;
        this.isDeleted = isDeleted;
    }

    /**
     * Constructor with unknown id will set id to Long.MIN_VALUE and persistence layer has
     * to provide a valid id
     *
     * @param name - no empty position name
     */
    public Position(String name, String buttonImagePath){
        this(Integer.MIN_VALUE, name,buttonImagePath, false);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getButtonImagePath() {return buttonImagePath;}

    public void setButtonImagePath(String buttonImagePath) {this.buttonImagePath = buttonImagePath;}

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object object){
        return object instanceof Position
                && ((Position) object).getId() == this.getId()
                && ((Position) object).getName().equals(this.getName())
                && ((Position) object).getButtonImagePath().equals(this.getButtonImagePath())
                && ((Position) object).isDeleted() ==  this.isDeleted();
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (buttonImagePath != null ? buttonImagePath.hashCode() : 0);
        result = 31 * result + (isDeleted ? 1 : 0);
        return result;
    }

    @Override
    public String toString(){
        return this.getName();
    }

    public String toStringDetails()
    { return "Id:"+id+"Name:"+name+"Path:"+buttonImagePath;}
}