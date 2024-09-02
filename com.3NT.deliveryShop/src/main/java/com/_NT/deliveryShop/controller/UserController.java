package com._NT.deliveryShop.controller;


import com._NT.deliveryShop.domain.dto.UserDto;
import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "사용자", description = "사용자 등록, 조회, 수정, 삭제 API")
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseBody
    @Operation(summary = "회원가입", description = "사용자를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "사용자 등록 성공")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto.Result createUser(@RequestBody UserDto.Create userDto) {

        return userService.createUser(userDto);
    }

    @GetMapping("/{userId}")
    @ResponseBody
    @Operation(summary = "사용자 단건 조회", description = "사용자를 단건 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 조회 성공")
    public UserDto.Result getUser(@PathVariable(value = "userId") Long userId,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return userService.getUser(userId, userDetails.getUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @ResponseBody
    @Operation(summary = "사용자 페이징 조회", description = "전체 사용자를 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 페이징 조회 성공")
    public Collection<UserDto.GetAllUsersResponse> getAllUsers(
            @Schema(description = "페이지 번호(1부터 N까지)", defaultValue = "1")
            @RequestParam("page") int page,
            @Schema(description = "페이지에 출력할 개수를 입력합니다.", defaultValue = "10")
            @RequestParam("size") int size,
            @Schema(description = "정렬 기준을 입력합니다.")
            @RequestParam("sort") String sort){
        return userService.getAllUsers(page - 1, size, sort);
    }

    @PatchMapping("/{userId}")
    @ResponseBody
    @Operation(summary = "사용자 수정", description = "사용자를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 수정 성공")
    public UserDto.ModifyUserResult modifyUser(
            @Schema(description = "사용자 식별자", example = "1", required = true)
            @PathVariable(value = "userId") Long userId,
            @RequestBody UserDto.Modify userDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.modifyUser(userId, userDto, userDetails.getUser());
    }


    @DeleteMapping("/{userId}")
    @ResponseBody
    @Operation(summary = "사용자 삭제", description = "사용자를 소프트 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 삭제 성공")
    public UserDto.DeleteUserResult deleteUser(
            @Schema(description = "사용자 식별자", example = "1", required = true)
            @PathVariable(value = "userId") Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return userService.deleteUser(userId, userDetails.getUser());
    }

}
