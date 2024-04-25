package com.dmytro.gameboot.repository;

import com.dmytro.gameboot.domain.GameDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameDetailRepository extends JpaRepository<GameDetail, Long> {
}
