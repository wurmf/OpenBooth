package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Position entity
 */
public class Position {
    private int id;
    private String name;
    private boolean isDeleted;

    /**
     * Constructor with known ID
     *
     * @param id - positive profile id
     * @param name - no empty profile name
     * @param isDeleted - mark this profile with true value like deleted
     */
    public Position(int id, String name, boolean isDeleted){
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    /**
     * Constructor with unknown id will set id to Integer.MIN_VALUE and persistence layer has
     * to provide a valid id
     *
     * @param name - no empty position name
     */
    public Position(String name){
        this(Integer.MIN_VALUE, name,false);
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
