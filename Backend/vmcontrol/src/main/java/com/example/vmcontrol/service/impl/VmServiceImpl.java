package com.example.vmcontrol.service.impl;

import com.example.vmcontrol.model.dto.VmRequestDTO;
import com.example.vmcontrol.model.dto.VmResponseDTO;
import com.example.vmcontrol.model.entity.Vm;
import com.example.vmcontrol.model.enums.VMStatus;
import com.example.vmcontrol.repository.VmRepository;
import com.example.vmcontrol.service.VmService;
import com.example.vmcontrol.service.exception.VmNotFoundException;
import com.example.vmcontrol.service.exception.VmValidationException;
import com.example.vmcontrol.util.mapper.VmMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VmServiceImpl implements VmService {

    private final VmRepository vmRepository;
    private final VmMapper vmMapper;

    @Override
    public VmResponseDTO criarVm(VmRequestDTO vmRequest) {
        // Validação de nome único
        if (vmRepository.existsByNome(vmRequest.getNome())) {
            throw new VmValidationException("Já existe uma VM com o nome: " + vmRequest.getNome());
        }

        Vm vm = vmMapper.toEntity(vmRequest);
        vm.setStatus(VMStatus.STOPPED); // Status inicial
        Vm savedVm = vmRepository.save(vm);

        return vmMapper.toResponseDTO(savedVm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VmResponseDTO> listarTodasVms() {
        return vmRepository.findAll()
                .stream()
                .map(vmMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VmResponseDTO buscarPorId(Long id) {
        Vm vm = vmRepository.findById(id)
                .orElseThrow(() -> new VmNotFoundException("VM não encontrada com ID: " + id));
        return vmMapper.toResponseDTO(vm);
    }

    @Override
    public VmResponseDTO atualizarVm(Long id, VmRequestDTO vmRequest) {
        Vm vm = vmRepository.findById(id)
                .orElseThrow(() -> new VmNotFoundException("VM não encontrada com ID: " + id));

        // Valida nome único (exceto para própria VM)
        if (!vm.getNome().equals(vmRequest.getNome()) &&
                vmRepository.existsByNomeAndIdNot(vmRequest.getNome(), id)) {
            throw new VmValidationException("Já existe outra VM com o nome: " + vmRequest.getNome());
        }

        vmMapper.updateEntity(vmRequest, vm);
        Vm updatedVm = vmRepository.save(vm);

        return vmMapper.toResponseDTO(updatedVm);
    }

    @Override
    public void deletarVm(Long id) {
        if (!vmRepository.existsById(id)) {
            throw new VmNotFoundException("VM não encontrada com ID: " + id);
        }
        vmRepository.deleteById(id);
    }

    @Override
    public VmResponseDTO alterarStatus(Long id, VMStatus status) {
        Vm vm = vmRepository.findById(id)
                .orElseThrow(() -> new VmNotFoundException("VM não encontrada com ID: " + id));

        vm.setStatus(status);
        Vm updatedVm = vmRepository.save(vm);

        return vmMapper.toResponseDTO(updatedVm);
    }
}