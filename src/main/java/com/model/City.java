package com.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class City {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String cityName;
}
