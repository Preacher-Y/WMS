package group.australia.warehouse_management_system.item;

import group.australia.warehouse_management_system.feature.item.controller.ItemController;
import group.australia.warehouse_management_system.feature.item.dto.ItemRequestDto;
import group.australia.warehouse_management_system.feature.item.dto.ResponseItemDto;
import group.australia.warehouse_management_system.feature.item.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ControllerItemTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ItemService itemService;

    @Test
    void ItemController_CreateItem_Returns201() throws Exception {
        var req = new ItemRequestDto("ITEM-001", 10, 1L, 1L);

        var responseDto = ResponseItemDto.builder()
                .id(1L).itemNumber("ITEM-001").quantity(10)
                .locationLocationCode("1-2-3").cartonHeaderBarcode("BC-001")
                .build();

        given(itemService.insertNewItem(any(ItemRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }
}
