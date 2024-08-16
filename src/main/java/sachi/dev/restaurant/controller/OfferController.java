package sachi.dev.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sachi.dev.restaurant.dto.OfferDTO;
import sachi.dev.restaurant.service.OfferService;

import java.util.List;

@RestController
@RequestMapping()
public class OfferController {

    @Autowired
    private OfferService offerService;

    @PostMapping("/api/staff/offer")
    public ResponseEntity<OfferDTO> createOffer(@RequestBody  OfferDTO offerDTO) throws Exception {
        return new ResponseEntity<>(offerService.createOffer(offerDTO), HttpStatus.CREATED);
    }

    @PutMapping("/api/staff/offer/{offerId}")
    public ResponseEntity<OfferDTO> updateOffer(@RequestBody  OfferDTO offerDTO,
                                                @PathVariable String offerId) throws Exception {
        return new ResponseEntity<>(offerService.updateOffer(offerId,offerDTO), HttpStatus.CREATED);
    }

    @GetMapping("/api/staff/offer")
    public ResponseEntity<List<OfferDTO>> getAllOffers() {
        return new ResponseEntity<>(offerService.findAllOffers(), HttpStatus.OK);
    }
}
