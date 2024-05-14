package com.dmytro.gameboot.controller;

import com.dmytro.gameboot.domain.Game;
import com.dmytro.gameboot.domain.GameDetail;
import com.dmytro.gameboot.domain.Genre;
import com.dmytro.gameboot.domain.User;
import com.dmytro.gameboot.dto.GameRequest;
import com.dmytro.gameboot.service.GameService;
import com.dmytro.gameboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


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

    @GetMapping("/add-game")
    public String addGamePage(Model model) {
        model.addAttribute("gameRequest", new GameRequest());
        return "addGame";
    }

    @PostMapping("/add-game")
    public String addGame(@ModelAttribute("gameRequest") @Valid GameRequest gameRequest,
                          BindingResult bindingResult,
                          Model model,
                          @RequestParam("file") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "addGame";
        }
        try {
            gameService.save(gameRequest, file);
        } catch (DataIntegrityViolationException ex){
            model.addAttribute("gameAlreadyExist", 1);
            return "addGame";
        }
        return "redirect:/game-boot/admin/manage-games";
    }

    @GetMapping("/edit-game")
    public String editGamePage(@RequestParam("gameId") Long gameId, Model model) {
        Game game = gameService.findGameById(gameId);
        GameRequest gameRequest = GameRequest.builder()
                .gameId(gameId)
                .name(game.getName())
                .genres(game.getGenres())
                .price(game.getGameDetail().getPrice())
                .count(game.getGameDetail().getCount())
                .yearOfProduction(game.getGameDetail().getYearOfProduction())
                .build();
        model.addAttribute("game", game);
        model.addAttribute("gameRequest", gameRequest);
        return "editGame";
    }

    @PostMapping("/edit-game")
    public String editGame(@ModelAttribute("gameRequest") @Valid GameRequest gameRequest,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "editGame";
        }
        try {
            gameService.editGame(gameRequest);
        }
        catch (IllegalStateException | DataIntegrityViolationException e){
            model.addAttribute("gameAlreadyExist", 1);
            return "editGame";
        }
        return "redirect:/game-boot/admin/search-game?criteria=" + gameRequest.getName();
    }

}
