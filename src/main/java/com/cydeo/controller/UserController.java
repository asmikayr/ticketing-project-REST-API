package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "UserController", description = "User API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    @RolesAllowed({"Manager", "Admin"})
    @Operation(summary = "Get users")
    public ResponseEntity<ResponseWrapper> getUsers(){
        List<UserDTO> users = userService.listAllUsers();
        ResponseWrapper responseWrapper = new ResponseWrapper();

        responseWrapper.setSuccess(true);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setMessage("Users retrieved");
        responseWrapper.setData(users);


        return ResponseEntity.ok(responseWrapper);
    }

    @GetMapping("/{username}")
    @RolesAllowed("Admin")
    @Operation(summary = "Get user by userName")

    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable("username") String username){

        UserDTO user = userService.findByUserName(username);

        return ResponseEntity.ok(new ResponseWrapper("User is successfully retrieved", user, HttpStatus.OK));
    }
    @PostMapping
    @RolesAllowed("Admin")
    @Operation(summary = "Create user")
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO user){
        userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User is successfully created", HttpStatus.CREATED));

    }
    @PutMapping
    @RolesAllowed("Admin")
    @Operation(summary = "Update user")
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO user){
        userService.update(user);
        return ResponseEntity.ok(new ResponseWrapper("User is updated", HttpStatus.OK));
    }
    @DeleteMapping("/{username}")
    @RolesAllowed("Admin")
    @Operation(summary = "Delete user")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username) throws TicketingProjectException {
        userService.delete(username);
        return  ResponseEntity.ok(new ResponseWrapper("User is successfully deleted", HttpStatus.OK));
    }

}
