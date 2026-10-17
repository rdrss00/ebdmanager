package ebdmanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para cadastro e atualização de alunos da EBD.
 */
public class StudentRequestDTO {

    @NotBlank(message = "O nome do aluno é obrigatório")
    @Size(min = 2, max = 150, message = "O nome do aluno deve conter entre 2 e 150 caracteres")
    private String nome;

    @NotNull(message = "A idade do aluno é obrigatória")
    @Min(value = 0, message = "A idade do aluno não pode ser negativa")
    private Integer idade;

    @NotNull(message = "O identificador da classe (ebdClassId) é obrigatório")
    private Long ebdClassId;

    public StudentRequestDTO() {
    }

    public StudentRequestDTO(String nome, Integer idade, Long ebdClassId) {
        this.nome = nome;
        this.idade = idade;
        this.ebdClassId = ebdClassId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public Long getEbdClassId() {
        return ebdClassId;
    }

    public void setEbdClassId(Long ebdClassId) {
        this.ebdClassId = ebdClassId;
    }
}
