package com.org.enotesapiservice.controller;

import com.org.enotesapiservice.dto.FavoriteNotesDTO;
import com.org.enotesapiservice.dto.NotesDto;
import com.org.enotesapiservice.dto.NotesResponse;
import com.org.enotesapiservice.entity.FileDetails;
import com.org.enotesapiservice.service.NotesService;
import com.org.enotesapiservice.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notes")
public class NotesController {

    private final NotesService notesService;

    @PostMapping("/")
    @PreAuthorize("hasRole('USER')")
    private ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws IOException {
        Boolean saveNotes = notesService.saveNotes(notes, file);
        if (saveNotes) {
            return CommonUtil.createBuildResponseMessage("notes saved success", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("notes save failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasRole('USER','ADMIN')")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws IOException {

        FileDetails fileDetails = notesService.getFileDetails(id);
        byte[] data = notesService.downloadFile(fileDetails);

        HttpHeaders headers = new HttpHeaders();
        String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());

        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

        return ResponseEntity.ok().headers(headers).body(data);
    }


    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    private ResponseEntity<?> getAllNotes() {
        List<NotesDto> allNotes = notesService.getAllNotes();
        if (CollectionUtils.isEmpty(allNotes)) {
            return CommonUtil.createErrorResponseMessage("NO-CONTENT", HttpStatus.NO_CONTENT);
        }
        return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
    }

    @GetMapping("/user-notes")
    @PreAuthorize("hasRole('USER')")
    private ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNumber, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Integer userId = 1;
        NotesResponse allNotes = notesService.getAllNotesByUser(userId, pageNumber, pageSize);
        return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) {
        notesService.softDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Deleted Successfully", HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) {
        notesService.restoreNotes(id);
        return CommonUtil.createBuildResponseMessage("Note Restore Successfully", HttpStatus.OK);
    }

    @GetMapping("/recycle-bin")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserRecycleBinNotes() {
        Integer userId = 1;
        List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
        if (CollectionUtils.isEmpty(notes)) {
            return CommonUtil.createBuildResponseMessage("notes not available in recycle-bin ", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) {
        notesService.hardDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Deleted Successfully", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> emptyRecycleBin() {
        int userId = 1;
        notesService.emptyRecycleBin(userId);
        return CommonUtil.createBuildResponseMessage("Deleted Successfully", HttpStatus.OK);
    }

    @GetMapping("/fav/{noteId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) {
        notesService.favoriteNotes(noteId);
        return CommonUtil.createBuildResponseMessage("Notes Added to Favourite", HttpStatus.CREATED);
    }

    @DeleteMapping("/un-fav/{favNotId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> UnFavouriteNote(@PathVariable Integer favNotId) {
        notesService.unFavoriteNotes(favNotId);
        return CommonUtil.createBuildResponseMessage("Remove UnFavourite note ", HttpStatus.OK);
    }

    @GetMapping("/fav-note")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserFavouriteNote() {
        List<FavoriteNotesDTO> userFavoriteNotes = notesService.getUserFavoriteNotes();
        if (CollectionUtils.isEmpty(userFavoriteNotes)) {
            return CommonUtil.createBuildResponseMessage("NO-CONTENT", HttpStatus.NO_CONTENT);
        }
        return CommonUtil.createBuildResponse(userFavoriteNotes, HttpStatus.OK);
    }

    @GetMapping("/copy/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> copyNotes(@PathVariable Integer id) {
        Boolean copyNotes = notesService.copyNotes(id);
        if (copyNotes) {
            return CommonUtil.createBuildResponseMessage("Copy Success", HttpStatus.CREATED);
        } else {
            return CommonUtil.createErrorResponseMessage("Copy Failed! Please try later", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}