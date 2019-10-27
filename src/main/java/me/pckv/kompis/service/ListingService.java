package me.pckv.kompis.service;

import me.pckv.kompis.data.Listing;
import me.pckv.kompis.data.ListingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingService {
    private ListingRepository repository;

    public ListingService(ListingRepository repository) {
        this.repository = repository;
    }

    public Listing createListing(Listing newListing) {
        return repository.save(newListing);
    }

    public void deleteListingById(Long id) {
        repository.deleteById(id);
    }

    public Listing getListing(Long id) {
        return repository.getOne(id);
    }

    public List<Listing> getAllListings() {
        return repository.findAll();
    }


    //TODO: Deavtivate/Activate listing


    //TODO: Assign logged in User to listing

}
