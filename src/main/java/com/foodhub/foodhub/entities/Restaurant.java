package com.foodhub.foodhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "cuisine")
    private Cuisine cuisine;

    @Transient
    private List<MenuItem> menuItems = new ArrayList<>();

    @Transient
    private double cosineSimilarity;

    @Transient
    private double distance;

    public boolean validate() {
        return !name.isEmpty() && !name.isBlank() && !description.isEmpty() && !description.isBlank() && !address.isEmpty() && !address.isBlank() && !phoneNumber.isEmpty() && !phoneNumber.isBlank();
    }
}



