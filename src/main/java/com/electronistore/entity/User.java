package com.electronistore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="user")
public class User implements UserDetails {

    //Role - "NORMAL", "ADMIN"
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;

    @Column(name="user_name")
    private String name;

    @Column(name="email", unique = true)
    private String email;

    @Column(name="user_password")
    private String password;

    private String gender;

    @Column(length = 1000)
    private String about;

    @Column(name="user_image_name")
    private String imageName;

    // For Order two-way cascading
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();


    // Mapping User table with Role Table
    @JsonIgnoreProperties("roles")
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();


    private Providers providers = Providers.SELF;

    /**
     *
     * @return These all are the UserDetails Interface methods
     */

    //Important
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());
        return authorities;
    }

    // Important
    @Override
    public String getUsername() {
        return this.email;
    }

    // Important
    @Override
    public String getPassword(){
        return this.password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}



