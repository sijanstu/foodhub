package com.foodhub.foodhub.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "menu_items")
public class MenuItem implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String itemImage;
    private String item3DModelURL;
    private String itemName;

    private String description;

    private Double price;

    @Column()
    private Long restaurantId;

    public boolean validate() {
        return !itemName.isEmpty() && !itemName.isBlank() && !description.isEmpty() && !description.isBlank() && !price.isNaN() && !price.isInfinite();
    }
}