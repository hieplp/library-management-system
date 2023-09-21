package dev.hieplp.library.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class District {
    @Id
    private String districtId;

    @Column(nullable = false)
    private String districtName;

    private String description;

    @OneToMany(mappedBy = "district")
    private Set<Ward> wards;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id")
    private City city;
}
