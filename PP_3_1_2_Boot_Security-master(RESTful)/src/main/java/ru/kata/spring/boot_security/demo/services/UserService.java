package ru.kata.spring.boot_security.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Transactional
    public boolean saveUser(User user) {

        user.setPassword(user.getPassword());
        userRepository.save(user);
        return true;
    }


    public List<User> getAllUsers() {


        return userRepository.findAll();
    }


    @Transactional
    public User update(Integer id, User user) {
        Optional<User> userToBeUpdatedOptional = userRepository.findById(id);
        if (!userToBeUpdatedOptional.isPresent()) {
            throw new UsernameNotFoundException("A user with this ID does not exist.");
        }
        User userToBeUpdated = userToBeUpdatedOptional.get();
        userToBeUpdated.setName(user.getName());
        userToBeUpdated.setSurname(user.getSurname());
        userToBeUpdated.setPassword(user.getPassword());
        userToBeUpdated.setUsername(user.getUsername());
        userToBeUpdated.setRoles(user.getRoles());
        userToBeUpdated.setAge(user.getAge());
        userToBeUpdated.setEmail(user.getEmail());
        return userToBeUpdated;
    }


    @Transactional(readOnly = true)

    public User getUserById(Integer id) {
        Optional<User> userByIdOptional = userRepository.findById(id);
        if (!userByIdOptional.isPresent()) {
            throw new UsernameNotFoundException("A user with this ID does not exist.");
        }
        User userById = userByIdOptional.get();
        return userById;
    }


    @Transactional
    public boolean deleteUser(Integer userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      return userRepository.findUserByEmail(email);
  }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleByName(String name) {//todo do I need to remake that?
        return roleRepository.findRoleByName(name);
    }//todo seems broken

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }
}
