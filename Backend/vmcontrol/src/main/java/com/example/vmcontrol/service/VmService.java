package com.example.vmcontrol.service;

import com.example.vmcontrol.model.dto.VmRequestDTO;
import com.example.vmcontrol.model.dto.VmResponseDTO;
import com.example.vmcontrol.model.enums.VMStatus;
import java.util.List;

public interface VmService {
    VmResponseDTO criarVm(VmRequestDTO vmRequest);
    List<VmResponseDTO> listarTodasVms();
    VmResponseDTO buscarPorId(Long id);
    VmResponseDTO atualizarVm(Long id, VmRequestDTO vmRequest);
    void deletarVm(Long id);
    VmResponseDTO alterarStatus(Long id, VMStatus status);
}