package sachi.dev.restaurant.service;

import sachi.dev.restaurant.dto.OfferDTO;
import sachi.dev.restaurant.model.Offer;

import java.util.List;

public interface OfferService {

    OfferDTO createOffer(OfferDTO offer) throws Exception;
    OfferDTO updateOffer(String offerId, OfferDTO offer) throws Exception;
    OfferDTO findOfferById(String id);
    List<OfferDTO> findAllOffers();
    OfferDTO findOfferByName(String offerName);
}
