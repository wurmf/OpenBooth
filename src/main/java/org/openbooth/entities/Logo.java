package org.openbooth.entities;

/**
 * Entity Logo
 */
public class Logo {
    private int id;
    private String label;
    private String path;
    private boolean isDeleted;

    /**
     * Constructor with known ID
     *
     * @param id - positive logo id
     * @param path - no empty logo path
     * @param label - no empty logo label
     * @param isDeleted - mark this logo with true value as deleted
     */

    public Logo(int id, String label, String path, boolean isDeleted){
        this.id = id;
        this.label = label;
        this.path = path;
        this.isDeleted = isDeleted;
    }

    /**
     * Constructor with unknown id will set id to Integer.MIN_VALUE and persistence layer has
     * to provide a valid id
     *
     * @param path - no empty logo path
     */
    public Logo(String label, String path){
        this(Integer.MIN_VALUE, label, path,false);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    @Override
    public boolean equals(Object object){
        return object instanceof Logo
                && ((Logo) object).getId() == this.getId()
                && ((Logo) object).getLabel().equals(this.getLabel())
                && ((Logo) object).getPath().equals(this.getPath())
                && ((Logo) object).isDeleted() == this.isDeleted();
    }

    @Override
    public String toString() {
        return "Logo{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", path='" + path + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + label.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + (isDeleted ? 1 : 0);
        return result;
    }
}
