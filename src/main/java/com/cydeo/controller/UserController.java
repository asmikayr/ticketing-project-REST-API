package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public ResponseEntity<ResponseWrapper> getUsers(){
        List<UserDTO> users = userService.listAllUsers();
        ResponseWrapper responseWrapper = new ResponseWrapper();

        responseWrapper.setSuccess(true);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("Users retrieved");
        responseWrapper.setData(users);


        return ResponseEntity.ok(responseWrapper);
    }
 //   public ResponseEntity<ResponseWrapper> getUserByUserName(){}
//    public ResponseEntity<ResponseWrapper> createUser(){}
//    public ResponseEntity<ResponseWrapper> updateUser(){}
//    public ResponseEntity<ResponseWrapper> deleteUser(){}

}
