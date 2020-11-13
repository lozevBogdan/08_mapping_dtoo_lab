package course.springdata.mapping.dto;

import course.springdata.mapping.entities.Role;

public class UserDto {

    private String fullName;
    private String email;
    private Role role;

    public UserDto() {
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
