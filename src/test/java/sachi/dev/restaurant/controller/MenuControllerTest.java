package sachi.dev.restaurant.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import sachi.dev.restaurant.dto.CategoryDTO;
import sachi.dev.restaurant.dto.MenuDTO;
import sachi.dev.restaurant.service.MenuService;
import sachi.dev.restaurant.service.SearchService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @MockBean
    private SearchService searchService;

    private MenuDTO menuDTO;

    @BeforeEach
    public void setUp() {
        menuDTO = new MenuDTO();
        menuDTO.setName("Pizza");
        menuDTO.setPrice(12.99);
        menuDTO.setDescription("Delicious cheese pizza");
    }

//    TC-001
    @Test
    @WithMockUser(username = "staffUser", roles = {"STAFF"})
    public void createMenu_ValidStaffMember_ShouldReturnCreated() throws Exception {
        Mockito.when(menuService.save(any(MenuDTO.class), eq("categoryId"))).thenReturn(menuDTO);

        mockMvc.perform(post("/api/staff/menus")
                        .param("categoryId", "categoryId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(menuDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

//    TC-002
    @Test
    @WithMockUser(username = "customerUser", roles = {"CUSTOMER"})
    public void createMenu_InvalidRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/staff/menus")
                        .param("categoryId", "categoryId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(menuDTO)))
                .andExpect(status().isForbidden());
    }

//    TC-003
    @Test
    public void createMenu_NoAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/staff/menus")
                        .param("categoryId", "categoryId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(menuDTO)))
                .andExpect(status().isUnauthorized());
    }

//    TC-004
    @Test
    @WithMockUser(username = "staffUser", roles = {"STAFF"})
    public void updateMenu_ValidRequest_ShouldReturnOk() throws Exception {
        Mockito.when(menuService.update(any(MenuDTO.class), eq("menuId"))).thenReturn(menuDTO);

        mockMvc.perform(put("/api/staff/menus/menuId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(menuDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

//    TC-005
    @Test
    @WithMockUser(username = "staffUser", roles = {"STAFF"})
    public void getCategoryByMenuId_ShouldReturnCategory() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("Main Course");
        Mockito.when(menuService.findCategoryByMenuId("menuId")).thenReturn(categoryDTO);

        mockMvc.perform(get("/menuId/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Main Course"));
    }

//    TC-006
    @Test
    @WithMockUser(username = "staffUser", roles = {"STAFF"})
    public void addMenuToRestaurant_ValidRequest_ShouldReturnOk() throws Exception {
        Mockito.doNothing().when(menuService).addMenuToRestaurant("restaurantId", Arrays.asList("menuId1", "menuId2"));

        mockMvc.perform(post("/api/staff/add-to-restaurant/restaurantId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Arrays.asList("menuId1", "menuId2"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Menus added to restaurant successfully."));
    }

//    TC-007
    @Test
    @WithMockUser(username = "staffUser", roles = {"STAFF"})
    public void searchMenus_ShouldReturnSearchResults() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MenuDTO> pageResults = new PageImpl<>(Arrays.asList(menuDTO));
        Mockito.when(searchService.searchMenus("Pizza", 0.0, 100.0, "Main Course", pageable)).thenReturn(pageResults);

        mockMvc.perform(post("/api/search/menus")
                        .param("name", "Pizza")
                        .param("minPrice", "0.0")
                        .param("maxPrice", "100.0")
                        .param("categoryName", "Main Course")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results.content[0].name").value("Pizza"));
    }
}
