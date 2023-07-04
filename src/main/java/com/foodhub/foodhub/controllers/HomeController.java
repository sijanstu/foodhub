package com.foodhub.foodhub.controllers;

import com.foodhub.foodhub.entities.Cuisine;
import com.foodhub.foodhub.entities.User;
import com.foodhub.foodhub.services.AlgorithmService;
import com.foodhub.foodhub.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class HomeController {
    private final UserService userService;
    private final AlgorithmService algorithmService;

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("user", new User());
        return "index";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        return "home";
    }
    @RequestMapping("/restaurants")
    public String restaurants(Model model) {
        return "restaurants";
    }

    //login
    @RequestMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "login";
    }

    //signup
    @RequestMapping("/signup")
    public String signup(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "signup";
    }

    //menu
    @RequestMapping("/menu")
    public String menu(Model model) {
        return "menu";
    }

    //login
    @PostMapping("/api/users/login")
    public String loginAPI(@ModelAttribute User user, Model model) {
        model.addAttribute("user", user);
        if (userService.login(user, model)) {
            return "redirect:/login?token=" + model.getAttribute("token");
        }
        return "redirect:/login?usernameError=" + model.getAttribute("usernameError") + "&passwordError=" + model.getAttribute("passwordError");
    }

    //signup
    @PostMapping("/api/users/signup")
    //form data
    public String signupAPI(@ModelAttribute User user, Model model) {
        System.out.println(user);
        model.addAttribute("user", user);
        if (userService.signup(user, model)) {
            return "redirect:/signup?token=" + model.getAttribute("token");
        }
        return "redirect:/signup?usernameError=" + model.getAttribute("usernameError") + "&emailError=" + model.getAttribute("emailError") + "&passwordError=" + model.getAttribute("passwordError" + "&phoneNumberError=" + model.getAttribute("phoneNumberError"));
    }

    @GetMapping("/api/users/togglePreference/{token}/{preference}")
    public ResponseEntity<?> togglePreference(@PathVariable String token, @PathVariable int preference) {
        Cuisine cuisine = Cuisine.values()[preference - 1];
        return userService.togglePreference(token, cuisine);
    }

    //get user by token
    @RequestMapping("/api/users/getUser/{token}")
    public ResponseEntity<?> getUser(@PathVariable String token) {
        return ResponseEntity.ok(userService.getUserFromToken(token));
    }

    //preferences
    @RequestMapping("/preferences")
    public String preferences(Model model) {
        return "preferences";
    }

    //getRestaurants/27.698565760592/85.30775964260103
    @RequestMapping("/getRestaurants/{lat}/{lng}/{token}")
    public ResponseEntity<?> getRestaurants(@PathVariable("lat") double lat, @PathVariable("lng") double lng, @PathVariable("token") String token) {
        User user = userService.getUserFromToken(token).getBody();
        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
        user.setLatitude(lat);
        user.setLongitude(lng);
        return ResponseEntity.ok(algorithmService.getRecommendedRestaurants(user, 10));
    }
}
