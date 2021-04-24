package com.meenah.meenahsignature.auth;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("management/api/v1")
@AllArgsConstructor
public class UserManagementApi {
    private final UserService userService;

    @GetMapping(path = "/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @DeleteMapping("{userId}/delete")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "userId") Long userId){
        return userService.deleteUser(userId);
    }

}
