package me.pckv.kompis.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Assignee {

    @Id @GeneratedValue private long id;
    @NonNull @ManyToOne private User user;
    @NonNull @OneToOne(cascade = {CascadeType.ALL}) private Location location;
}
