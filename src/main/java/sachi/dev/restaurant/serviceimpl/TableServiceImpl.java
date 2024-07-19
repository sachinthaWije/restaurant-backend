package sachi.dev.restaurant.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.dto.TableDTO;
import sachi.dev.restaurant.model.Restaurant;
import sachi.dev.restaurant.model.Table;
import sachi.dev.restaurant.repository.RestaurantRepository;
import sachi.dev.restaurant.repository.TableRepository;
import sachi.dev.restaurant.service.TableService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public TableDTO createTable(TableDTO tableDTO, String restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        Table table = modelMapper.map(tableDTO, Table.class);
        table.setRestaurantId(restaurant.getRestaurantId());
        Table savedTable = tableRepository.save(table);
        return modelMapper.map(savedTable, TableDTO.class);
    }

    @Override
    public List<TableDTO> getAllTablesByRestaurantId(String restaurantId) {
        return tableRepository.findTablesByRestaurantId(restaurantId);

    }
}
