package com.yunuskaya.task.management.api.dto;

import com.yunuskaya.task.management.api.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
}
