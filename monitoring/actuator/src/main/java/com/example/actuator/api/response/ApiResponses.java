package com.example.actuator.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponses<T> {
    private String code;
    private String message;
    private T data;
}
