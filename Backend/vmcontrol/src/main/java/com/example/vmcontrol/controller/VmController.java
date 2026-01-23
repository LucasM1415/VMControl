package com.example.vmcontrol.controller;

import com.example.vmcontrol.model.dto.VirtualMachine.VmRequestDTO;
import com.example.vmcontrol.model.dto.VirtualMachine.VmResponseDTO;
import com.example.vmcontrol.model.entity.Usuario;
import com.example.vmcontrol.model.enums.VMStatus;
import com.example.vmcontrol.service.VmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/vms")
@RequiredArgsConstructor
@Tag(name = "Máquinas Virtuais", description = "API para gerenciamento de máquinas virtuais")
public class VmController {

    private final VmService vmService;


    @PostMapping("/admin")
    @Operation(summary = "Criar uma nova VM (admin)")
    public ResponseEntity<VmResponseDTO> criarVm(@Valid @RequestBody VmRequestDTO vmRequest) {
        VmResponseDTO vmResponse = vmService.criarVm(vmRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(vmResponse);
    }

    @GetMapping("/todas")
    @Operation(summary = "Listar todas as VMs")
    public ResponseEntity<List<VmResponseDTO>> listarTodasVms() {
        List<VmResponseDTO> vms = vmService.listarTodasVms();
        return ResponseEntity.ok(vms);
    }

    @GetMapping("/admin/{id}")
    @Operation(summary = "Buscar VM por ID (admin)")
    public ResponseEntity<VmResponseDTO> buscarPorId(@PathVariable Long id) {
        VmResponseDTO vm = vmService.buscarPorId(id);
        return ResponseEntity.ok(vm);
    }

    @PostMapping("/minhas")
    @Operation(summary = "Criar uma nova VM para o usuário atual")
    public ResponseEntity<VmResponseDTO> criarVmParaUsuario(@Valid @RequestBody VmRequestDTO vmRequest) {
        Usuario usuario = getUsuarioFromAuthentication();
        VmResponseDTO vmResponse = vmService.criarVmParaUsuario(vmRequest, usuario.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(vmResponse);
    }

    @GetMapping("/minhas")
    @Operation(summary = "Listar VMs do usuário atual")
    public ResponseEntity<List<VmResponseDTO>> listarMinhasVms() {
        Usuario usuario = getUsuarioFromAuthentication();
        List<VmResponseDTO> vms = vmService.listarVmsPorUsuario(usuario.getId());
        return ResponseEntity.ok(vms);
    }

    @GetMapping("/minhas/{id}")
    @Operation(summary = "Buscar VM do usuário atual por ID")
    public ResponseEntity<VmResponseDTO> buscarMinhaVmPorId(@PathVariable Long id) {
        Usuario usuario = getUsuarioFromAuthentication();
        VmResponseDTO vm = vmService.buscarVmDoUsuario(id, usuario.getId());
        return ResponseEntity.ok(vm);
    }

    @PutMapping("/minhas/{id}")
    @Operation(summary = "Atualizar VM do usuário atual")
    public ResponseEntity<VmResponseDTO> atualizarMinhaVm(
            @PathVariable Long id,
            @Valid @RequestBody VmRequestDTO vmRequest) {
        Usuario usuario = getUsuarioFromAuthentication();
        VmResponseDTO vmAtualizada = vmService.atualizarVmDoUsuario(id, vmRequest, usuario.getId());
        return ResponseEntity.ok(vmAtualizada);
    }

    @DeleteMapping("/minhas/{id}")
    @Operation(summary = "Deletar VM do usuário atual")
    public ResponseEntity<Void> deletarMinhaVm(@PathVariable Long id) {
        Usuario usuario = getUsuarioFromAuthentication();
        vmService.deletarVmDoUsuario(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/minhas/{id}/status")
    @Operation(summary = "Alterar status da VM do usuário atual")
    public ResponseEntity<VmResponseDTO> alterarStatusMinhaVm(
            @PathVariable Long id,
            @RequestParam VMStatus status) {
        Usuario usuario = getUsuarioFromAuthentication();
        VmResponseDTO vmAtualizada = vmService.alterarStatusDaVmDoUsuario(id, status, usuario.getId());
        return ResponseEntity.ok(vmAtualizada);
    }

    private Usuario getUsuarioFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Usuario) {
            return (Usuario) principal;
        }

        throw new SecurityException("Tipo de autenticação não suportado: " + principal.getClass());
    }
}