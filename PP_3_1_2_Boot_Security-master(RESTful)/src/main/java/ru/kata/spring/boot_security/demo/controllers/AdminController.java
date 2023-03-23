package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserService;


import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    public AdminController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping()
    public String getAllUsers(Model model, @AuthenticationPrincipal UserDetails admin, User addUser) {
        User adminTop = userService.findByUsername(admin.getUsername());
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("theUsers", allUsers);
        model.addAttribute("adminTop", adminTop);
        model.addAttribute("role_admin", roleRepository.findRoleByName("ROLE_ADMIN"));
        model.addAttribute("role_user", roleRepository.findRoleByName("ROLE_USER"));
        model.addAttribute("addUser", addUser);
        return "all-users";
    }


    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("addUser") User addUser
    ) {
        addUser.setPassword(bCryptPasswordEncoder.encode(addUser.getPassword()));
        userService.saveUser(addUser);

        return "redirect:/admin";
    }

    @PostMapping("/updateUser")
    public String updateUser(Model model, @RequestParam("idToUpdate") Integer id) {
        model.addAttribute("userForUpdate", userService.getUserById(id));
        String adminRole = "ROLE_ADMIN";
        model.addAttribute("ROLE_ADMIN", adminRole);
        String userRole = "ROLE_USER";
        model.addAttribute("ROLE_USER", userRole);
        return "update";
    }

    @PostMapping("/update")
    public String update(
            @RequestParam("idToUpdate") Integer id,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("age") Integer age,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("roles") Role role) {
        User userInfoUpdate = new User();
        userInfoUpdate.setName(name);
        userInfoUpdate.setSurname(surname);
        userInfoUpdate.setAge(age);
        userInfoUpdate.setEmail(email);
        userInfoUpdate.setPassword(bCryptPasswordEncoder.encode(password));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        userInfoUpdate.setRoles(roles);
        userService.update(id, userInfoUpdate);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("idToDelete") Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
