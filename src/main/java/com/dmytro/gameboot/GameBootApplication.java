package com.dmytro.gameboot;

import com.dmytro.gameboot.domain.*;
import com.dmytro.gameboot.service.GameDetailService;
import com.dmytro.gameboot.service.GameService;
import com.dmytro.gameboot.service.UserGameService;
import com.dmytro.gameboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@RequiredArgsConstructor
public class GameBootApplication implements CommandLineRunner {

    private final UserService userService;

    private final GameService gameService;
    private final GameDetailService gameDetailService;

    private final UserGameService userGameService;

    private final PasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(GameBootApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        List<Genre> genres = new ArrayList<>();

        genres.add(Genre.SPORTS_AND_RACING);
        genres.add(Genre.ADVENTURE);
        GameDetail forzaDetail = GameDetail.builder()
                .count(15)
                .price(1000.00)
                .yearOfProduction(2021)
                .build();
        Game forza = createGame("Forza Horizon 5", genres, forzaDetail);
        gameDetailService.save(forzaDetail);
        forzaDetail.setGame(forza);
        gameService.save(forza);
        gameDetailService.save(forzaDetail);
        genres.clear();


        genres.add(Genre.STRATEGY);
        genres.add(Genre.ACTION);
        GameDetail pubgDetail = GameDetail.builder()
                .count(30)
                .price(0.00)
                .yearOfProduction(2017)
                .build();
        Game pubg = createGame("PUBG: BATTLEGROUNDS", genres, pubgDetail);
        gameDetailService.save(pubgDetail);
        pubgDetail.setGame(pubg);
        gameService.save(pubg);
        gameDetailService.save(pubgDetail);
        genres.clear();

        genres.add(Genre.ROLE_PLAYING);
        genres.add(Genre.ADVENTURE);
        GameDetail witcherDetail = GameDetail.builder()
                .count(20)
                .price(700.00)
                .yearOfProduction(2015)
                .build();
        Game witcher = createGame("The Witcher 3: Wild Hunt", genres, witcherDetail);
        gameDetailService.save(witcherDetail);
        witcherDetail.setGame(witcher);
        gameService.save(witcher);
        gameDetailService.save(witcherDetail);
        genres.clear();

        genres.add(Genre.ACTION);
        genres.add(Genre.ADVENTURE);
        GameDetail gtaDetail = GameDetail.builder()
                .count(35)
                .price(600.00)
                .yearOfProduction(2013)
                .build();
        Game gta = createGame("Grand Theft Auto V", genres, gtaDetail);
        gameDetailService.save(gtaDetail);
        gtaDetail.setGame(gta);
        gameService.save(gta);
        gameDetailService.save(gtaDetail);
        genres.clear();

        genres.add(Genre.ADVENTURE);
        genres.add(Genre.ROLE_PLAYING);
        GameDetail minecraftDetail = GameDetail.builder()
                .count(0)
                .price(30.00)
                .yearOfProduction(2011)
                .build();
        Game minecraft = createGame("Minecraft", genres, minecraftDetail);
        gameDetailService.save(minecraftDetail);
        minecraftDetail.setGame(minecraft);
        gameService.save(minecraft);
        gameDetailService.save(minecraftDetail);
        genres.clear();

        genres.add(Genre.ACTION);
        genres.add(Genre.STRATEGY);
        GameDetail codDetail = GameDetail.builder()
                .count(40)
                .price(1000.00)
                .yearOfProduction(2019)
                .build();
        Game cod = createGame("Call of Duty: Modern Warfare", genres, codDetail);
        gameDetailService.save(codDetail);
        codDetail.setGame(cod);
        gameService.save(cod);
        gameDetailService.save(codDetail);
        genres.clear();

        genres.add(Genre.ROLE_PLAYING);
        genres.add(Genre.ADVENTURE);
        GameDetail cpDetail = GameDetail.builder()
                .count(30)
                .price(800.00)
                .yearOfProduction(2020)
                .build();
        Game cp = createGame("Cyberpunk 2077", genres, cpDetail);
        gameDetailService.save(cpDetail);
        cpDetail.setGame(cp);
        gameService.save(cp);
        gameDetailService.save(cpDetail);
        genres.clear();

        genres.add(Genre.SPORTS_AND_RACING);
        genres.add(Genre.ACTION);
        GameDetail rlDetail = GameDetail.builder()
                .count(35)
                .price(600.00)
                .yearOfProduction(2015)
                .build();
        Game rl = createGame("Rocket League", genres, rlDetail);
        gameDetailService.save(rlDetail);
        rlDetail.setGame(rl);
        gameService.save(rl);
        gameDetailService.save(rlDetail);
        genres.clear();

        genres.add(Genre.STRATEGY);
        genres.add(Genre.ROLE_PLAYING);
        GameDetail civDetail = GameDetail.builder()
                .count(30)
                .price(1500.00)
                .yearOfProduction(2016)
                .build();
        Game civ = createGame("Civilization VI", genres, civDetail);
        gameDetailService.save(civDetail);
        civDetail.setGame(civ);
        gameService.save(civ);
        gameDetailService.save(civDetail);
        genres.clear();

        genres.add(Genre.ACTION);
        genres.add(Genre.ROLE_PLAYING);
        GameDetail acvDetail = GameDetail.builder()
                .count(30)
                .price(950.00)
                .yearOfProduction(2020)
                .build();
        Game acv = createGame("Assassin's Creed Valhalla", genres, acvDetail);
        gameDetailService.save(acvDetail);
        acvDetail.setGame(acv);
        gameService.save(acv);
        gameDetailService.save(acvDetail);
        genres.clear();


        List<UserGame> userGamesList = new ArrayList<>();
        List<Game> games = new ArrayList<>();
        games.add(forza);
        games.add(witcher);
        games.add(civ);
        games.add(cp);
        String pass1 = "tyler";
        User tyler = User.builder()
                .username("tyler")
                .email("tyler@game.com")
                .password(encoder.encode(pass1))
                .balance(1000.00)
                .enabled(true)
                .role(Role.ADMIN)
                /*.game(games)*/
                .build();
        UserGame forzaGame = UserGame.builder()
                .user(tyler)
                .game(forza)
                .gameCode(UUID.randomUUID().toString())
                .buyAt(LocalDateTime.now())
                .gamePrice(450.00)
                .build();
        userGamesList.add(forzaGame);
        userService.save(tyler);
        tyler.setGame(userGamesList);
        userGameService.save(forzaGame);
        userService.save(tyler);
        UserGame witcherGame = UserGame.builder()
                .user(tyler)
                .game(witcher)
                .gameCode(UUID.randomUUID().toString())
                .buyAt(LocalDateTime.now())
                .gamePrice(700.00)
                .build();
        userGamesList.add(witcherGame);
        /*userService.save(tyler);
        tyler.setGame(userGamesList);*/
        userGameService.save(witcherGame);
        /*userService.save(tyler);*/
        UserGame civGame = UserGame.builder()
                .user(tyler)
                .game(civ)
                .gameCode(UUID.randomUUID().toString())
                .buyAt(LocalDateTime.now())
                .gamePrice(1500.00)
                .build();
        userGamesList.add(civGame);
        /*userService.save(tyler);
        tyler.setGame(userGamesList);*/
        userGameService.save(civGame);
        /*userService.save(tyler);*/
        UserGame cpGame = UserGame.builder()
                .user(tyler)
                .game(cp)
                .gameCode(UUID.randomUUID().toString())
                .buyAt(LocalDateTime.now())
                .gamePrice(670.00)
                .build();
        userGamesList.add(cpGame);
        userService.save(tyler);
        tyler.setGame(userGamesList);
        userGameService.save(cpGame);
        userService.save(tyler);



        userGamesList.clear();
        games.clear();

        games.add(pubg);
        games.add(gta);
        games.add(witcher);
        games.add(cod);
        games.add(cp);
        games.add(acv);
        String pass2 = "rayan";
        User rayan = User.builder()
                .username("rayan")
                .email("ray@gmail.boo")
                .password(encoder.encode(pass2))
                .balance(3542.00)
                .enabled(true)
                .role(Role.USER)
                /*.games(games)*/
                .build();
        /*userService.save(rayan);*/

        UserGame pubgGame = UserGame.builder()
                .user(rayan)
                .game(pubg)
                .gameCode(UUID.randomUUID().toString())
                .buyAt(LocalDateTime.now())
                .gamePrice(0.0)
                .build();
        userGamesList.add(pubgGame);
        userService.save(rayan);
        rayan.setGame(userGamesList);
        userGameService.save(pubgGame);
        userService.save(rayan);
        UserGame gtaGame = UserGame.builder()
                .user(rayan)
                .game(gta)
                .gameCode(UUID.randomUUID().toString())
                .buyAt(LocalDateTime.now())
                .gamePrice(gta.getGameDetail().getPrice())
                .build();
        userGamesList.add(gtaGame);
        userGameService.save(gtaGame);
        UserGame witcherGame2 = UserGame.builder()
                .user(rayan)
                .game(witcher)
                .gameCode(UUID.randomUUID().toString())
                .buyAt(LocalDateTime.now())
                .gamePrice(witcher.getGameDetail().getPrice())
                .build();
        userGamesList.add(witcherGame);
        userGameService.save(witcherGame);
        UserGame codGame = UserGame.builder()
                .user(rayan)
                .game(cod)
                .gameCode(UUID.randomUUID().toString())
                .buyAt(LocalDateTime.now())
                .gamePrice(cod.getGameDetail().getPrice())
                .build();
        userGamesList.add(codGame);
        userGameService.save(codGame);
        UserGame cpGame2 = UserGame.builder()
                .user(rayan)
                .game(cp)
                .gameCode(UUID.randomUUID().toString())
                .buyAt(LocalDateTime.now())
                .gamePrice(cp.getGameDetail().getPrice())
                .build();
        userGamesList.add(cpGame2);
        userGameService.save(cpGame2);
        UserGame acvGame = UserGame.builder()
                .user(rayan)
                .game(acv)
                .gameCode(UUID.randomUUID().toString())
                .buyAt(LocalDateTime.now())
                .gamePrice(acv.getGameDetail().getPrice())
                .build();
        userGamesList.add(acvGame);
        userGameService.save(acvGame);
        userService.save(rayan);
        rayan.setGame(userGamesList);
        userGameService.save(gtaGame);
        userService.save(rayan);

    }

    private Game createGame(String name, List<Genre> genres, GameDetail gameDetail) {
        return Game.builder()
                .name(name)
                .genres(genres)
                .gameDetail(gameDetail)
                .build();
    }
}
