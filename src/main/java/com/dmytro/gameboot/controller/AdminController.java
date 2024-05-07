package com.dmytro.gameboot.controller;

import com.dmytro.gameboot.domain.User;
import com.dmytro.gameboot.dto.RegistrationRequest;
import com.dmytro.gameboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@PreAuthorize("hasAuthority('ADMIN')")
@Controller
@RequiredArgsConstructor
@RequestMapping("/game-boot/admin")
public class AdminController {

    private final UserService userService;

    @GetMapping()
    public String adminPage(Model model){
        return "adminPage";
    }


    @GetMapping("/view-users")
    public String viewAllUsers(@PageableDefault Pageable pageable, Model model){
        Page<User> userPage = userService.getAllUsers(pageable);
        model.addAttribute("userPage", userPage);
        return "viewUsers";
    }

    @PostMapping("/promote-to-admin")
    public String promoteToAdmin(@ModelAttribute("email") String email){
        userService.promoteUserToAdmin(email);
        return "redirect:/game-boot/admin/view-users";
    }

}
