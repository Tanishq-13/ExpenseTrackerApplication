package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class UserInfo {
    @Id
    @Column(name="user_id")
    private String userId;
    private String username;
    private String password;

    // Getters and Setters
    @Setter
    @Getter
    @Column(nullable = false)
    private boolean enabled = true; // Ensure this is included

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<UserRole> roles=new HashSet<>();


}
