package at.ac.tuwien.sepm.ws16.qse01.entities;

/**
 * Representation of an admin-user with username and password.
 */
public class AdminUser {
    private String adminName;
    private byte[] password;

    public AdminUser(String adminName, byte[] password) {
        this.adminName = adminName;
        this.password = password;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }
}
