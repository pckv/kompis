package me.pckv.kompis.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Location {

    @Id @GeneratedValue private long id;
    private float latitude;
    private float longitude;
    private float accuracy;
}
