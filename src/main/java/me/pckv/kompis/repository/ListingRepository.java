package me.pckv.kompis.repository;

import java.util.List;
import me.pckv.kompis.data.Listing;
import me.pckv.kompis.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    List<Listing> findByOwner(User user);

    List<Listing> findByAssigneeIsNotNull();
}
