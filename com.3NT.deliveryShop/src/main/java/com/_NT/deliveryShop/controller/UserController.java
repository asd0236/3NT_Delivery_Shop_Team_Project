package com._NT.deliveryShop.controller;


import static com._NT.deliveryShop.common.codes.SuccessCode.DELETE_SUCCESS;
import static com._NT.deliveryShop.common.codes.SuccessCode.INSERT_SUCCESS;
import static com._NT.deliveryShop.common.codes.SuccessCode.SELECT_SUCCESS;
import static com._NT.deliveryShop.common.codes.SuccessCode.UPDATE_SUCCESS;
import static com._NT.deliveryShop.domain.dto.UserDto.Create;
import static com._NT.deliveryShop.domain.dto.UserDto.DeleteUserResult;
import static com._NT.deliveryShop.domain.dto.UserDto.GetAllUsersResponse;
import static com._NT.deliveryShop.domain.dto.UserDto.Modify;
import static com._NT.deliveryShop.domain.dto.UserDto.ModifyUserResult;
import static com._NT.deliveryShop.domain.dto.UserDto.Result;

import com._NT.deliveryShop.common.response.ResultResponse;
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
    public ResultResponse<Result> createUser(@RequestBody Create userDto) {

        return ResultResponse.<Result>successBuilder()
            .result(userService.createUser(userDto))
            .successCode(INSERT_SUCCESS)
            .build();
    }

    @GetMapping("/{userId}")
    @ResponseBody
    @Operation(summary = "사용자 단건 조회", description = "사용자를 단건 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 조회 성공")
    public ResultResponse<Result> getUser(@PathVariable(value = "userId") Long userId,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResultResponse.<Result>successBuilder()
            .result(userService.getUser(userId, userDetails.getUser()))
            .successCode(SELECT_SUCCESS)
            .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @ResponseBody
    @Operation(summary = "사용자 페이징 조회", description = "전체 사용자를 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 페이징 조회 성공")
    public ResultResponse<Collection<GetAllUsersResponse>> getAllUsers(
            @Schema(description = "페이지 번호(1부터 N까지)", defaultValue = "1")
            @RequestParam("page") int page,
            @Schema(description = "페이지에 출력할 개수를 입력합니다.", defaultValue = "10")
            @RequestParam("size") int size,
            @Schema(description = "정렬 기준을 입력합니다.")
            @RequestParam("sort") String sort){
        return ResultResponse.<Collection<GetAllUsersResponse>>successBuilder()
            .result(userService.getAllUsers(page - 1, size, sort))
            .successCode(SELECT_SUCCESS)
            .build();
    }

    @PatchMapping("/{userId}")
    @ResponseBody
    @Operation(summary = "사용자 수정", description = "사용자를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 수정 성공")
    public ResultResponse<ModifyUserResult> modifyUser(
            @Schema(description = "사용자 식별자", example = "1", required = true)
            @PathVariable(value = "userId") Long userId,
        @RequestBody Modify userDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResultResponse.<ModifyUserResult>successBuilder()
            .result(userService.modifyUser(userId, userDto, userDetails.getUser()))
            .successCode(UPDATE_SUCCESS)
            .build();
    }


    @DeleteMapping("/{userId}")
    @ResponseBody
    @Operation(summary = "사용자 삭제", description = "사용자를 소프트 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 삭제 성공")
    public ResultResponse<DeleteUserResult> deleteUser(
            @Schema(description = "사용자 식별자", example = "1", required = true)
            @PathVariable(value = "userId") Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResultResponse.<DeleteUserResult>successBuilder()
            .result(userService.deleteUser(userId, userDetails.getUser()))
            .successCode(DELETE_SUCCESS)
            .build();
    }

}
