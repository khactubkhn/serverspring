package com.gpch.login.service;

import com.gpch.login.model.Role;
import com.gpch.login.model.User;
import com.gpch.login.repository.RoleRepository;
import com.gpch.login.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("UserService")
public class UserService{
	@Autowired
    private UserRepository userRepository;
	@Autowired
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }
    
    public User findByUsernameAndPassword(String username, String password) {
    	User user = userRepository.findByUsername(username);
    	if(user==null) return null;
    	Boolean check = bCryptPasswordEncoder.matches(password, user.getPassword());
    	if(check) {
    		return user;
    	} else {
    		return null;
    	}
    	
    }
    
    public User loadUserByUsername(String username) {
        
    	return userRepository.findByUsername(username);
    	
    }
    
  
    

}