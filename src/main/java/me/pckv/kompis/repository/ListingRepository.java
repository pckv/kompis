package me.pckv.kompis.repository;

import me.pckv.kompis.data.Listing;
import me.pckv.kompis.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {
    List<Listing> findByOwner(User user);
    List<Listing> findByAssignee(User user);
}
