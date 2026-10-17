package ebdmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ebdmanager.config.SecurityConfig;
import ebdmanager.dto.ClassRequestDTO;
import ebdmanager.dto.ClassResponseDTO;
import ebdmanager.service.ClassService;
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

@WebMvcTest(ClassController.class)
@Import(SecurityConfig.class)
class ClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private ClassService classService;

    @Test
    @DisplayName("Deve retornar 401 Unauthorized quando não autenticado")
    void getClasses_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/classes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve listar classes quando autenticado com perfil USER")
    void getClasses_Authorized() throws Exception {
        ClassResponseDTO dto = new ClassResponseDTO(1L, "Jovens", LocalDateTime.now(), LocalDateTime.now(), 5L);
        when(classService.getAllClasses()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/classes")
                        .with(httpBasic("user", "user123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Jovens"))
                .andExpect(jsonPath("$[0].totalStudents").value(5L));
    }

    @Test
    @DisplayName("Deve criar nova classe quando autenticado com perfil ADMIN")
    void createClass_Success() throws Exception {
        ClassRequestDTO request = new ClassRequestDTO("Adolescentes");
        ClassResponseDTO response = new ClassResponseDTO(2L, "Adolescentes", LocalDateTime.now(), LocalDateTime.now(), 0L);

        when(classService.createClass(any(ClassRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/classes")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nome").value("Adolescentes"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar criar classe com nome inválido")
    void createClass_ValidationError() throws Exception {
        ClassRequestDTO request = new ClassRequestDTO("");

        mockMvc.perform(post("/api/classes")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Deve excluir classe com perfil ADMIN")
    void deleteClass_Admin_Success() throws Exception {
        mockMvc.perform(delete("/api/classes/1")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isNoContent());

        verify(classService).deleteClass(1L);
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden ao tentar excluir classe com perfil USER")
    void deleteClass_User_Forbidden() throws Exception {
        mockMvc.perform(delete("/api/classes/1")
                        .with(httpBasic("user", "user123")))
                .andExpect(status().isForbidden());
    }
}
