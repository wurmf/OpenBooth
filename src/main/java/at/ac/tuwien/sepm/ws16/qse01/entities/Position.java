package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Position entity
 */
public class Position {
    private long id;
    private String name;
    private boolean isDeleted;

    /**
     * Constructor with known ID
     *
     * @param id - positive position id
     * @param name - no empty position name
     * @param isDeleted - mark this position with true value as deleted
     */
    public Position(long id, String name, boolean isDeleted){
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;

    }

    /**
     * Constructor with unknown id will set id to Long.MIN_VALUE and persistence layer has
     * to provide a valid id
     *
     * @param name - no empty position name
     */
    public Position(String name){
        this(Long.MIN_VALUE, name,false);
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
