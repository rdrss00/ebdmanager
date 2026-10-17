package ebdmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ebdmanager.config.SecurityConfig;
import ebdmanager.dto.EnrollmentRequestDTO;
import ebdmanager.dto.EnrollmentResponseDTO;
import ebdmanager.service.EnrollmentService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnrollmentController.class)
@Import(SecurityConfig.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private EnrollmentService enrollmentService;

    @Test
    @DisplayName("Deve matricular aluno com sucesso")
    void enrollStudent_Success() throws Exception {
        EnrollmentRequestDTO request = new EnrollmentRequestDTO(1L, 2L);
        EnrollmentResponseDTO response = new EnrollmentResponseDTO(10L, 1L, "Lucas", 2L, "Adultos", LocalDateTime.now(), LocalDateTime.now());

        when(enrollmentService.enrollStudent(any(EnrollmentRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/enrollments")
                        .with(httpBasic("user", "user123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.studentName").value("Lucas"))
                .andExpect(jsonPath("$.ebdClassName").value("Adultos"));
    }

    @Test
    @DisplayName("Deve listar matrículas por classe com sucesso")
    void getEnrollmentsByClass_Success() throws Exception {
        EnrollmentResponseDTO response = new EnrollmentResponseDTO(10L, 1L, "Lucas", 2L, "Adultos", LocalDateTime.now(), LocalDateTime.now());
        when(enrollmentService.getEnrollmentsByClass(2L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/enrollments/class/2")
                        .with(httpBasic("user", "user123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].studentName").value("Lucas"));
    }

    @Test
    @DisplayName("Deve cancelar matrícula com perfil ADMIN")
    void cancelEnrollment_Admin_Success() throws Exception {
        mockMvc.perform(delete("/api/enrollments/10")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isNoContent());

        verify(enrollmentService).cancelEnrollment(10L);
    }
}
