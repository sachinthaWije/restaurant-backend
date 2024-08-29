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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import sachi.dev.restaurant.dto.CategoryDTO;
import sachi.dev.restaurant.service.CategoryService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private CategoryDTO categoryDTO;

    @BeforeEach
    public void setUp() {
        categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("New Category");
    }

//    TC-001
    @Test
    @WithMockUser(username = "staffUser", roles = {"STAFF"})
    public void createCategory_ValidStaffMember_ShouldReturnCreated() throws Exception {
        // Mocking the service call
        Mockito.when(categoryService.save(any(CategoryDTO.class))).thenReturn(categoryDTO);

        // Performing the POST request to create a category
        mockMvc.perform(post("/api/staff/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryName").value("New Category"));
    }

//    TC-002
    @Test
    @WithMockUser(username = "customerUser", roles = {"CUSTOMER"})
    public void createCategory_InvalidRole_ShouldReturnForbidden() throws Exception {
        // Attempting to create a category with an invalid role
        mockMvc.perform(post("/api/staff/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDTO)))
                .andExpect(status().isForbidden());
    }




//    TC-003
    @Test
    public void createCategory_NoAuthentication_ShouldReturnUnauthorized() throws Exception {
        // Attempting to create a category without being authenticated
        mockMvc.perform(post("/api/staff/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryDTO)))
                .andExpect(status().isUnauthorized());
    }
}
