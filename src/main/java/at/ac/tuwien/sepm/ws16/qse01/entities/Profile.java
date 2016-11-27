package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Profile entity
 */
public class Profile {
    private int id;
    private String name;
    private boolean isActive;

    /**
     * Constructor with known ID
     *
     * @param id - positive profile id
     * @param name - no empty profile name
     */
    public Profile(int id, String name, boolean isActive){
        this.id = id;
        this.name = name;
        this.isActive = isActive;
    }

    /**
     * Constructor with unknown id will set id to Integer.MIN_VALUE and persistence layer has
     * to provide a valid id
     *
     * @param name - no empty profile name
     */
    public Profile(String name){
        this(Integer.MIN_VALUE,name,false);
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

    public boolean isActive() {return isActive;}

    public void setIsActive(boolean isActive) {this.isActive = isActive;}
}
