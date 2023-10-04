package org.onlinebankingweb.controllers.rest;

import com.google.common.base.Preconditions;
import org.onlinebanking.core.businesslogic.services.UserService;
import org.onlinebanking.core.domain.models.user.User;
import org.onlinebankingweb.dto.requests.UserUpdateRequest;
import org.onlinebankingweb.dto.responses.UserResponse;
import org.onlinebankingweb.mappers.UserMapper;
import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserRestController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/")
    public UserResponse getLoggedInUserResponse(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new UserResponse(userService.findById(userPrincipal.getUserId()));
    }

    @PutMapping("/{id}")
    public UserResponse updateLoggedInUser(@PathVariable("id") Long id, @RequestBody UserUpdateRequest userUpdateRequest,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Preconditions.checkNotNull(userUpdateRequest);

        if (userPrincipal.getUserId().equals(id)) {
            User persistedUser = userService.findById(id);
            User user = userMapper.updateRequestToDomain(userUpdateRequest, id, persistedUser.getRoles());
            User updatedUser = userService.update(user);
            return new UserResponse(updatedUser);
        } else {
            throw new AccessDeniedException("The user entity doesn't belong to logged in user");
        }
    }
}
