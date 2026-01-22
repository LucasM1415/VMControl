package com.example.vmcontrol.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VmValidationException extends RuntimeException {

    public VmValidationException(String message) {
        super(message);
    }

    public VmValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static VmValidationException nomeJaExiste(String nome) {
        return new VmValidationException("Já existe uma VM com o nome: " + nome);
    }

    public static VmValidationException cpuInvalida(Integer cpu) {
        return new VmValidationException("CPU deve ser maior que zero. Valor informado: " + cpu);
    }

    public static VmValidationException memoriaInvalida(Integer memoria) {
        return new VmValidationException("Memória deve ser maior que zero. Valor informado: " + memoria);
    }

    public static VmValidationException discoInvalido(Integer disco) {
        return new VmValidationException("Disco deve ser maior que zero. Valor informado: " + disco);
    }


    public static VmValidationException statusInvalido(String status) {
        return new VmValidationException(
                "Status inválido: " + status +
                        ". Status válidos: STARTED, STOPPED, PAUSED"
        );
    }

    public static VmValidationException limiteVmsAtingido(int limite, int atual) {
        return new VmValidationException(
                String.format("Limite máximo de %d VMs atingido. VMs atuais: %d", limite, atual)
        );
    }
}