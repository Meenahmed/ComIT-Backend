package com.meenah.meenahsignature.auth;



import com.meenah.meenahsignature.payload.SignUpRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/register")
@AllArgsConstructor
public class UserApi {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody SignUpRequest request){
        return userService.signUp(request);
    }

    @PutMapping("/update")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

}
