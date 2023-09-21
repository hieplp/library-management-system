package dev.hieplp.library.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Ward {
    @Id
    private String wardId;

    @Column(nullable = false)
    private String wardName;

    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "district_id")
    private District district;
}
