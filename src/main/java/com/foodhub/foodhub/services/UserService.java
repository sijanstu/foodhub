package com.foodhub.foodhub.services;

import com.foodhub.foodhub.entities.Cuisine;
import com.foodhub.foodhub.entities.User;
import com.foodhub.foodhub.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;

import java.util.Base64;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AlgorithmService algorithmService;

    //signup
    public boolean signup(User user, Model model) {
        //check if email is valid
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("emailError", "Email is not valid");
            return false;
        }
        //check if password is valid
        if (user.getPassword().length() < 8) {
            model.addAttribute("passwordError", "Password must be at least 8 characters");
            return false;
        }
        //phone number is optional
        if (!user.getPhoneNumber().isEmpty() && !user.getPhoneNumber().matches("^[0-9]{10}$")) {
            model.addAttribute("phoneNumberError", "Phone number is not valid");
            return false;
        }
        //check if user already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            model.addAttribute("emailError", "User Email already exists");
            return false;
        }
        //encrypt password to md5
        user.setPassword(new Md5PasswordEncoder().encode(user.getPassword()));
        //save user
        userRepository.save(user);
        model.addAttribute("token", generateToken(user));
        return true;
    }

    public boolean login(User user, Model model) {
        //check if user exists
        User user1 = userRepository.findByEmail(user.getEmail());
        if (user1 == null) {
            model.addAttribute("usernameError", "User does not exist");
            return false;
        }
        //check if password is correct
        if (!new Md5PasswordEncoder().matches(user.getPassword(), user1.getPassword())) {
            model.addAttribute("passwordError", "Password is incorrect");
            return false;
        }
        //generate token
        String token = generateToken(user1);
        model.addAttribute("token", token);
        return true;
    }

    public ResponseEntity<?> getUser(Long userId) {
        return ResponseEntity.ok(userRepository.findById(userId));
    }

    public ResponseEntity<?> updateUser(User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    public ResponseEntity<User> getUserFromToken(String token) {
        try {
            String decodedToken = checkToken(token);
            if (decodedToken == null) {
                return ResponseEntity.badRequest().build();
            }
            User user = userRepository.findById(Long.parseLong(decodedToken)).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    String generateToken(User user) {
        return Base64.getEncoder().encodeToString((user.getId() + "-" + System.currentTimeMillis()).getBytes());
    }


    private String checkToken(String token) {
        String decodedToken = new String(Base64.getDecoder().decode(token));
        String userID=decodedToken.substring(0, decodedToken.indexOf("-"));
        String time=decodedToken.substring(decodedToken.indexOf("-")+1);
        //check if logged in within last 7 days
        if (System.currentTimeMillis() - Long.parseLong(time) > 604800000) {
            return null;
        }
        return userID;
    }

    public String getRestaurants(double lat, double lng, String token, Model model) {
        User user = getUserFromToken(token).getBody();
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("restaurants", algorithmService.getRecommendedRestaurants(user, 10));
        return "restaurants";
    }

    public ResponseEntity<?> togglePreference(String token, Cuisine cuisine) {
        User user = getUserFromToken(token).getBody();
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        if (user.getUserPreference().contains(cuisine)) {
            user.getUserPreference().remove(cuisine);
        } else {
            user.getUserPreference().add(cuisine);
        }
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    private static class Md5PasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encodedPassword.equals(encode(rawPassword));
        }

        @Override
        public boolean upgradeEncoding(String encodedPassword) {
            return PasswordEncoder.super.upgradeEncoding(encodedPassword);
        }
    }

//    public static void main(String[] args) {
//        UserService userService = new UserService(null);
//        User user = new User();
//        user.setId(9999L);
//        String token = userService.generateToken(user);
//        System.out.println(token);
//        System.out.println(userService.checkToken(token));
//    }
}
