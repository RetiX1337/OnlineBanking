package org.onlinebankingweb.controllers;

import org.onlinebankingweb.security.userprincipal.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/a")
public class AdminController {

    @GetMapping("/admin-page")
    public String getUser(@AuthenticationPrincipal UserPrincipal principal) {
        return "Logged in as " + principal.getUsername();
    }
}
