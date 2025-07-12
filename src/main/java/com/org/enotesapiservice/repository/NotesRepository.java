package com.org.enotesapiservice.repository;

import com.org.enotesapiservice.entity.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer> {
    Page<Notes> findByCreatedBy(Integer userId, Pageable pageable);
}
