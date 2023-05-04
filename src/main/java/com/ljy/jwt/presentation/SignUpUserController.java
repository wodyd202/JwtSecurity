package com.ljy.jwt.presentation;

import com.ljy.jwt.application.SignUpUserService;
import com.ljy.jwt.application.dto.SignUpUserDto;
import com.ljy.jwt.application.resource.SignUpUserResource;
import com.ljy.jwt.common.response.Response;
import com.ljy.jwt.presentation.request.SignUpUserRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class SignUpUserController {

    public SignUpUserController(SignUpUserService signUpUserService) {
        this.signUpUserService = signUpUserService;
    }

    private final SignUpUserService signUpUserService;

    @PostMapping("/v1/users")
    public Response<SignUpUserResource> signUp(@Valid @RequestBody SignUpUserRequest request) {
        SignUpUserDto dto = new SignUpUserDto(request.getId(), request.getPassword(), request.getUsername());
        SignUpUserResource resource = signUpUserService.signUp(dto);
        return Response.ok(resource);
    }
}
