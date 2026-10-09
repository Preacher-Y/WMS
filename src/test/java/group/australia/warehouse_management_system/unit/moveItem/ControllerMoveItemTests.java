package group.australia.warehouse_management_system.unit.moveItem;

import group.australia.warehouse_management_system.feature.moveItem.controller.MoveItemController;
import group.australia.warehouse_management_system.feature.moveItem.dto.RequestMoveItemDto;
import group.australia.warehouse_management_system.feature.moveItem.exception.MoveItemInsufficientStockException;
import group.australia.warehouse_management_system.feature.moveItem.service.MoveItemService;
import group.australia.warehouse_management_system.feature.warehouse.exception.WarehouseNotFoundException;
import group.australia.warehouse_management_system.common.security.JwtService;
import tools.jackson.databind.ObjectMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = MoveItemController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ControllerMoveItemTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MoveItemService moveItemService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private RequestMoveItemDto validRequest;

    @BeforeEach
    void setUp() {
        validRequest = RequestMoveItemDto.builder()
                .warehouseNumber("WH-001")
                .itemNumber("ITEM-001")
                .fromLocationCode("1-1-1")
                .toLocationCode("2-2-2")
                .quantity(5)
                .build();
    }

    @Test
    void Should_return_200_when_move_is_successful() throws Exception {
        willDoNothing().given(moveItemService).moveItem(any(RequestMoveItemDto.class));

        mockMvc.perform(post("/api/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void Should_return_400_when_quantity_is_zero() throws Exception {
        var badRequest = RequestMoveItemDto.builder()
                .warehouseNumber("WH-001")
                .itemNumber("ITEM-001")
                .fromLocationCode("1-1-1")
                .toLocationCode("2-2-2")
                .quantity(0)
                .build();

        mockMvc.perform(post("/api/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void Should_return_400_when_warehouse_number_is_blank() throws Exception {
        var badRequest = RequestMoveItemDto.builder()
                .warehouseNumber("")
                .itemNumber("ITEM-001")
                .fromLocationCode("1-1-1")
                .toLocationCode("2-2-2")
                .quantity(5)
                .build();

        mockMvc.perform(post("/api/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void Should_return_404_when_warehouse_not_found() throws Exception {
        willThrow(new WarehouseNotFoundException("WH-999"))
                .given(moveItemService).moveItem(any(RequestMoveItemDto.class));

        mockMvc.perform(post("/api/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void Should_return_400_when_insufficient_stock() throws Exception {
        willThrow(new MoveItemInsufficientStockException("ITEM-001", 3, 10))
                .given(moveItemService).moveItem(any(RequestMoveItemDto.class));

        mockMvc.perform(post("/api/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void Should_return_409_when_concurrent_conflict() throws Exception {
        willThrow(new ObjectOptimisticLockingFailureException(Object.class, null))
                .given(moveItemService).moveItem(any(RequestMoveItemDto.class));

        mockMvc.perform(post("/api/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
}
