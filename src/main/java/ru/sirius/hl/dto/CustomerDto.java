package ru.sirius.hl.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("customer")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class CustomerDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("full_name")
    private String name;

    @JsonProperty("email")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String email;
}