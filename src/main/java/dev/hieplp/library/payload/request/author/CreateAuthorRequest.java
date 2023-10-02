package dev.hieplp.library.payload.request.author;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Date;

@Data
public class CreateAuthorRequest {

    @NotBlank(message = "AuthorName is required")
    private String authorName;

    private String authorAvatar;

    private Date dateOfBirth;

    private Date dateOfDeath;

    private Byte nationality;

    private String biography;

    private String notableWorks;
}
