package me.pckv.kompis.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "\"user\"")
public class User {

    @Id @GeneratedValue private long id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @NotEmpty @Email private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) @NotEmpty private String password;
    @NotEmpty private String displayName;
}