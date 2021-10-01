package com.deviget.domain;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_auth")
public class UserAuth implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;
    private String username;
    private String password;
    private boolean blocked;

    public  UserAuth(){
    }

    public UserAuth(String username, String password, boolean blocked) {
        this.username = username;
        this.password = password;
        this.blocked = blocked;
    }

    public Long getId() {
        return id_user;
    }

    public void setId(Long id) {
        this.id_user = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}