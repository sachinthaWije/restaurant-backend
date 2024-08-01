package sachi.dev.restaurant.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sachi.dev.restaurant.dto.TableDTO;
import sachi.dev.restaurant.service.TableService;

import java.util.List;

@RestController
@RequestMapping("")
public class TableController {

    @Autowired
    private TableService tableService;

    @PostMapping("/api/staff/table/{restaurantId}")
    public ResponseEntity<TableDTO> createTable(@Valid @RequestBody TableDTO tableDTO,
                                                @PathVariable String restaurantId) {
        return new ResponseEntity<>(tableService.createTable(tableDTO, restaurantId), HttpStatus.CREATED);

    }

    @GetMapping("/tables/{restaurantId}")
    public ResponseEntity<List<TableDTO>> getTablesByRestaurantId(@PathVariable String restaurantId) {
        return new ResponseEntity<>(tableService.getAllTablesByRestaurantId(restaurantId), HttpStatus.OK);
    }
}
