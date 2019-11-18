package me.pckv.kompis.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Location {
    @Id @GeneratedValue private long id;
    private float latitude;
    private float longitude;
    private float accuracy;
}
