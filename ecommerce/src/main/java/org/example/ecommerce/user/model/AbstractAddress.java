package org.example.ecommerce.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractAddress {
    @Size(max = 15)
    @Column(name = "mobile")
    protected String mobile;

    @Size(max = 255)
    @Column(name = "city")
    protected String city;

    @Size(max = 255)
    @Column(name = "street")
    protected String street;

    @Size(max = 255)
    @Column(name = "district")
    protected String district;

    @Column(name = "postal_code")
    protected Integer postalCode;
}
