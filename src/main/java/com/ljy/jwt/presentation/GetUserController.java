package com.ljy.jwt.presentation;

import com.ljy.jwt.application.GetUserService;
import com.ljy.jwt.application.resource.GetUserResource;
import com.ljy.jwt.common.response.Response;
import com.ljy.jwt.common.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class GetUserController {
    private final GetUserService getUserService;

    public GetUserController(GetUserService getUserService) {
        this.getUserService = getUserService;
    }

    @GetMapping("/v1/users")
    public Response<GetUserResource> get(Principal principal) {
        GetUserResource resource = getUserService.get(principal.getName());
        return Response.ok(resource);
    }
}
