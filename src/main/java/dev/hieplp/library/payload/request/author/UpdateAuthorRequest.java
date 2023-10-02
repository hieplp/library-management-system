package dev.hieplp.library.payload.request.author;

import lombok.Data;

import java.sql.Date;

@Data
public class UpdateAuthorRequest {
    private String authorName;

    private String authorAvatar;

    private Date dateOfBirth;

    private Date dateOfDeath;

    private Byte nationality;

    private String biography;

    private String notableWorks;
}
