package com.org.enotesapiservice.controller;

import com.org.enotesapiservice.dto.NotesDto;
import com.org.enotesapiservice.entity.FileDetails;
import com.org.enotesapiservice.service.NotesService;
import com.org.enotesapiservice.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private ResponseEntity<?> saveNotes(@RequestParam String notes,
                                        @RequestParam(required = false) MultipartFile file) throws IOException {
        Boolean saveNotes = notesService.saveNotes(notes, file);
        if (saveNotes) {
            return CommonUtil.createBuildResponseMessage("notes saved success", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("notes save failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/download/{id}")
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
    private ResponseEntity<?> getAllNotes() {
        List<NotesDto> allNotes = notesService.getAllNotes();
        if (CollectionUtils.isEmpty(allNotes)) {
            return CommonUtil.createErrorResponseMessage("NO-CONTENT", HttpStatus.NO_CONTENT);
        }
        return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
    }

}