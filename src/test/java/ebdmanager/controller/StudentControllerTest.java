package ebdmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ebdmanager.config.SecurityConfig;
import ebdmanager.dto.StudentRequestDTO;
import ebdmanager.dto.StudentResponseDTO;
import ebdmanager.service.StudentService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(SecurityConfig.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private StudentService studentService;

    @Test
    @DisplayName("Deve cadastrar aluno com sucesso")
    void createStudent_Success() throws Exception {
        StudentRequestDTO request = new StudentRequestDTO("Carlos", 18, 1L);
        StudentResponseDTO response = new StudentResponseDTO(1L, "Carlos", 18, 1L, "Jovens", LocalDateTime.now(), LocalDateTime.now());

        when(studentService.createStudent(any(StudentRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/students")
                        .with(httpBasic("user", "user123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Carlos"));
    }

    @Test
    @DisplayName("Deve listar alunos com sucesso")
    void getAllStudents_Success() throws Exception {
        StudentResponseDTO response = new StudentResponseDTO(1L, "Carlos", 18, 1L, "Jovens", LocalDateTime.now(), LocalDateTime.now());
        when(studentService.getAllStudents(any())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/students")
                        .with(httpBasic("user", "user123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Carlos"));
    }

    @Test
    @DisplayName("Deve buscar aluno por ID com sucesso")
    void getStudentById_Success() throws Exception {
        StudentResponseDTO response = new StudentResponseDTO(1L, "Carlos", 18, 1L, "Jovens", LocalDateTime.now(), LocalDateTime.now());
        when(studentService.getStudentById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/students/1")
                        .with(httpBasic("user", "user123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Carlos"));
    }

    @Test
    @DisplayName("Deve atualizar aluno com sucesso")
    void updateStudent_Success() throws Exception {
        StudentRequestDTO request = new StudentRequestDTO("Carlos Eduardo", 19, 1L);
        StudentResponseDTO response = new StudentResponseDTO(1L, "Carlos Eduardo", 19, 1L, "Jovens", LocalDateTime.now(), LocalDateTime.now());

        when(studentService.updateStudent(eq(1L), any(StudentRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/students/1")
                        .with(httpBasic("user", "user123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Eduardo"));
    }

    @Test
    @DisplayName("Deve excluir aluno com perfil ADMIN")
    void deleteStudent_Admin_Success() throws Exception {
        mockMvc.perform(delete("/api/students/1")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(1L);
    }
}
