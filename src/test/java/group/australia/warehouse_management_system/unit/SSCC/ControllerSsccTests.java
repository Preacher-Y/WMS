package group.australia.warehouse_management_system.unit.SSCC;

import group.australia.warehouse_management_system.feature.SSCC.controller.SsccController;
import group.australia.warehouse_management_system.feature.SSCC.domain.Sscc;
import group.australia.warehouse_management_system.feature.SSCC.dto.CreateSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.dto.ResponseSsccDto;
import group.australia.warehouse_management_system.feature.SSCC.mapper.SsccMapper;
import group.australia.warehouse_management_system.feature.SSCC.service.SsccService;
import group.australia.warehouse_management_system.common.security.JwtService;
import org.springframework.security.core.userdetails.UserDetailsService;
import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
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

    @MockitoBean
    private SsccMapper ssccMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private Sscc ssccDomain;
    private ResponseSsccDto responseDto;

    @BeforeEach
    void setUp() {
        var cartonHeader = CartonHeader.builder()
                .id(1L)
                .barcode("13431324532")
                .description("Test Description")
                .version(0)
                .build();

        ssccDomain = Sscc.builder()
                .id(1L)
                .sscc("1234567890123456789")
                .cartonHeader(cartonHeader)
                .build();

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
    void Should_return_201_when_creating_sscc() throws Exception {
        CreateSsccDto req = CreateSsccDto.builder()
                .sscc("1234567890123456789")
                .build();

        given(ssccService.insertNewSscc(anyString(), any(CreateSsccDto.class))).willReturn(ssccDomain);
        given(ssccMapper.toDto(any(Sscc.class))).willReturn(responseDto);

        ResultActions ra = mockMvc.perform(post("/carton-headers/13431324532/ssccs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)));

        ra.andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
