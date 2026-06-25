package com.streamanalytics.auth_service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class UserDto {
    private String Username;
    private String Message;
}
