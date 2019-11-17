package me.pckv.kompis.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@NoArgsConstructor
@Table(name = "\"user\"")
public class User {

    @Id @GeneratedValue private long id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @NotEmpty(message = "email must not be empty") @Email
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @NotEmpty(message = "password must not be empty")
    private String password;
    @NotEmpty(message = "displayName must not be empty") private String displayName;
}