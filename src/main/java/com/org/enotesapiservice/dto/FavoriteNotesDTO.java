package com.org.enotesapiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteNotesDTO {

    private Integer id;

    private NotesDto note;

    private Integer userId;
}
