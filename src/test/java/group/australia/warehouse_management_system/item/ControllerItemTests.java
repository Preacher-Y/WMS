package group.australia.warehouse_management_system.item;

import group.australia.warehouse_management_system.feature.item.controller.ItemController;
import group.australia.warehouse_management_system.feature.item.domain.Item;
import group.australia.warehouse_management_system.feature.item.dto.ItemRequestDto;
import group.australia.warehouse_management_system.feature.item.dto.ResponseItemDto;
import group.australia.warehouse_management_system.feature.item.mapper.ItemMapper;
import group.australia.warehouse_management_system.feature.item.service.ItemService;
import group.australia.warehouse_management_system.common.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    @MockitoBean
    private ItemMapper itemMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private Item itemDomain;
    private ResponseItemDto responseDto;

    @BeforeEach
    void setUp() {
        itemDomain = Item.builder()
                .id(1L).itemNumber("ITEM-001").quantity(10).build();

        responseDto = ResponseItemDto.builder()
                .id(1L).itemNumber("ITEM-001").quantity(10)
                .locationLocationCode("1-2-3").cartonHeaderBarcode("BC-001")
                .build();
    }

    @Test
    void Should_return_201_when_creating_item() throws Exception {
        var req = new ItemRequestDto("ITEM-001", 10, 1L, 1L);

        given(itemService.insertNewItem(any(ItemRequestDto.class))).willReturn(itemDomain);
        given(itemMapper.toDto(any(Item.class))).willReturn(responseDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }
}
