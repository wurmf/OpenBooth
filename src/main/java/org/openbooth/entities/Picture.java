package org.openbooth.entities;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Picture picture = (Picture) o;

        if (id != picture.id) return false;
        return path.equals(picture.path);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + path.hashCode();
        return result;
    }
}
