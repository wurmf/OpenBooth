package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Abstract picture entity
 */
public abstract class Picture {
    private int id;
    private String path;

    public Picture(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public Picture(String path) {
        this(Integer.MIN_VALUE,path);
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

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", path='" + path + '\'' +
                '}';
    }
}