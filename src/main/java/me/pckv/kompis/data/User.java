package me.pckv.kompis.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
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

    @Id @GeneratedValue
    private long id;

    @JsonProperty(access = Access.WRITE_ONLY) @NotEmpty(message = "email must not be empty") @Email
    private String email;

    @JsonProperty(access = Access.WRITE_ONLY) @NotEmpty(message = "password must not be empty")
    private String password;

    @NotEmpty(message = "displayName must not be empty")
    private String displayName;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String firebaseToken;

    /**
     * Returns true if the user has a firebase token.
     *
     * @return true if the user has a firebase token
     */
    public boolean hasFirebaseToken() {
        return firebaseToken != null;
    }
}