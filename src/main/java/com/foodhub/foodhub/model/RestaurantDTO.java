package com.foodhub.foodhub.model;

import lombok.Data;

@Data
public class RestaurantDTO {
    private String name;
    private String description;
    private String address;
    private String phoneNumber;
    private String email;
    private String website;
    private String image;
    private String menuImage;
    private String menu3DModelURL;
    private String menuName;
    private String menuDescription;
    private Double menuPrice;
}
