package com.dmytro.gameboot.repository;

import com.dmytro.gameboot.domain.Game;
import com.dmytro.gameboot.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> getGameByNameContainingIgnoreCase(String name);

    @Query("SELECT g FROM Game g WHERE :genre MEMBER OF g.genres")
    List<Game> getGamesByGenre(@Param("genre") Genre genre);
}
