package me.pckv.kompis.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@NoArgsConstructor
public class Listing {

    @Id @GeneratedValue private long id;
    @NotEmpty(message = "tile must not be empty") private String title;
    private boolean active = true;
    private boolean driver = false;
    @OneToOne(cascade = {CascadeType.ALL}) private Location location;
    @ManyToOne private User owner;
    @ManyToOne private User assignee;

    /**
     * Returns true if the given user is the owner.
     *
     * @param user the user to compare
     * @return true if the given user is the owner.
     */
    public boolean isOwner(User user) {
        return user.equals(owner);
    }

    /**
     * Returns true if the given user is the assignee.
     *
     * @param user the user to compare
     * @return true if the given user is the assignee
     */
    public boolean isAssignee(User user) {
        if (assignee == null) {
            return false;
        }

        return user.equals(assignee);
    }

    /**
     * Check if the listing has a assignee.
     *
     * @return true if the listing has an assignee
     */
    public boolean hasAssignee() {
        return assignee != null;
    }
}
