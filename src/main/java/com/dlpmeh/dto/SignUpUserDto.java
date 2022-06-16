package com.dlpmeh.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SignUpUserDto extends UserDto {
    private Long id;

    @JsonProperty("full_name")
    private String fullName;

    private String email;

    @JsonProperty("email_verified")
    private boolean emailVerified;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
