package com.example.vmcontrol.model.dto;

import com.example.vmcontrol.model.enums.VMStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VmResponseDTO {
    private Long id;
    private String nome;
    private Integer cpu;
    private Integer memoriaRam;
    private Integer tamanhoDisco;
    private VMStatus status;
    private Double cpuUso;
    private Double memoriaUso;
    private Double discoUso;
    private LocalDateTime ultimaAtualizacao;
    private LocalDateTime dataCriacao;
}