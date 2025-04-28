package ru.sirius.hl.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("movie")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class MovieDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String name;

    @JsonProperty("genre")
    private String genre;

    @JsonProperty("duration_minutes")
    private float duration;
}