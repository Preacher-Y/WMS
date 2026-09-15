package group.australia.warehouse_management_system.warehouse;

import group.australia.warehouse_management_system.feature.warehouse.dto.UpdateWarehouseDto;
import tools.jackson.databind.ObjectMapper;
import group.australia.warehouse_management_system.feature.warehouse.controller.WarehouseController;
import group.australia.warehouse_management_system.feature.warehouse.dto.RequestWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.dto.ResponseWarehouseDto;
import group.australia.warehouse_management_system.feature.warehouse.service.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = WarehouseController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ControllerWarehouseTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WarehouseService warehouseService;

    @Autowired
    private ObjectMapper objectMapper;

    private ResponseWarehouseDto dto;
    private ResponseWarehouseDto updateResDto;

    @BeforeEach
    void setUp() {
        dto = ResponseWarehouseDto.builder()
                .id(1L)
                .name("Test Warehouse 1")
                .warehouseNumber("WH-001")
                .active(true)
                .build();
        updateResDto = ResponseWarehouseDto.builder()
                .id(1L)
                .name("Test Warehouse 1 - Update")
                .warehouseNumber("WH-001")
                .active(false)
                .build();
    }


    @Test
    void WarehouseController_CreateWarehouse_ReturnCreated() throws Exception {
        RequestWarehouseDto requestDto = RequestWarehouseDto.builder()
                .name("Test Warehouse 1")
                .warehouseNumber(1)
                .build();

        given(warehouseService.insertNewWarehouse(ArgumentMatchers.any())).willReturn(dto);

        ResultActions response = mockMvc.perform(post("/warehouses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void WarehouseController_UpdateWarehouse_ReturnOk() throws Exception {
        UpdateWarehouseDto updateDto = UpdateWarehouseDto.builder()
                .name("Test Warehouse 1 - Update")
                .active(false)
                .build();

        given(warehouseService.updateWarehouse(
                ArgumentMatchers.eq(1L),
                ArgumentMatchers.any(UpdateWarehouseDto.class)
                )).willReturn(updateResDto);

        ResultActions response = mockMvc.perform(put("/warehouses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
