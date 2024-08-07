package sachi.dev.restaurant.service;

import sachi.dev.restaurant.dto.QueryDTO;

import java.util.List;

public interface QueryService {
    QueryDTO findById(String id);
    List<QueryDTO> findAllByCustomerId(String customerId);
    QueryDTO save(QueryDTO queryDTO);
    QueryDTO updateRespond(QueryDTO queryDTO, String id);
    List<QueryDTO> findAllByRestaurantId(String restaurantId);
}
