package com.system.common.domain.dto.user;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    private String username;
    private String oldPassword;
    private String newPassword;
    private String checkPassword;
}
