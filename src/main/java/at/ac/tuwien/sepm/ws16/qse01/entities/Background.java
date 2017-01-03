package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Background class(provides a picture for green screen)
 */
public class Background extends Picture{
    private String name;
    private AssociationType associationType;
    private int associationId;

    public Background(int id,
                      String path,
                      AssociationType associationType,
                      int associationId) {
        super(id, path);
        this.setAssociationType(associationType);
        this.setAssociationId(associationId);
    }

    public Background(String path,
                      AssociationType associationType,
                      int associationId) {
        super(path);
        this.setAssociationType(associationType);
        this.setAssociationId(associationId);
    }

    public Background(String path) {
        super(path);
        this.setAssociationType(AssociationType.GLOBAL);
        this.setAssociationId(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AssociationType getAssociationType() {
        return associationType;
    }

    public void setAssociationType(AssociationType associationType) {
        this.associationType = associationType;
    }

    public int getAssociationId() {
        return associationId;
    }

    public void setAssociationId(int associationId) {
        this.associationId = associationId;
    }

    @Override
    public String toString() {
        return "Background{" +
                "name='" + name + '\'' +
                ", associationType=" + associationType +
                ", associationId=" + associationId +
                '}';
    }

    /**
     * Association Types
     */
    public enum AssociationType {
        GLOBAL,PROFILE,EVENT
    }

    /**
     * Event entity
     */
    public class Event {
        private int id;
        private String name;

        public Event(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public Event(String name) {
            this(Integer.MIN_VALUE,name);
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

        @Override
        public String toString() {
            return "Event{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
