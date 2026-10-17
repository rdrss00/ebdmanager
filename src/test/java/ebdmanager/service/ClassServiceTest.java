package ebdmanager.service;

import ebdmanager.dto.ClassRequestDTO;
import ebdmanager.dto.ClassResponseDTO;
import ebdmanager.exception.BusinessRuleException;
import ebdmanager.exception.ResourceNotFoundException;
import ebdmanager.model.EbdClass;
import ebdmanager.repository.EbdClassRepository;
import ebdmanager.repository.EbdStudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassServiceTest {

    @Mock
    private EbdClassRepository classRepository;

    @Mock
    private EbdStudentRepository studentRepository;

    @InjectMocks
    private ClassService classService;

    @Test
    @DisplayName("Deve criar uma classe com sucesso")
    void createClass_Success() {
        ClassRequestDTO request = new ClassRequestDTO("Jovens");
        EbdClass savedClass = new EbdClass(1L, "Jovens");
        savedClass.setCreatedAt(LocalDateTime.now());
        savedClass.setUpdatedAt(LocalDateTime.now());

        when(classRepository.existsByNomeIgnoreCase("Jovens")).thenReturn(false);
        when(classRepository.save(any(EbdClass.class))).thenReturn(savedClass);
        when(studentRepository.countByEbdClassId(1L)).thenReturn(0L);

        ClassResponseDTO response = classService.createClass(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Jovens", response.getNome());
        assertEquals(0L, response.getTotalStudents());
        verify(classRepository).save(any(EbdClass.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessRuleException quando já existir classe com mesmo nome")
    void createClass_ThrowsBusinessRuleException_WhenDuplicateName() {
        ClassRequestDTO request = new ClassRequestDTO("Jovens");
        when(classRepository.existsByNomeIgnoreCase("Jovens")).thenReturn(true);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () ->
                classService.createClass(request));

        assertEquals("Já existe uma classe cadastrada com o nome: Jovens", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar classe por ID com sucesso")
    void getClassById_Success() {
        EbdClass ebdClass = new EbdClass(1L, "Adultos");
        ebdClass.setCreatedAt(LocalDateTime.now());
        ebdClass.setUpdatedAt(LocalDateTime.now());

        when(classRepository.findById(1L)).thenReturn(Optional.of(ebdClass));
        when(studentRepository.countByEbdClassId(1L)).thenReturn(5L);

        ClassResponseDTO response = classService.getClassById(1L);

        assertNotNull(response);
        assertEquals("Adultos", response.getNome());
        assertEquals(5L, response.getTotalStudents());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando classe não for encontrada")
    void getClassById_NotFound() {
        when(classRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> classService.getClassById(99L));
    }

    @Test
    @DisplayName("Deve listar todas as classes")
    void getAllClasses_Success() {
        EbdClass c1 = new EbdClass(1L, "Classe 1");
        EbdClass c2 = new EbdClass(2L, "Classe 2");

        when(classRepository.findAll()).thenReturn(List.of(c1, c2));
        when(studentRepository.countByEbdClassId(1L)).thenReturn(3L);
        when(studentRepository.countByEbdClassId(2L)).thenReturn(7L);

        List<ClassResponseDTO> result = classService.getAllClasses();

        assertEquals(2, result.size());
        assertEquals("Classe 1", result.get(0).getNome());
        assertEquals(3L, result.get(0).getTotalStudents());
    }

    @Test
    @DisplayName("Deve atualizar o nome da classe com sucesso")
    void updateClass_Success() {
        EbdClass ebdClass = new EbdClass(1L, "Nome Antigo");
        ClassRequestDTO request = new ClassRequestDTO("Nome Novo");

        when(classRepository.findById(1L)).thenReturn(Optional.of(ebdClass));
        when(classRepository.existsByNomeIgnoreCase("Nome Novo")).thenReturn(false);
        when(classRepository.save(any(EbdClass.class))).thenReturn(ebdClass);
        when(studentRepository.countByEbdClassId(1L)).thenReturn(2L);

        ClassResponseDTO response = classService.updateClass(1L, request);

        assertEquals("Nome Novo", response.getNome());
        verify(classRepository).save(ebdClass);
    }

    @Test
    @DisplayName("Deve excluir classe com sucesso")
    void deleteClass_Success() {
        EbdClass ebdClass = new EbdClass(1L, "Classe a Excluir");
        when(classRepository.findById(1L)).thenReturn(Optional.of(ebdClass));

        classService.deleteClass(1L);

        verify(classRepository).delete(ebdClass);
    }
}
