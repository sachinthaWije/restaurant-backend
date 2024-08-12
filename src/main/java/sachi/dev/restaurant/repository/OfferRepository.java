package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sachi.dev.restaurant.dto.OfferDTO;
import sachi.dev.restaurant.model.Offer;

public interface OfferRepository extends MongoRepository<Offer, String> {

    public OfferDTO findOfferByName(String offerName);
    public  OfferDTO findOfferByOfferId(String offerId);
}
