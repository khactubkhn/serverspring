
package com.gpch.login.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.gpch.login.model.User;
import com.gpch.login.service.JwtService;
import com.gpch.login.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtService jwtService;


    @RequestMapping(value = "/api/reg", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Map<String, ? extends Object> register(@Valid User user){
        Map<String, Object> result = new HashMap<>();
        int status = 0;
        User newUser = userService.saveUser(user);
        if(newUser != null){
            status = 1;
            result.put("message", "Register successful");
        }else{
            result.put("message", "Register Error");

        }
        result.put("status", status);
        return result;
    }

    @RequestMapping(value = "/api/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Map<String, ? extends Object> login(HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int status = 0;
        User user = userService.findByUsernameAndPassword(username, password);
        if(user != null){
            status = 1;
            Map<String, Object> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("lastName", user.getLastName());
            data.put("firstName", user.getFirstName());
            result.put("user", data);
            String token = jwtService.generateTokenLogin(user.getUsername());
            result.put("token", token);
        }else{
            result.put("message", "Login fail");
        }
        result.put("status", status);
        return result;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Map<String, ? extends Object> get(HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        User user = (User) request.getAttribute("user");
        result.put("username", user.getUsername());
        return result;
    }

}