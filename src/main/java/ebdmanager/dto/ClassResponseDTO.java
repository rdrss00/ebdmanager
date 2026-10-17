package ebdmanager.dto;

import java.time.LocalDateTime;

/**
 * DTO para retorno de informações de uma classe da EBD.
 */
public class ClassResponseDTO {

    private Long id;
    private String nome;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long totalStudents;

    public ClassResponseDTO() {
    }

    public ClassResponseDTO(Long id, String nome, LocalDateTime createdAt, LocalDateTime updatedAt, long totalStudents) {
        this.id = id;
        this.nome = nome;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.totalStudents = totalStudents;
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

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }
}
