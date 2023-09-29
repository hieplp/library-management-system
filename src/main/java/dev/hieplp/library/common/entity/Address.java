package dev.hieplp.library.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Address {
    @Id
    private String addressId;

    @Column(nullable = false)
    private String address;

    private String description;

    @Column(length = 1)
    private Byte type;

    @Column(length = 1)
    private Byte isDefault;

    @Column(length = 1)
    private Byte status;

    private String createdBy;

    private Timestamp createdAt;

    private String modifiedBy;

    private Timestamp modifiedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Transient
    private String userId;

    @Transient
    private String wardId;
    @Transient
    private String wardName;

    @Transient
    private String districtId;
    @Transient
    private String districtName;

    @Transient
    private String cityId;
    @Transient
    private String cityName;

    @Transient
    private String countryId;
    @Transient
    private String countryName;

    // ------------------- Getters -------------------
    public String getUserId() {
        return user == null ? null : user.getUserId();
    }

    public String getWardId() {
        return ward == null ? null : ward.getWardId();
    }

    public String getWardName() {
        return ward == null ? null : ward.getWardName();
    }

    public String getDistrictId() {
        return district == null ? null : district.getDistrictId();
    }

    public String getDistrictName() {
        return district == null ? null : district.getDistrictName();
    }

    public String getCityId() {
        return city == null ? null : city.getCityId();
    }

    public String getCityName() {
        return city == null ? null : city.getCityName();
    }

    public String getCountryId() {
        return country == null ? null : country.getCountryId();
    }

    public String getCountryName() {
        return country == null ? null : country.getCountryName();
    }
}
