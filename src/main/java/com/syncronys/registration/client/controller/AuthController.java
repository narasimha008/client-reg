package com.syncronys.registration.client.controller;

import com.syncronys.registration.client.dto.UserDto;
import com.syncronys.registration.client.entity.User;
import com.syncronys.registration.client.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("index")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model) {
        UserDto existing = userService.findByUserName(user.getUserName());
        if (existing != null) {
            result.rejectValue("userName", null, "There is already an account registered with that User Name");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        try {
            userService.saveUser(user);
        } catch (IllegalArgumentException e) {
            return "redirect:/register?error=" + e.getMessage();
        } catch (Exception e) {
            return "redirect:/register?error=" + e.getMessage();
        }
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String listRegisteredUsers(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/user-info")
    public String userInfo(Model model, Principal principal) {
        UserDto user = userService.findByUserName(principal.getName());
        model.addAttribute("user", user);
        return "user-info";
    }

    @PostMapping("/user/update")
    public String update(@ModelAttribute("user") UserDto user, Model model) {
        try {
            userService.updateUser(user);
            model.addAttribute("user", user);
        } catch (Exception e) {
            return "redirect:/user-info?error=" + e.getMessage();
        }
        return "redirect:/user-info?success";
    }
}
