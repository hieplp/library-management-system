package dev.hieplp.library.common.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Country {
    @Id
    private String countryId;

    @Column(nullable = false)
    private String countryName;

    private String description;

    @Column(length = 1)
    private Byte status;

    private String createdBy;

    private Timestamp createdAt;

    private String modifiedBy;

    private Timestamp modifiedAt;

    @OneToMany(
            mappedBy = "country",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<City> cities;
}
