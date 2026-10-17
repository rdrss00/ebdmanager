package ebdmanager.dto;

import java.time.LocalDateTime;

/**
 * DTO para retorno de informações de um aluno da EBD.
 */
public class StudentResponseDTO {

    private Long id;
    private String nome;
    private Integer idade;
    private Long ebdClassId;
    private String ebdClassName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StudentResponseDTO() {
    }

    public StudentResponseDTO(Long id, String nome, Integer idade, Long ebdClassId, String ebdClassName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.ebdClassId = ebdClassId;
        this.ebdClassName = ebdClassName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEbdClassName() {
        return ebdClassName;
    }

    public void setEbdClassName(String ebdClassName) {
        this.ebdClassName = ebdClassName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
