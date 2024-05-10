package com.dmytro.gameboot.controller;

import com.dmytro.gameboot.domain.Game;
import com.dmytro.gameboot.domain.GameDetail;
import com.dmytro.gameboot.domain.User;
import com.dmytro.gameboot.service.GameService;
import com.dmytro.gameboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@PreAuthorize("hasAuthority('ADMIN')")
@Controller
@RequiredArgsConstructor
@RequestMapping("/game-boot/admin")
public class AdminController {

    private final UserService userService;
    private final GameService gameService;

    @GetMapping()
    public String adminPage(Model model) {
        return "adminPage";
    }


    @GetMapping("/view-users")
    public String viewAllUsers(@PageableDefault Pageable pageable, Model model) {
        Page<User> userPage = userService.getAllUsers(pageable);
        model.addAttribute("userPage", userPage);
        return "viewUsers";
    }

    @PostMapping("/promote-to-admin")
    public String promoteToAdmin(@ModelAttribute("email") String email) {
        userService.promoteUserToAdmin(email);
        return "redirect:/game-boot/admin/view-users";
    }

    @GetMapping("/search")
    public String searchByCriteria(@RequestParam("criteria") String criteria,
                                   @PageableDefault Pageable pageable,
                                   Model model) {
        Page<User> userPage = userService.findAllByUsernameOrEmailContaining(criteria, pageable);
        model.addAttribute("userPage", userPage);
        model.addAttribute("criteria", criteria);
        return "viewUsersSearchResult";
    }

    @GetMapping("/manage-games")
    public String manageGames(@PageableDefault(size = 5) Pageable pageable, Model model) {
        Page<Game> gamePage = gameService.getAllGamesWithDetails(pageable);

        Sort sort = pageable.getSort();

        String sortField = null;
        Sort.Direction sortDirection = null;

        if (sort.isSorted()) {
            Sort.Order order = sort.iterator().next();
            sortField = order.getProperty();
            sortDirection = order.getDirection();
        }
        model.addAttribute("gamePage", gamePage);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        return "viewGames";
    }

    @GetMapping("/search-game")
    public String searchGameByCriteria(@RequestParam("criteria") String criteria,
                                   @PageableDefault(size = 5) Pageable pageable,
                                   Model model) {
        Page<Game> gamePage = gameService.getGameByNameWithGameDetails(criteria, pageable);
        Sort sort = pageable.getSort();

        String sortField = null;
        Sort.Direction sortDirection = null;

        if (sort.isSorted()) {
            Sort.Order order = sort.iterator().next();
            sortField = order.getProperty();
            sortDirection = order.getDirection();
        }
        model.addAttribute("gamePage", gamePage);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("criteria", criteria);
        return "viewGamesSearchResult";
    }

}
