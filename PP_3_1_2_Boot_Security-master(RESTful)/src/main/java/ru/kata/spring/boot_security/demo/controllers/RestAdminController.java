package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserService;


import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestAdminController {

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") int id) {
        User user = userService.getUserById(id);
        return user;
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user, @RequestParam(required = false
            , name = "selectedRoles") String[] selectedRoles) {
        HashSet<Role> addRoles = new HashSet<>();
        for (String s : selectedRoles) {
            if (s.contains("ADMIN")) {
                addRoles.add(roleService.findRoleByName("ROLE_ADMIN"));
            }
            if (s.contains("USER")) {
                addRoles.add(roleService.findRoleByName("ROLE_USER"));
            }
        }
        user.setRoles(addRoles);
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PatchMapping("/edit")
    public ResponseEntity<User> patchUser(@RequestBody User user, @RequestParam(required = false
            , name = "selectedRoles") String[] selectedRoles) {
        HashSet<Role> editRoles = new HashSet<>();
        for (String s : selectedRoles) {
            if (s.contains("ADMIN")) {
                editRoles.add(roleService.findRoleByName("ROLE_ADMIN"));
            }
            if (s.contains("USER")) {
                editRoles.add(roleService.findRoleByName("ROLE_USER"));
            }
        }
        user.setRoles(editRoles);
        userService.update(user.getId(),user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    @DeleteMapping ("/delete")
    public void delete(@RequestParam(required = true, name = "deleteId") Integer id) {
        userService.deleteUser(id);
    }


    @GetMapping("/thisUser")
    @ResponseBody
    public ResponseEntity<User> currentClient(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(userService.getUserById(user.getId()), HttpStatus.OK) ;
    }

    @GetMapping("/userInfo")
    @ResponseBody
    public String roles(@AuthenticationPrincipal User user) {
        String roles = new String();
        for(Role r: user.getRoles()){
            roles += r.toString() + " ";
        }
        return roles;
    }
}

