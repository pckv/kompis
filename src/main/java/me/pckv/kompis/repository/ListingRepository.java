package me.pckv.kompis.repository;

import me.pckv.kompis.data.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long> {
}
