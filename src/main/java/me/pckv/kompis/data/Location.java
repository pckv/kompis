package me.pckv.kompis.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "location")
public class Location {
    @Id @GeneratedValue private long id;
    @NotNull private float latitude;
    @NotNull private float longitude;
    @NotNull private float accuracy;
    @OneToOne @MapsId private Listing listing;
}
