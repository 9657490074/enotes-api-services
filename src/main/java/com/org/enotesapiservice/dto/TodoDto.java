package com.org.enotesapiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoDto {
    private Integer id;

    private String title;

    private StatusDto status;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StatusDto {
        private Integer id;
        private String name;
    }
}
