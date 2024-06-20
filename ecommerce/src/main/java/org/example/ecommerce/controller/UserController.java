package org.example.ecommerce.controller;

import jakarta.validation.Valid;
import org.example.ecommerce.dto.request.UserRequestDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @GetMapping("/all")
    public String getUsers(@RequestParam(defaultValue = "0") int pageNo,
                           @RequestParam(defaultValue = "10") int pageSize) {
        return "Users";
    }
    @PostMapping("/add/{userId}")
    public String addUser(@PathVariable Long userId, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        return "User added";
    }
    @PatchMapping("/change-status/{userId}")
    public String changeUserStatus(@PathVariable Long userId, @RequestBody UserRequestDTO userRequestDTO) {
        return "User patched";
    }
    @PutMapping("/update/{userId}")
    public String updateUser(@PathVariable Long userId, @RequestBody UserRequestDTO userRequestDTO) {
        return "User updated";
    }
    @DeleteMapping("/delete/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        return "User deleted";
    }
}
