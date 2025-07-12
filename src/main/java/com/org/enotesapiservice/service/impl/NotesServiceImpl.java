package com.org.enotesapiservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.enotesapiservice.dto.NotesDto;
import com.org.enotesapiservice.dto.NotesResponse;
import com.org.enotesapiservice.entity.FileDetails;
import com.org.enotesapiservice.entity.Notes;
import com.org.enotesapiservice.exception.ResourceNotFoundException;
import com.org.enotesapiservice.repository.CategoryRepository;
import com.org.enotesapiservice.repository.FileRepository;
import com.org.enotesapiservice.repository.NotesRepository;
import com.org.enotesapiservice.service.NotesService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final FileRepository fileRepository;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public Boolean saveNotes(String notes, MultipartFile file) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        NotesDto notesDto = objectMapper.readValue(notes, NotesDto.class);

        //validation notes
        checkCategoryExists(notesDto.getCategory());

        Notes notesMap = modelMapper.map(notesDto, Notes.class);

        FileDetails fileDetails = saveFileDetails(file);
        if (!ObjectUtils.isEmpty(fileDetails)) {
            notesMap.setFileDetails(fileDetails);
        } else {
            notesMap.setFileDetails(null);
        }

        Notes savedNotes = notesRepository.save(notesMap);
        if (!ObjectUtils.isEmpty(savedNotes)) {
            return true;
        }
        return false;
    }

    private FileDetails saveFileDetails(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);

        List<String> extensionAllow = List.of("pdf", "png", "jpg", "xlsx", "docx");

        if (!extensionAllow.contains(extension)) {
            throw new IllegalArgumentException("invalid file format");
        }

        if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {

            FileDetails fileDetails = new FileDetails();
            fileDetails.setOriginalFileName(originalFilename);
            fileDetails.setDisplayFileName(getDisplayName(originalFilename));

            String randomString = UUID.randomUUID().toString();
            String uploadFileName = randomString + "." + extension;

            fileDetails.setUploadFileName(uploadFileName);
            fileDetails.setFileSize(file.getSize());

            File saveFile = new File(uploadPath);
            if (!saveFile.exists()) {
                saveFile.mkdir();
            }
            //path
            String storePath = uploadPath.concat(uploadFileName);
            fileDetails.setPath(storePath);

            //upload the file

            long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
            if (upload != 0) {
                return fileRepository.save(fileDetails);
            }

        }
        return null;
    }

    private String getDisplayName(String originalFilename) {

        String extension = FilenameUtils.getExtension(originalFilename);
        String fileName = FilenameUtils.removeExtension(originalFilename);
        if (fileName.length() > 8) {
            fileName = fileName.substring(0, 7) + "." + extension;
        }
        return fileName;
    }

    private void checkCategoryExists(NotesDto.CategoryDto category) {

        categoryRepository.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("category is invalid "));

    }

    @Override
    public List<NotesDto> getAllNotes() {
        return notesRepository.findAll().stream().map(note -> modelMapper.map(note, NotesDto.class)).toList();
    }

    @Override
    public byte[] downloadFile(FileDetails fileDetails) throws IOException {

        InputStream fileInputStream = new FileInputStream(fileDetails.getPath());
        return StreamUtils.copyToByteArray(fileInputStream);
    }

    @Override
    public FileDetails getFileDetails(Integer id) {
        return fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("file is invalid "));
    }

    @Override
    public NotesResponse getAllNotesByUser(Integer userId, Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Notes> pageNotes = notesRepository.findByCreatedBy(userId, pageable);

        List<NotesDto> notesDto = pageNotes.get().map(n -> modelMapper.map(n, NotesDto.class)).toList();
        NotesResponse notes = NotesResponse.builder()
                .notes(notesDto)
                .pageNo(pageNotes.getNumber())
                .pageSize(pageNotes.getSize())
                .totalElements(pageNotes.getTotalElements())
                .totalPages(pageNotes.getTotalPages())
                .isFirst(pageNotes.isFirst())
                .isLast(pageNotes.isLast())
                .build();
        return notes;
    }
}
