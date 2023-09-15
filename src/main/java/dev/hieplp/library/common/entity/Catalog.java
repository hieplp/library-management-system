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
public class Catalog {
    @Id
    @Column(length = 10)
    private String catalogId;

    @Column(length = 100)
    private String catalogName;

    @Column(length = 100)
    private String description;

    @Column(length = 1)
    private Byte status;

    @Column(length = 10)
    private String createdBy;

    private Timestamp createdAt;

    @Column(length = 10)
    private String modifiedBy;

    private Timestamp modifiedAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentCatalog", referencedColumnName = "catalogId")
    private Catalog parentCatalog;

    @OneToMany(mappedBy = "parentCatalog", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Catalog> childCatalogs;
}
