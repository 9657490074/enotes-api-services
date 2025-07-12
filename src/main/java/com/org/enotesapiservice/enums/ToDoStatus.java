package com.org.enotesapiservice.enums;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
public enum ToDoStatus {
    NOT_STARTED(1, "Not Started"),
    IN_PROGRESS(2, "In Progress"),
    COMPLETED(3, "Completed");

    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String name;

}
