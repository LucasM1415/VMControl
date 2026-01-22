package com.example.vmcontrol.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VmRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 5, message = "Nome deve ter no mínimo 5 caracteres")
    private String nome;

    @NotNull(message = "CPU é obrigatório")
    @Min(value = 1, message = "CPU deve ser maior que zero")
    @Max(value = 32, message = "CPU não pode exceder 32 núcleos")
    private Integer cpu;

    @NotNull(message = "Memória é obrigatória")
    @Min(value = 1, message = "Memória deve ser maior que zero")
    @Max(value = 256, message = "Memória não pode exceder 256GB")
    private Integer memoriaRam;

    @NotNull(message = "Disco é obrigatório")
    @Min(value = 20, message = "Disco mínimo é 20GB")
    @Max(value = 2000, message = "Disco não pode exceder 2TB (2000GB)")
    private Integer tamanhoDisco;
}