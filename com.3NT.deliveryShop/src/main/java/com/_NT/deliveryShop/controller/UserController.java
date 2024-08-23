package com._NT.deliveryShop.controller;


import com._NT.deliveryShop.domain.dto.UserDto;
import com._NT.deliveryShop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseBody
    public UserDto.Response createUser(@RequestBody UserDto.Create userDto) {

        UserDto.Response createdUser = userService.createUser(userDto);
        log.info(createdUser.toString());

        return createdUser;
    }

//    @GetMapping
//
//    @GetMapping

}
