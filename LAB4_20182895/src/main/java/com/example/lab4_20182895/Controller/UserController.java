package com.example.lab4_20182895.Controller;

import com.example.lab4_20182895.Entity.User;
import com.example.lab4_20182895.Repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value = {"/sesion", "/"})
    public String Inicio(Model model) {


        return "inicio";
    }

    @PostMapping(value = {"/validar"})
    public String Validar(Model model, @RequestParam("username") String username, @RequestParam("password") String password) {

        Optional<User>  userOptional = userRepository.findById(username);

        if (userOptional.isPresent()) {

        } else {
            return "redirect:/product";
        }



        return "inicio";
    }

}
