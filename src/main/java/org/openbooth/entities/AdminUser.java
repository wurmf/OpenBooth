package org.openbooth.entities;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminUser adminUser = (AdminUser) o;
        return Objects.equals(adminName, adminUser.adminName) &&
                Arrays.equals(password, adminUser.password);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(adminName);
        result = 31 * result + Arrays.hashCode(password);
        return result;
    }
}
