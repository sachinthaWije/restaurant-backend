package sachi.dev.restaurant.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.dto.CategoryDTO;
import sachi.dev.restaurant.dto.OfferDTO;
import sachi.dev.restaurant.model.Category;
import sachi.dev.restaurant.model.Offer;
import sachi.dev.restaurant.repository.OfferRepository;
import sachi.dev.restaurant.service.OfferService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    public ModelMapper modelMapper;

    @Override
    public OfferDTO createOffer(OfferDTO offer) throws Exception {
        OfferDTO existingOffer = offerRepository.findOfferByName(offer.getName());
        offer.setCreatedAt(new Date());
        if (existingOffer == null) {
            Offer savedOffer = offerRepository.save(modelMapper.map(offer, Offer.class));


            return modelMapper.map(savedOffer, OfferDTO.class);
        } else {
            throw new Exception("An offer with the name " + offer.getName() + " already exists.");
        }
    }

    @Override
    public OfferDTO updateOffer(String offerId, OfferDTO offer) throws Exception {
        OfferDTO existingOffer = offerRepository.findOfferByOfferId(offerId);

        if (existingOffer != null) {
            // Update the fields of the existing offer with the new values

            if (offer.getName() != null) {
                existingOffer.setName(offer.getName());
            }
            if (offer.getDescription() != null) {
                existingOffer.setDescription(offer.getDescription());
            }
            if (offer.getDiscountPercentage() != null) {
                existingOffer.setDiscountPercentage(offer.getDiscountPercentage());
            }
            if (offer.getStartDate() != null) {
                existingOffer.setStartDate(offer.getStartDate());
            }
            if (offer.getEndDate() != null) {
                existingOffer.setEndDate(offer.getEndDate());
            }

            // Update other fields as necessary

            // Save the updated offer back to the repository
            Offer updatedOffer = offerRepository.save(modelMapper.map(existingOffer, Offer.class));
            return modelMapper.map(updatedOffer, OfferDTO.class);
        } else {
            // Handle the case where the offer does not exist
            throw new Exception("Offer with ID " + offerId + " not found.");
        }
    }

    @Override
    public OfferDTO findOfferById(String id) {
        return offerRepository.findOfferByOfferId(id);
    }

    @Override
    public List<OfferDTO> findAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offers.stream().map(offer -> modelMapper.map(offer, OfferDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OfferDTO findOfferByName(String offerName) {
        return offerRepository.findOfferByName(offerName);
    }
}
