package me.pckv.kompis.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "listing")
public class Listing {
    @Id @GeneratedValue private long id;
    @NotNull private String title;
    @NotNull private boolean driver;
    @NotNull @OneToOne(mappedBy = "listing") private Location location;
}
