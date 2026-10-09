package group.australia.warehouse_management_system.integration;

import group.australia.warehouse_management_system.feature.auth.dto.LoginRequestDto;
import group.australia.warehouse_management_system.feature.auth.dto.LoginResponseDto;
import group.australia.warehouse_management_system.feature.user.dto.CreateUserDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.CreateWarehouseDto;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WarehouseFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void Should_complete_user_creation_login_and_warehouse_creation_flow() throws Exception {
        var createUserReq = new CreateUserDto("integration@test.com", "Integration User", "testPass123");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("integration@test.com"))
                .andExpect(jsonPath("$.active").value(true));

        var loginReq = new LoginRequestDto("integration@test.com", "testPass123");

        var loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        var loginResponse = objectMapper.readValue(
                loginResult.getResponse().getContentAsString(), LoginResponseDto.class);

        var createWarehouseReq = CreateWarehouseDto.builder()
                .name("Integration Warehouse")
                .warehouseNumber(1)
                .build();

        mockMvc.perform(post("/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + loginResponse.accessToken())
                        .content(objectMapper.writeValueAsString(createWarehouseReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.warehouseNumber").value("WH-001"))
                .andExpect(jsonPath("$.name").value("Integration Warehouse"))
                .andExpect(jsonPath("$.active").value(true));
    }
}
