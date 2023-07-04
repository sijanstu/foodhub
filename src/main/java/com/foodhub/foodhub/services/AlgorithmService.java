package com.foodhub.foodhub.services;

import com.foodhub.foodhub.entities.Cuisine;
import com.foodhub.foodhub.entities.Restaurant;
import com.foodhub.foodhub.entities.User;
import com.foodhub.foodhub.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AlgorithmService {
    private final RestaurantService restaurantService;
    private final UserRepository userService;

    public ResponseEntity<?> getRecommendedRestaurants(Double latitude, Double longitude, Long userId, int k) {
        User user = userService.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        user.setLatitude(latitude);
        user.setLongitude(longitude);
        return ResponseEntity.ok(getRecommendedRestaurants(user, k));
    }

    public List<Restaurant> getRecommendedRestaurants(User user, int k) {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return getRecommendedRestaurants(user, restaurants, k);
    }

    public List<Restaurant> getRecommendedRestaurants(User user, List<Restaurant> restaurants, int k) {
        List<Restaurant> recommendedRestaurants = new ArrayList<>();
        // Get restaurants that match user's cuisine preferences
        List<Restaurant> matchingRestaurants = restaurants.stream()
                .filter(restaurant -> user.getUserPreference().contains(restaurant.getCuisine()))
                .collect(Collectors.toList());

        // Calculate distances between user and each restaurant
        Map<Restaurant, Double> distances = new HashMap<>();
        for (Restaurant restaurant : matchingRestaurants) {
            double distance = Math.sqrt(Math.pow(user.getLatitude() - restaurant.getLatitude(), 2) +
                    Math.pow(user.getLongitude() - restaurant.getLongitude(), 2));
            distances.put(restaurant, distance);
            double distanceInKm = distance * 111.12;
            restaurant.setDistance(Math.round(distanceInKm * 100.0) / 100.0);
        }

        // Sort restaurants by distance
        List<Map.Entry<Restaurant, Double>> sortedDistances = distances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        // Use KNN to get top k closest restaurants
        List<Restaurant> topKClosest = new ArrayList<>();
        for (int i = 0; i < k && i < sortedDistances.size(); i++) {
            topKClosest.add(sortedDistances.get(i).getKey());
        }

        // Use Cosine similarity to calculate similarity between user and each restaurant
        Map<Restaurant, Double> similarities = new HashMap<>();
        for (Restaurant restaurant : topKClosest) {
            double cosineSimilarity = calculateCosineSimilarity(user.getUserPreference(), restaurant.getCuisine());
            similarities.put(restaurant, cosineSimilarity);
            restaurant.setCosineSimilarity(cosineSimilarity);
        }

        // Sort restaurants by similarity
        List<Map.Entry<Restaurant, Double>> sortedSimilarities = similarities.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // Get recommended restaurants
        for (Map.Entry<Restaurant, Double> entry : sortedSimilarities) {
            recommendedRestaurants.add(entry.getKey());
        }

        return recommendedRestaurants;
    }

    // Helper function to calculate Cosine similarity between user preference and restaurant cuisine
    private double calculateCosineSimilarity(List<Cuisine> userPreference, Cuisine restaurantCuisine) {
        int dotProduct = 0;
        int userPreferenceMagnitude = userPreference.size();
        int restaurantCuisineMagnitude = 1;

        for (Cuisine cuisine : userPreference) {
            if (cuisine.equals(restaurantCuisine)) {
                dotProduct++;
            }
        }

        return dotProduct / (Math.sqrt(userPreferenceMagnitude) * Math.sqrt(restaurantCuisineMagnitude));
    }

    public ResponseEntity<?> getRestaurantsInRadius(double latitude, double longitude, double radius) {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        List<Restaurant> restaurantsInRadius = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {
            double distance = Math.sqrt(Math.pow(latitude - restaurant.getLatitude(), 2) +
                    Math.pow(longitude - restaurant.getLongitude(), 2));
            if (distance <= radius) {
                restaurantsInRadius.add(restaurant);
            }
        }
        return ResponseEntity.ok(restaurantsInRadius);
    }

    public ResponseEntity<?> getRestaurantsNearby(double latitude, double longitude, double n) {
        //n number of restaurants available most near to the user
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        List<Restaurant> restaurantsNearby = new ArrayList<>();

        //find the distant between the user and the restaurants
        Map<Restaurant, Double> distances = new HashMap<>();
        for (Restaurant restaurant : restaurants) {
            double distance = Math.sqrt(Math.pow(latitude - restaurant.getLatitude(), 2) +
                    Math.pow(longitude - restaurant.getLongitude(), 2));
            distances.put(restaurant, distance);
        }

        //sort the restaurants by distance
        List<Map.Entry<Restaurant, Double>> sortedDistances = distances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        //get the n closest restaurants maximum n
        for (int i = 0; i < n && i < sortedDistances.size(); i++) {
            restaurantsNearby.add(sortedDistances.get(i).getKey());
        }

        return ResponseEntity.ok(restaurantsNearby);
    }
}