package com.org.enotesapiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.org.enotesapiservice.dto.NotesDto;
import com.org.enotesapiservice.dto.NotesResponse;
import com.org.enotesapiservice.entity.FileDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface NotesService {

    Boolean saveNotes(String notes, MultipartFile file) throws IOException;

    List<NotesDto> getAllNotes();

    byte[] downloadFile(FileDetails fileDetails) throws IOException;

    FileDetails getFileDetails(Integer id);

    NotesResponse getAllNotesByUser(Integer userId,Integer pageNumber,Integer pageSize);

    void softDeleteNotes(Integer id);

    void restoreNotes(Integer id);

    List<NotesDto> getUserRecycleBinNotes(Integer userId);

    void hardDeleteNotes(Integer id);

    void emptyRecycleBin(int userId);
}