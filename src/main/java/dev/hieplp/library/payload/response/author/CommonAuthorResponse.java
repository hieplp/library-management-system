package dev.hieplp.library.payload.response.author;

import lombok.Data;

import java.sql.Date;

@Data
public class CommonAuthorResponse {
    private String authorId;
    private String authorName;
    private String authorAvatar;
    private Date dateOfBirth;
    private Date dateOfDeath;
    private Byte nationality;
    private String biography;
    private String notableWorks;
}
