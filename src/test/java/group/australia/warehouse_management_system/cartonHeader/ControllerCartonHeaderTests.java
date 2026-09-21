package group.australia.warehouse_management_system.cartonHeader;

import group.australia.warehouse_management_system.feature.cartonHeader.controller.CartonHeaderController;
import group.australia.warehouse_management_system.feature.cartonHeader.domain.CartonHeader;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.CreateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.ResponseCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.dto.UpdateCartonHeaderDto;
import group.australia.warehouse_management_system.feature.cartonHeader.mapper.CartonHeaderMapper;
import group.australia.warehouse_management_system.feature.cartonHeader.service.CartonHeaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = CartonHeaderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ControllerCartonHeaderTests {

    @MockitoBean
    private CartonHeaderService cartonHeaderService;

    @MockitoBean
    private CartonHeaderMapper cartonHeaderMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CartonHeader cartonHeaderDomain;
    private ResponseCartonHeaderDto res;
    private ResponseCartonHeaderDto updateRes;

    @BeforeEach
    void setUp() {
        cartonHeaderDomain = CartonHeader.builder()
                .id(1L)
                .barcode("123456789")
                .description("Test Description")
                .version(0)
                .build();

        res = ResponseCartonHeaderDto.builder()
                .id(1L)
                .barcode("123456789")
                .description("Test Description")
                .version(0)
                .build();

        updateRes = ResponseCartonHeaderDto.builder()
                .id(1L)
                .barcode("123456789")
                .description("Test Description - Update")
                .version(1)
                .build();
    }

    @Test
    void Should_return_201_when_creating_carton_header() throws Exception {
        CreateCartonHeaderDto req = CreateCartonHeaderDto.builder()
                .barcode("123456789")
                .description("Test Description")
                .build();

        given(cartonHeaderService.insertNewCartonHeader(Mockito.any(CreateCartonHeaderDto.class)))
                .willReturn(cartonHeaderDomain);
        given(cartonHeaderMapper.toDto(any(CartonHeader.class))).willReturn(res);

        ResultActions ra = mockMvc.perform(post("/carton-headers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)));

        ra.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void Should_return_200_when_updating_carton_header() throws Exception {
        UpdateCartonHeaderDto update = UpdateCartonHeaderDto.builder()
                .description("Test Description - Update")
                .build();

        given(cartonHeaderService.updateCartonHeader(Mockito.anyString(), Mockito.any(UpdateCartonHeaderDto.class)))
                .willReturn(cartonHeaderDomain);
        given(cartonHeaderMapper.toDto(any(CartonHeader.class))).willReturn(updateRes);

        ResultActions ra = mockMvc.perform(put("/carton-headers/123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)));

        ra.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
