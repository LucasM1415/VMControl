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


    private static final int LIMITE_MAXIMO_VMS = 5;

    @Override
    public VmResponseDTO criarVm(VmRequestDTO vmRequest) {

        validarLimiteVms();


        if (vmRepository.existsByNome(vmRequest.getNome())) {
            throw VmValidationException.nomeJaExiste(vmRequest.getNome());
        }

        validarRecursosVm(vmRequest);

        Vm vm = vmMapper.toEntity(vmRequest);
        vm.setStatus(VMStatus.STOPPED);


        simularUsoRecursos(vm);

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
                .orElseThrow(() -> new VmNotFoundException(id));
        return vmMapper.toResponseDTO(vm);
    }

    @Override
    public VmResponseDTO atualizarVm(Long id, VmRequestDTO vmRequest) {
        Vm vm = vmRepository.findById(id)
                .orElseThrow(() -> new VmNotFoundException(id));


        if (!vm.getNome().equals(vmRequest.getNome()) &&
                vmRepository.existsByNome(vmRequest.getNome())) {
            throw VmValidationException.nomeJaExiste(vmRequest.getNome());
        }


        validarRecursosVm(vmRequest);

        vmMapper.updateEntity(vmRequest, vm);
        Vm updatedVm = vmRepository.save(vm);

        return vmMapper.toResponseDTO(updatedVm);
    }

    @Override
    public void deletarVm(Long id) {
        if (!vmRepository.existsById(id)) {
            throw new VmNotFoundException(id);
        }
        vmRepository.deleteById(id);
    }

    @Override
    public VmResponseDTO alterarStatus(Long id, VMStatus status) {
        Vm vm = vmRepository.findById(id)
                .orElseThrow(() -> new VmNotFoundException(id));


        if (status == null) {
            throw VmValidationException.statusInvalido("null");
        }

        vm.setStatus(status);


        simularUsoRecursos(vm);

        Vm updatedVm = vmRepository.save(vm);

        return vmMapper.toResponseDTO(updatedVm);
    }


    private void validarLimiteVms() {
        long totalVms = vmRepository.count();
        if (totalVms >= LIMITE_MAXIMO_VMS) {
            throw new VmValidationException(
                    String.format("Limite máximo de %d VMs atingido. VMs atuais: %d",
                            LIMITE_MAXIMO_VMS, totalVms)
            );
        }
    }

    private void validarRecursosVm(VmRequestDTO vmRequest) {
        if (vmRequest.getCpu() == null || vmRequest.getCpu() <= 0) {
            throw VmValidationException.cpuInvalida(vmRequest.getCpu());
        }
        if (vmRequest.getMemoriaRam() == null || vmRequest.getMemoriaRam() <= 0) {
            throw VmValidationException.memoriaInvalida(vmRequest.getMemoriaRam());
        }
        if (vmRequest.getTamanhoDisco() == null || vmRequest.getTamanhoDisco() < 20) {
            throw VmValidationException.discoInvalido(vmRequest.getTamanhoDisco());
        }
    }

    private void simularUsoRecursos(Vm vm) {
        // Valores aleatórios baseados no status
        switch (vm.getStatus()) {
            case STARTED:
                vm.setCpuUso(randomEntre(30.0, 80.0));
                vm.setMemoriaUso(randomEntre(40.0, 90.0));
                vm.setDiscoUso(randomEntre(10.0, 60.0));
                break;

            case SUSPENDED:
                vm.setCpuUso(0.0);
                vm.setMemoriaUso(randomEntre(70.0, 90.0));
                vm.setDiscoUso(randomEntre(10.0, 30.0));
                break;

            case STOPPED:
            default:
                vm.setCpuUso(0.0);
                vm.setMemoriaUso(0.0);
                vm.setDiscoUso(randomEntre(5.0, 20.0));
                break;
        }
    }

    private Double randomEntre(Double min, Double max) {
        return Math.round((min + (Math.random() * (max - min))) * 10.0) / 10.0;
    }
}