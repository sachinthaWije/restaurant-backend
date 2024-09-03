package sachi.dev.restaurant.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sachi.dev.restaurant.dto.TableDTO;
import sachi.dev.restaurant.service.TableService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TableControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    private TableDTO tableDTO;

    private String validToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MjUwNzI3NjcsImV4cCI6MTcyNTE1OTE2NywiZW1haWwiOiJqb25fc3RhZmYiLCJhdXRob3JpdGllcyI6IlJPTEVfU1RBRkYifQ.KBQY3BWglZ3ehHlYJZhMnRnKzpUg06DVOUiAx-QvoyMUqGFsPOI67OvCpljMcIA7sYoDy9oIg3IbLuTKWiDY8A";


    @BeforeEach
    public void setUp() {
        tableDTO = new TableDTO();
        tableDTO.setTableNumber("No003");
        tableDTO.setCapacity(4);
        tableDTO.setRestaurantId("rest123");
    }

//    TC-001
    @Test
    public void createTable_ValidRequest_ShouldReturnCreated() throws Exception {
        // Mock tableService to return a valid TableDTO when a valid request is made
        Mockito.when(tableService.createTable(any(TableDTO.class), eq("rest123"))).thenReturn(tableDTO);

        mockMvc.perform(post("/api/staff/table/rest123")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(tableDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tableNumber").value("No003"))
                .andExpect(jsonPath("$.capacity").value(4));
    }

//    TC-002
    @Test
    public void getTablesByRestaurantId_ValidRequest_ShouldReturnTables() throws Exception {
        List<TableDTO> tables = Arrays.asList(tableDTO);
        Mockito.when(tableService.getAllTablesByRestaurantId("rest123")).thenReturn(tables);

        mockMvc.perform(get("/tables/rest123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tableNumber").value("No003"))
                .andExpect(jsonPath("$[0].capacity").value(4));
    }

//    TC-003
    @Test
    public void getTablesByRestaurantId_InvalidRestaurantId_ShouldReturnEmptyList() throws Exception {
        // Mock tableService to return an empty list for an invalid restaurant ID
        Mockito.when(tableService.getAllTablesByRestaurantId("invalidRestId")).thenReturn(Arrays.asList());

        mockMvc.perform(get("/tables/invalidRestId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
