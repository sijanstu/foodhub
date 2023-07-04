package com.foodhub.foodhub.services;

import com.foodhub.foodhub.entities.MenuItem;
import com.foodhub.foodhub.entities.Restaurant;
import com.foodhub.foodhub.repositories.MenuItemRepository;
import com.foodhub.foodhub.repositories.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RestaurantService {
    private RestaurantRepository restaurantRepository;
    private MenuItemRepository menuItemRepository;
    public List<Restaurant> restaurants = new ArrayList<>();

    //get all restaurants
    public List<Restaurant> getAllRestaurants() {
        if (restaurants.isEmpty()) {
            restaurants = restaurantRepository.findAll();
        }
        List<MenuItem> menuItems = menuItemRepository.findAll();
        for (Restaurant restaurant : restaurants) {
            for (MenuItem menuItem : menuItems) {
                if (Objects.equals(menuItem.getRestaurantId(), restaurant.getId())) {
                    restaurant.getMenuItems().add(menuItem);
                }
            }
        }
        return restaurants;
    }
}
