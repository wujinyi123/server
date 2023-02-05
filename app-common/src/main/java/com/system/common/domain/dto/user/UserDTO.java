package com.system.common.domain.dto.user;

import com.system.common.domain.model.UserModel;
import lombok.Data;

@Data
public class UserDTO extends UserModel {
    private String roleName;
    private Boolean isAlive;
}
