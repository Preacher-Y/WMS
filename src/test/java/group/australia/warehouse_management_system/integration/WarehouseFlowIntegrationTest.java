package group.australia.warehouse_management_system.integration;

import group.australia.warehouse_management_system.feature.auth.dto.LoginRequestDto;
import group.australia.warehouse_management_system.feature.auth.dto.LoginResponseDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.CreateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.item.service.ItemService;
import group.australia.warehouse_management_system.feature.location.dto.CreateLocationDto;
import group.australia.warehouse_management_system.feature.location.repository.LocationRepositoryAdapter;
import group.australia.warehouse_management_system.feature.moveItem.dto.RequestMoveItemDto;
import group.australia.warehouse_management_system.feature.recieve.dto.CreateRecieverDto;
import group.australia.warehouse_management_system.feature.SSCC.dto.CreateSsccDto;
import group.australia.warehouse_management_system.feature.stockHistory.domain.StockAction;
import group.australia.warehouse_management_system.feature.stockHistory.service.StockHistoryService;
import group.australia.warehouse_management_system.feature.user.domain.Role;
import group.australia.warehouse_management_system.feature.user.domain.User;
import group.australia.warehouse_management_system.feature.user.dto.CreateUserDto;
import group.australia.warehouse_management_system.feature.user.repository.UserRepositoryPort;
import group.australia.warehouse_management_system.feature.warehouse.dto.CreateWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.ResponseWarehouseDto;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Autowired
    private ItemService itemService;

    @Autowired
    private LocationRepositoryAdapter locationRepositoryAdapter;

    @Autowired
    private StockHistoryService stockHistoryService;

    @Autowired
    private UserRepositoryPort userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ADMIN_EMAIL = "admin@test.com";
    private static final String ADMIN_PASSWORD = "adminPass123";

    @BeforeEach
    void seedAdmin() {
        userRepository.save(User.builder()
                .email(ADMIN_EMAIL)
                .name("Test Admin")
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .role(Role.ADMIN)
                .build());
    }

    private String adminToken() throws Exception {
        var result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequestDto(ADMIN_EMAIL, ADMIN_PASSWORD))))
                .andExpect(status().isOk())
                .andReturn();
        return "Bearer " + objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponseDto.class).accessToken();
    }

    @Test
    void Should_complete_user_creation_login_and_warehouse_creation_flow() throws Exception {
        var createUserReq = new CreateUserDto("integration@test.com", "Integration User", "testPass123");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("integration@test.com"))
                .andExpect(jsonPath("$.active").value(true));

        var token = adminToken();

        mockMvc.perform(post("/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(CreateWarehouseDto.builder()
                                .name("Integration Warehouse")
                                .warehouseNumber(1)
                                .build())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.warehouseNumber").value("WH-001"))
                .andExpect(jsonPath("$.name").value("Integration Warehouse"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void Should_complete_receive_and_move_flow_with_stock_history() throws Exception {
        var token = adminToken();

        // create warehouse
        var warehouseResult = mockMvc.perform(post("/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(CreateWarehouseDto.builder().name("Flow Warehouse").warehouseNumber(1).build())))
                .andExpect(status().isCreated())
                .andReturn();
        var warehouseId = objectMapper.readValue(warehouseResult.getResponse().getContentAsString(), ResponseWarehouseDto.class).id();

        // create source and destination locations
        mockMvc.perform(post("/warehouses/" + warehouseId + "/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new CreateLocationDto(1, 1, 1))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/warehouses/" + warehouseId + "/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new CreateLocationDto(2, 2, 2))))
                .andExpect(status().isCreated());

        // create carton header and two SSCCs
        mockMvc.perform(post("/carton-headers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new CreateCartonHeaderDto("BC-FLOW-001", "Flow Test Carton"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/carton-headers/BC-FLOW-001/ssccs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new CreateSsccDto("SSCC-FLOW-001"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/carton-headers/BC-FLOW-001/ssccs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new CreateSsccDto("SSCC-FLOW-002"))))
                .andExpect(status().isCreated());

        // receive both SSCCs into source location
        mockMvc.perform(post("/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new CreateRecieverDto("1", "SSCC-FLOW-001", "1-1-1"))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new CreateRecieverDto("1", "SSCC-FLOW-002", "1-1-1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(2));

        // move 1 unit to destination
        mockMvc.perform(post("/api/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(RequestMoveItemDto.builder()
                                .warehouseNumber("WH-001")
                                .itemNumber("BC-FLOW-001")
                                .fromLocationCode("1-1-1")
                                .toLocationCode("2-2-2")
                                .quantity(1)
                                .build())))
                .andExpect(status().isOk());

        // verify source quantity = 1
        var srcLoc = locationRepositoryAdapter.findByLocationCode(warehouseId, "1-1-1").get();
        var srcItem = itemService.findByItemNumberAndLocationId("BC-FLOW-001", srcLoc.getId()).get();
        assertThat(srcItem.getQuantity()).isEqualTo(1);

        // verify destination quantity = 1
        var dstLoc = locationRepositoryAdapter.findByLocationCode(warehouseId, "2-2-2").get();
        var dstItem = itemService.findByItemNumberAndLocationId("BC-FLOW-001", dstLoc.getId()).get();
        assertThat(dstItem.getQuantity()).isEqualTo(1);

        // verify StockHistory: 2 RECEIVE + 1 MOVE
        var history = stockHistoryService.find("1", "BC-FLOW-001", null);
        assertThat(history).hasSize(3);
        assertThat(history.stream().filter(h -> h.action() == StockAction.RECEIVE).count()).isEqualTo(2);
        assertThat(history.stream().filter(h -> h.action() == StockAction.MOVE).count()).isEqualTo(1);
    }
}
