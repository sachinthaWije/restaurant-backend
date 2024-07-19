package sachi.dev.restaurant.service;

import sachi.dev.restaurant.dto.RestaurantDTO;
import sachi.dev.restaurant.dto.TableDTO;

import java.util.List;

public interface TableService {
    TableDTO createTable(TableDTO tableDTO, String restaurantId);
    List<TableDTO> getAllTablesByRestaurantId(String restaurantId);
}
