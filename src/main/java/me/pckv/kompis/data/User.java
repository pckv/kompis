package me.pckv.kompis.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "\"user\"")
public class User {
    @Id @GeneratedValue private long id;
    @NonNull private String email;
    @NonNull private String password;
    @NonNull private String displayName;
}