package ebdmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para criação e atualização de classes da EBD.
 */
public class ClassRequestDTO {

    @NotBlank(message = "O nome da classe é obrigatório")
    @Size(min = 2, max = 100, message = "O nome da classe deve conter entre 2 e 100 caracteres")
    private String nome;

    public ClassRequestDTO() {
    }

    public ClassRequestDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
