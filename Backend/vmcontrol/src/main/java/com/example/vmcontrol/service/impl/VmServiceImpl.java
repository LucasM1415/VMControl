package com.example.vmcontrol.service.impl;

import com.example.vmcontrol.model.dto.VirtualMachine.VmRequestDTO;
import com.example.vmcontrol.model.dto.VirtualMachine.VmResponseDTO;
import com.example.vmcontrol.model.entity.Usuario;
import com.example.vmcontrol.model.entity.Vm;
import com.example.vmcontrol.model.enums.VMStatus;
import com.example.vmcontrol.repository.UsuarioRepository;
import com.example.vmcontrol.repository.VmRepository;
import com.example.vmcontrol.service.VmService;
import com.example.vmcontrol.service.exception.UsuarioException;
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
    private final UsuarioRepository usuarioRepository; // NOVO
    private final VmMapper vmMapper;

    private static final int LIMITE_MAXIMO_VMS = 5;


    @Override
    public VmResponseDTO criarVm(VmRequestDTO vmRequest) {
        validarLimiteVms();
        validarNomeUnico(vmRequest.getNome(), null);
        validarRecursosVm(vmRequest);

        Vm vm = vmMapper.toEntity(vmRequest);
        vm.setStatus(VMStatus.STOPPED);
        inicializarUsoRecursos(vm);

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

        validarNomeUnico(vmRequest.getNome(), id);
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
        atualizarUsoPorStatus(vm);
        Vm updatedVm = vmRepository.save(vm);
        return vmMapper.toResponseDTO(updatedVm);
    }


    @Override
    public VmResponseDTO criarVmParaUsuario(VmRequestDTO vmRequest, Long usuarioId) {
        Usuario usuario = buscarUsuarioPorId(usuarioId);
        validarLimiteVmsPorUsuario(usuario);
        validarNomeUnicoParaUsuario(vmRequest.getNome(), usuarioId, null);
        validarRecursosVm(vmRequest);

        Vm vm = vmMapper.toEntity(vmRequest);
        vm.setStatus(VMStatus.STOPPED);
        vm.setUsuario(usuario);
        inicializarUsoRecursos(vm);

        Vm savedVm = vmRepository.save(vm);
        return vmMapper.toResponseDTO(savedVm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VmResponseDTO> listarVmsPorUsuario(Long usuarioId) {

        return vmRepository.findAll()
                .stream()
                .filter(vm -> vm.getUsuario() != null && vm.getUsuario().getId().equals(usuarioId))
                .map(vmMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VmResponseDTO buscarVmDoUsuario(Long id, Long usuarioId) {
        Vm vm = vmRepository.findById(id)
                .orElseThrow(() -> new VmNotFoundException(id));


        if (vm.getUsuario() == null || !vm.getUsuario().getId().equals(usuarioId)) {
            throw new VmNotFoundException("VM não encontrada ou não pertence ao usuário");
        }

        return vmMapper.toResponseDTO(vm);
    }

    @Override
    public VmResponseDTO atualizarVmDoUsuario(Long id, VmRequestDTO vmRequest, Long usuarioId) {
        Vm vm = vmRepository.findById(id)
                .orElseThrow(() -> new VmNotFoundException(id));


        if (vm.getUsuario() == null || !vm.getUsuario().getId().equals(usuarioId)) {
            throw new VmNotFoundException("VM não encontrada ou não pertence ao usuário");
        }

        validarNomeUnicoParaUsuario(vmRequest.getNome(), usuarioId, id);
        validarRecursosVm(vmRequest);

        vmMapper.updateEntity(vmRequest, vm);
        Vm updatedVm = vmRepository.save(vm);
        return vmMapper.toResponseDTO(updatedVm);
    }

    @Override
    public void deletarVmDoUsuario(Long id, Long usuarioId) {
        Vm vm = vmRepository.findById(id)
                .orElseThrow(() -> new VmNotFoundException(id));


        if (vm.getUsuario() == null || !vm.getUsuario().getId().equals(usuarioId)) {
            throw new VmNotFoundException("VM não encontrada ou não pertence ao usuário");
        }

        vmRepository.deleteById(id);
    }

    @Override
    public VmResponseDTO alterarStatusDaVmDoUsuario(Long id, VMStatus status, Long usuarioId) {
        Vm vm = vmRepository.findById(id)
                .orElseThrow(() -> new VmNotFoundException(id));


        if (vm.getUsuario() == null || !vm.getUsuario().getId().equals(usuarioId)) {
            throw new VmNotFoundException("VM não encontrada ou não pertence ao usuário");
        }

        if (status == null) {
            throw VmValidationException.statusInvalido("null");
        }

        vm.setStatus(status);
        atualizarUsoPorStatus(vm);
        Vm updatedVm = vmRepository.save(vm);
        return vmMapper.toResponseDTO(updatedVm);
    }


    private Usuario buscarUsuarioPorId(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioException("Usuário não encontrado com ID: " + usuarioId));
    }

    private void validarLimiteVmsPorUsuario(Usuario usuario) {

        long vmsDoUsuario = vmRepository.findAll()
                .stream()
                .filter(vm -> vm.getUsuario() != null && vm.getUsuario().getId().equals(usuario.getId()))
                .count();

        if (vmsDoUsuario >= LIMITE_MAXIMO_VMS) {
            throw new VmValidationException(
                    String.format("Limite máximo de %d VMs atingido para o usuário. VMs atuais: %d",
                            LIMITE_MAXIMO_VMS, vmsDoUsuario)
            );
        }
    }

    private void validarNomeUnicoParaUsuario(String nome, Long usuarioId, Long vmId) {

        boolean existe = vmRepository.findAll()
                .stream()
                .anyMatch(vm ->
                        vm.getUsuario() != null &&
                                vm.getUsuario().getId().equals(usuarioId) &&
                                vm.getNome().equals(nome) &&
                                (vmId == null || !vm.getId().equals(vmId))
                );

        if (existe) {
            throw VmValidationException.nomeJaExiste(nome);
        }
    }

    private void validarNomeUnico(String nome, Long idExcluir) {
        if (idExcluir == null) {
            if (vmRepository.existsByNome(nome)) {
                throw VmValidationException.nomeJaExiste(nome);
            }
        } else {
            if (vmRepository.existsByNomeAndIdNot(nome, idExcluir)) {
                throw VmValidationException.nomeJaExiste(nome);
            }
        }
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

    private void inicializarUsoRecursos(Vm vm) {
        vm.setCpuUso(0.0);
        vm.setMemoriaUso(0.0);
        vm.setDiscoUso(5.0);
    }

    private void atualizarUsoPorStatus(Vm vm) {
        switch (vm.getStatus()) {
            case STARTED:
                if (vm.getCpuUso() < 10.0) vm.setCpuUso(10.0);
                if (vm.getMemoriaUso() < 15.0) vm.setMemoriaUso(15.0);
                break;
            case STOPPED:
                vm.setCpuUso(0.0);
                vm.setMemoriaUso(0.0);
                break;
            case SUSPENDED:
                vm.setCpuUso(0.0);
                if (vm.getMemoriaUso() < 50.0) vm.setMemoriaUso(50.0);
                break;
        }
    }
}