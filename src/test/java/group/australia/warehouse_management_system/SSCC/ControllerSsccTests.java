package group.australia.warehouse_management_system.SSCC;

import group.australia.warehouse_management_system.feature.SSCC.controller.SsccController;
import group.australia.warehouse_management_system.feature.SSCC.dto.RequestSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.dto.ResponseSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.service.SsccService;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.ResponseCartonHeaderDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = SsccController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ControllerSsccTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SsccService ssccService;

    @Autowired
    private ObjectMapper objectMapper;

    private ResponseSsccDto responseDto;

    @BeforeEach
    void setUp() {
        responseDto = ResponseSsccDto.builder()
                .id(1L)
                .sscc("1234567890123456789")
                .cartonHeader(ResponseCartonHeaderDto.builder()
                        .id(1L)
                        .barcode("13431324532")
                        .description("Test Description")
                        .version(0)
                        .build())
                .build();
    }

    @Test
    void SsccController_CreateSscc_ReturnsCreated() throws Exception {
        RequestSsccDto req = RequestSsccDto.builder()
                .sscc("1234567890123456789")
                .build();

        given(ssccService.insertNewSscc(anyString(), any(RequestSsccDto.class))).willReturn(responseDto);

        ResultActions ra = mockMvc.perform(post("/warehouses/13431324532/ssccs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)));

        ra.andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
