package com._NT.deliveryShop.controller;

import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import com._NT.deliveryShop.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final ProductService productService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    // 함수 호출을 위해 어드민 권한을 가진 유저를 임시로 제작
    private final User admin =
            new User("tmp", "adminUser@#19#!%", "tmp", "tmp", UserRoleEnum.ADMIN);

    // 로그인 페이지
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    // 메인 페이지
    @GetMapping("/main")
    public String getMainPage() {
        return "main";  // main.html
    }

    // 회원 관리 페이지
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String getUsers(Model model) {

        model.addAttribute("users", userService.getAllUsers(0, 100, "userId"));

        return "users";
    }

    // 음식점 관리 페이지
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/restaurants")
    public String getRestaurants(Model model) {

        Pageable pageable = PageRequest.of(0, 100);
        model.addAttribute("restaurants", restaurantService.readRestaurants(pageable));

        return "restaurants";
    }

    // 상품 관리 페이지
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/products")
    public String getProducts(Model model) {

        Pageable pageable = PageRequest.of(0, 100);
        model.addAttribute("products", productService.readProducts(pageable));

        return "products";
    }

    // 주문 관리 페이지
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public String getOrders(Model model) {

        model.addAttribute("orders", orderService.getAllOrders(0, 100, "orderId", true, admin));

        return "orders";
    }

    // 결제 관리 페이지
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/payments")
    public String getPayments(Model model) {

        model.addAttribute("payments", paymentService.getAllPayments(0, 100, "paymentId", admin));

        return "payments";
    }


}
