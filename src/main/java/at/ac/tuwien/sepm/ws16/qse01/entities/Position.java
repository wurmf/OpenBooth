package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Position entity
 */
public class Position {
    private long id;
    private String name;
    private String buttonImagePath;
    private boolean isDeleted;

    /**
     * Constructor with known ID
     *
     * @param id - positive position id
     * @param name - no empty position name
     * @param isDeleted - mark this position with true value as deleted
     */
    public Position(long id, String name, String buttonImagePath, boolean isDeleted){
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
        this.buttonImagePath = buttonImagePath;
    }

    /**
     * Constructor with unknown id will set id to Long.MIN_VALUE and persistence layer has
     * to provide a valid id
     *
     * @param name - no empty position name
     */
    public Position(String name, String buttonImagePath){
        this(Long.MIN_VALUE, buttonImagePath, name,false);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
