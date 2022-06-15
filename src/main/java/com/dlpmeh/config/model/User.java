package com.dlpmeh.config.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Represents a user with profile details and relationships to tweets, likes,
 * and verification status.
 */
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Unique username for the user
    private String username;

    private String fullName;
    private String location;
    private String website;
    private String dateOfBirth;
    private String email;
    private String password;
    private String moblileNumber;
    private String image;
    private String backgroundImage;
    private String bio;

    // Indicates if the user is verified
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Indicates if the user is verified
    @Column(name = "login_at")
    private LocalDateTime loginAt;

    // private boolean req_user;
    @Column(name = "req_user")
    private boolean requestedUser;

    @Column(name = "login_with_google", nullable = false)
    private boolean loginWithGoogle;

    // One-to-many relationship: One user can have many tweets
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Tweet> userTweets = new ArrayList<>();

    // One-to-many relationship: One user can have many likes
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> userLikes = new ArrayList<>();

    // Embedded object: Verification details are stored in the same table
    @Embedded
    private Verification verification;

    // Many-to-many relationship: A user can follow many users
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_following_users", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
    private List<User> followingUsers = new ArrayList<>();

    // Users who follow this user
    @JsonIgnore
    @ManyToMany(mappedBy = "followingUsers")
    private List<User> followers = new ArrayList<>();

}
