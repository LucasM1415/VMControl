package com.example.vmcontrol.service;

import com.example.vmcontrol.model.dto.VirtualMachine.VmRequestDTO;
import com.example.vmcontrol.model.dto.VirtualMachine.VmResponseDTO;
import com.example.vmcontrol.model.enums.VMStatus;
import java.util.List;

public interface VmService {

    VmResponseDTO criarVm(VmRequestDTO vmRequest);
    List<VmResponseDTO> listarTodasVms();
    VmResponseDTO buscarPorId(Long id);
    VmResponseDTO atualizarVm(Long id, VmRequestDTO vmRequest);
    void deletarVm(Long id);
    VmResponseDTO alterarStatus(Long id, VMStatus status);

    VmResponseDTO criarVmParaUsuario(VmRequestDTO vmRequest, Long usuarioId);
    List<VmResponseDTO> listarVmsPorUsuario(Long usuarioId);
    VmResponseDTO buscarVmDoUsuario(Long id, Long usuarioId);
    VmResponseDTO atualizarVmDoUsuario(Long id, VmRequestDTO vmRequest, Long usuarioId);
    void deletarVmDoUsuario(Long id, Long usuarioId);
    VmResponseDTO alterarStatusDaVmDoUsuario(Long id, VMStatus status, Long usuarioId);
}