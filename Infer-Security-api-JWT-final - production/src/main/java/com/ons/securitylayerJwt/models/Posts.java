package com.ons.securitylayerJwt.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Table(name="posts")
public class Posts implements Serializable {
    @Id
    private String pid;

    private String title;
    private String author;
    private String content;
    private String link;



    @ManyToOne
    @JoinColumn(name = "user_id")
    //@JsonIgnore
    @JsonBackReference
    private User user; // Foreign key referencing the User table


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostRating> ratings = new ArrayList<>();


    // New field for post notes
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();


}
