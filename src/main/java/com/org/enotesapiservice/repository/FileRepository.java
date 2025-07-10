package com.org.enotesapiservice.repository;

import com.org.enotesapiservice.entity.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileDetails, Integer> {
}
