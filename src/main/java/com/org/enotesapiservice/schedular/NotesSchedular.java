package com.org.enotesapiservice.schedular;

import com.org.enotesapiservice.entity.Notes;
import com.org.enotesapiservice.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotesSchedular {

    private final NotesRepository notesRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteNotesSchedular() {
        LocalDateTime cutOffDate = LocalDateTime.now().minusDays(7);
        List<Notes> deletedNotes = notesRepository.findAllByIsDeletedAndDeletedOnBefore(true, cutOffDate);
        notesRepository.deleteAll(deletedNotes);
    }
}
