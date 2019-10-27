package me.pckv.kompis.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Location {
    @Id @GeneratedValue private long id;
    @NotNull private float latitude;
    @NotNull private float longitude;
    @NotNull private float accuracy;
}
