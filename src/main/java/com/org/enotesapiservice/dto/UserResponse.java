package com.org.enotesapiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobileNo;

    private StatusDto status;

    private List<UserRequest.RoleDTO> roles;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleDTO {
        private Integer id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusDto {
        private Integer id;
        private Boolean isActive;
    }

}
