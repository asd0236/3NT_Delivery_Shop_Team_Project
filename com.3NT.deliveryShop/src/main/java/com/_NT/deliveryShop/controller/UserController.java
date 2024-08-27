package com._NT.deliveryShop.controller;


import com._NT.deliveryShop.domain.dto.UserDto;
import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseBody
    public UserDto.Result createUser(@RequestBody UserDto.Create userDto) {

        return userService.createUser(userDto);
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public UserDto.Result getUser(@PathVariable(value = "userId") Long userId,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return userService.getUser(userId, userDetails.getUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @ResponseBody
    public Collection<UserDto.GetAllUsersResponse> getAllUsers(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sort){
        return userService.getAllUsers(page - 1, size, sort);
    }

    @PatchMapping("/{userId}")
    @ResponseBody
    public UserDto.ModifyUserResult modifyUser(@PathVariable(value = "userId") Long userId,
                                               @RequestBody UserDto.Modify userDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.modifyUser(userId, userDto, userDetails.getUser());
    }


    @DeleteMapping("/{userId}")
    @ResponseBody
    public UserDto.DeleteUserResult deleteUser(@PathVariable(value = "userId") Long userId,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return userService.deleteUser(userId, userDetails.getUser());
    }

}
