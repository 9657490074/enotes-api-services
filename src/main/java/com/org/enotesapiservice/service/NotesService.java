package com.org.enotesapiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.org.enotesapiservice.dto.NotesDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NotesService {

    Boolean saveNotes(String notes, MultipartFile file) throws IOException;

    List<NotesDto> getAllNotes();
}
