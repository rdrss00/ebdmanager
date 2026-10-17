package ebdmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ebdmanager.config.SecurityConfig;
import ebdmanager.dto.AttendanceCreateDTO;
import ebdmanager.dto.AttendanceRecordItemDTO;
import ebdmanager.dto.AttendanceResponseDTO;
import ebdmanager.service.AttendanceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AttendanceController.class)
@Import(SecurityConfig.class)
class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private AttendanceService attendanceService;

    @Test
    @DisplayName("Deve registrar a chamada de uma turma com sucesso")
    void recordAttendance_Success() throws Exception {
        AttendanceCreateDTO request = new AttendanceCreateDTO(
                1L,
                LocalDateTime.now(),
                10.0,
                List.of(new AttendanceRecordItemDTO(1L, true, true, true, 10.0))
        );

        AttendanceResponseDTO response = new AttendanceResponseDTO(
                100L,
                1L,
                "Jovens",
                request.getCreatedAt(),
                10.0,
                1,
                0,
                List.of()
        );

        when(attendanceService.recordAttendance(any(AttendanceCreateDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/attendances")
                        .with(httpBasic("user", "user123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.ebdClassName").value("Jovens"))
                .andExpect(jsonPath("$.totalOffering").value(10.0));
    }

    @Test
    @DisplayName("Deve buscar chamada por ID")
    void getAttendanceById_Success() throws Exception {
        AttendanceResponseDTO response = new AttendanceResponseDTO(
                100L,
                1L,
                "Jovens",
                LocalDateTime.now(),
                10.0,
                1,
                0,
                List.of()
        );

        when(attendanceService.getAttendanceById(100L)).thenReturn(response);

        mockMvc.perform(get("/api/attendances/100")
                        .with(httpBasic("user", "user123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.ebdClassName").value("Jovens"));
    }
}
