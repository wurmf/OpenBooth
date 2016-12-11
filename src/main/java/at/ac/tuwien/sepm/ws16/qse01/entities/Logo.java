package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Entity Logo
 */
public class Logo {
    private int id;
    private String path;
    private boolean isDeleted;

    /**
     * Constructor with known ID
     *
     * @param id - positive logo id
     * @param path - no empty logo path
     * @param isDeleted - mark this logo with true value as deleted
     */
    public Logo(int id, String path, boolean isDeleted){
        this.id = id;
        this.path = path;
        this.isDeleted = isDeleted;

    }

    /**
     * Constructor with unknown id will set id to Integer.MIN_VALUE and persistence layer has
     * to provide a valid id
     *
     * @param path - no empty logo path
     */
    public Logo(String path){
        this(Integer.MIN_VALUE, path,false);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
