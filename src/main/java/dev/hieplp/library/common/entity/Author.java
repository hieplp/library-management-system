package dev.hieplp.library.common.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Author {
    @Id
    private String authorId;

    private String authorName;

    private String authorAvatar;

    private Date dateOfBirth;

    private Date dateOfDeath;

    private Byte nationality;

    private String biography;

    private String notableWorks;

    private Byte status;

    private String createdBy;

    private Timestamp createdAt;

    private String modifiedBy;

    private Timestamp modifiedAt;
}
