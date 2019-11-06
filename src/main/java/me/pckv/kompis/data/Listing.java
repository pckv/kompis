package me.pckv.kompis.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Listing {
    @Id @GeneratedValue private long id;
    @NonNull private String title;
    @NonNull private boolean active = true;
    @NonNull private boolean driver = false;
    // @NonNull @OneToOne private Location location;
    @ManyToOne private User owner;
    @ManyToOne private User assignee;
}
