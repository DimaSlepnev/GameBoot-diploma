package com.dmytro.gameboot.service;

import com.dmytro.gameboot.domain.Game;
import com.dmytro.gameboot.domain.GameDetail;
import com.dmytro.gameboot.domain.Genre;
import com.dmytro.gameboot.dto.GameRequest;
import com.dmytro.gameboot.repository.GameRepository;
import com.dmytro.gameboot.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.aspectj.util.FileUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GameService {
    public static final String PHOTO_DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/";

    private final GameRepository gameRepository;

    private final GameDetailService gameDetailService;

    public void save(Game game) {
        gameRepository.save(game);
    }

    public List<Game> findByNameContaining(String name) {
        return gameRepository.getGameByNameContainingIgnoreCase(name);
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Game findGameById(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new IllegalStateException("Not_Found"));
    }

    public List<Game> getGamesByGenre(Genre genre) {
        return gameRepository.getGamesByGenre(genre);
    }

    public Page<Game> getAllGamesWithDetails(Pageable pageable) {
        return gameRepository.findAllGamesWithGameDetails(pageable);
    }

    public Page<Game> getGameByNameWithGameDetails(String name, Pageable pageable) {
        return gameRepository.getGameByNameWithGameDetails(name, pageable);
    }

    public void save(GameRequest gameRequest, MultipartFile file) {
        String fileName = gameRequest.getName().replace(":","");
        try {
            fileName = FileUploadUtil.saveFile(PHOTO_DIRECTORY, fileName, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameRequest.setPhotoUrl("http://localhost:8085/game-boot/v1/game/image/" + fileName);
        GameDetail gameDetail = GameDetail.builder()
                .price(gameRequest.getPrice())
                .count(gameRequest.getCount())
                .yearOfProduction(gameRequest.getYearOfProduction())
                .build();
        Game game = Game.builder()
                .name(gameRequest.getName())
                .genres(gameRequest.getGenres())
                .photoUrl(gameRequest.getPhotoUrl())
                .gameDetail(gameDetail)
                .build();
        gameDetailService.save(gameDetail);
        gameDetail.setGame(game);
        gameRepository.save(game);
        gameDetailService.save(gameDetail);
    }

    public void editGame(GameRequest gameRequest) {
        Game game = gameRepository.getById(gameRequest.getGameId());
        Game oldGame = (Game) gameRepository.findGameByName(gameRequest.getName())
                .orElse(Game.builder()
                        .gameId(-100L)
                        .name("NOT_FOUND")
                        .build());
        if (!Objects.equals(game.getGameId(), oldGame.getGameId()) && game.getName().equalsIgnoreCase(oldGame.getName())) {
            throw new IllegalStateException("Game with such name already exist");
        }
        game.getGameDetail().setPrice(gameRequest.getPrice());
        game.getGameDetail().setCount(gameRequest.getCount());
        game.getGameDetail().setYearOfProduction(gameRequest.getYearOfProduction());
        gameDetailService.save(game.getGameDetail());
        game.setGenres(gameRequest.getGenres());
        game.setName(gameRequest.getName());
        gameRepository.save(game);
    }

    /*public void updatePhoto(Long id, MultipartFile file) {
        Game game = gameRepository.getById(id);
        String photoUrl = photoFunction.apply(id.toString(), file);
        game.setPhotoUrl(photoUrl);
        gameRepository.save(game);
    }*/

   /* private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");

    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
        String filename = id + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (Files.notExists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/game-boot/v1/game/image/" + filename).toUriString();
        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image", exception);
        }
    };*/
}
