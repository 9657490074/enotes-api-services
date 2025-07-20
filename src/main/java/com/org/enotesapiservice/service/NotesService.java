package com.org.enotesapiservice.service;

import com.org.enotesapiservice.dto.FavoriteNotesDTO;
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

    NotesResponse getAllNotesByUser(Integer pageNumber, Integer pageSize);

    NotesResponse getAllNotesByUserSearch(Integer pageNumber, Integer pageSize,String keyword);

    void softDeleteNotes(Integer id);

    void restoreNotes(Integer id);

    List<NotesDto> getUserRecycleBinNotes();

    void hardDeleteNotes(Integer id);

    void emptyRecycleBin();

    void favoriteNotes(Integer noteId);

    void unFavoriteNotes(Integer noteId);

    List<FavoriteNotesDTO> getUserFavoriteNotes();

    Boolean copyNotes(Integer id);
}