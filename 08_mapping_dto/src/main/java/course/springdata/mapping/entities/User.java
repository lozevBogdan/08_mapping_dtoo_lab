package course.springdata.mapping.entities;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String fullName;
    private String password;
    private String email;
    private Set<Game> games;
    private Role roles;

    public User() {

    }


    @Column(name = "full_name",nullable = false)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    @Column(name = "password",nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Column(name = "email",unique = true,nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ManyToMany(fetch = FetchType.EAGER,targetEntity = Game.class)
    @JoinTable(
            name = "users_games",
    joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "game_id",referencedColumnName = "id"))
    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    @Enumerated(STRING)
    public Role getRoles() {
        return roles;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }
}
