package com.org.enotesapiservice.repository;

import com.org.enotesapiservice.entity.FavouriteNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteNoteRepository extends JpaRepository<FavouriteNote, Integer> {
    List<FavouriteNote> findByUserId(Integer userId);
}
