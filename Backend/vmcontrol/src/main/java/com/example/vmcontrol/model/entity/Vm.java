package com.example.vmcontrol.model.entity;

import com.example.vmcontrol.model.enums.VMStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "maquinas_virtuais")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 5, message = "Nome deve ter no mínimo 5 caracteres")
    @Column(nullable = false, unique = true)
    private String nome;

    @NotNull(message = "CPU é obrigatório")
    @Min(value = 1, message = "CPU deve ser maior que zero")
    @Column(nullable = false)
    private Integer cpu;

    @NotNull(message = "Memória é obrigatória")
    @Min(value = 1, message = "Memória deve ser maior que zero")
    @Column(name = "memoria_ram", nullable = false)
    private Integer memoriaRam;

    @NotNull(message = "Disco é obrigatório")
    @Min(value = 1, message = "Disco deve ser maior que zero")
    @Column(name = "tamanho_disco", nullable = false)
    private Integer tamanhoDisco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VMStatus status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        if (status == null) {
            status = VMStatus.STOPPED;
        }
    }
}