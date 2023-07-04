package com.foodhub.foodhub.runner;

import com.foodhub.foodhub.entities.Cuisine;
import com.foodhub.foodhub.entities.MenuItem;
import com.foodhub.foodhub.entities.Restaurant;
import com.foodhub.foodhub.repositories.MenuItemRepository;
import com.foodhub.foodhub.repositories.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public void run(String... args) {

//        Restaurant restaurant1 = Restaurant.builder()
//                .menuItems(new ArrayList<>())
//                .name("Khaptad Restaurant").address("Kathmandu 44600").cuisine(Cuisine.NEPALI).image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTumEpcffisOM4vEOpiJ-8p0a3QOmLoZxoJE7fhzHOXF7UPfeSDKe1V_90JKqLOCIEV_PY&usqp=CAU").phoneNumber("984-8660025").description("Khaptad Restaurant is a Nepali restaurant located in Kathmandu, Nepal. It is a family owned restaurant that serves authentic Nepali food. The restaurant is open from 11:00 am to 10:00 pm.").latitude(27.684934545067488).longitude(85.29236580231077).build();
//        restaurant1 = restaurantRepository.save(restaurant1);
//        MenuItem menuItem1 = MenuItem.builder()
//                .itemName("Chicken Momos")
//                .price(200.0)
//                .restaurantId(restaurant1.getId())
//                .description("Made with chicken and vegetables")
//                .itemImage("https://www.archanaskitchen.com/images/archanaskitchen/1-Author/shaikh.khalid7-gmail.com/Chicken_Momos_Recipe_Delicious_Steamed_Chicken_Dumplings.jpg")
//                .item3DModelURL("https://raw.githubusercontent.com/sijanbhandari/gtlf-models/main/momo.glb").
//                build();
//        menuItemRepository.save(menuItem1);
////        MenuItem menuItem2 = MenuItem.builder()
////                .itemName("Khaja set")
////                .price(150.0)
////                .description("Khaja set is a set of 3 khajas, made with chicken and vegetables")
////                .itemImage("https://triplegend.com/wp-content/uploads/2022/03/sajan-rajbahak-tSujMB65cXQ-unsplash-scaled.jpg.webp").build();
////        menuItems1.add(menuItem2);
//        restaurantRepository.save(restaurant1);
    }
}
