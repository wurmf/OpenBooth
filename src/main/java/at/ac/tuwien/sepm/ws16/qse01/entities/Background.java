package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Background class(provides a picture for green screen)
 */
public class Background extends Picture{
    private String name;
    private Category category;
    private boolean isDeleted;

    public Background(int id,
                      String name,
                      String path,
                      Category category,
                      boolean isDeleted) {
        super(id, path);
        this.setName(name);
        this.setCategory(category);
        this.setDeleted(isDeleted);
    }

    public Background(String name,
                      String path,
                      Category category) {
        super(path);
        this.setName(name);
        this.setCategory(category);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object obj) {
        return  obj instanceof Background
                && ((Background) obj).getId() == this.getId();
    }

    @Override
    public String toString() {
        return "Background{" +
                "name='" + name + '\'' +
                ", category=" + category +
                '}';
    }

    /**
     * Category entity
     */
    public static class Category {
        private int id;
        private String name;
        private boolean isDeleted;

        public Category(int id, String name, boolean isDeleted) {
            this.id = id;
            this.name = name;
            this.isDeleted = isDeleted;
        }

        public Category(String name) {
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

        public boolean isDeleted() {
            return isDeleted;
        }

        public void setDeleted(boolean deleted) {
            isDeleted = deleted;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Category
                    && ((Category) obj).getId() == this.getId()
                    && ((Category) obj).getName().equals(this.getName())
                    && ((Category) obj).isDeleted() == this.isDeleted();
        }

        @Override
        public String toString() {
            return "Background.Category{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
