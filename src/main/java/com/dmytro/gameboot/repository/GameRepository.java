package com.dmytro.gameboot.repository;

import com.dmytro.gameboot.domain.Game;
import com.dmytro.gameboot.domain.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> getGameByNameContainingIgnoreCase(String name);

    @Query("SELECT g FROM Game g WHERE :genre MEMBER OF g.genres")
    List<Game> getGamesByGenre(@Param("genre") Genre genre);

    @Query("SELECT g FROM Game g JOIN GameDetail gm ON g.gameId=gm.game.gameId")
    Page<Game> findAllGamesWithGameDetails(Pageable pageable);

    @Query("SELECT g FROM Game g JOIN GameDetail gm ON g.gameId=gm.game.gameId WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Game> getGameByNameWithGameDetails(@Param("name") String name, Pageable pageable);

    Optional findGameByName(String name);

    Game getById(Long id);
}
