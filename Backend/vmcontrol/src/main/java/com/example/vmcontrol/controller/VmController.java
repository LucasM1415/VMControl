package com.example.vmcontrol.controller;

import com.example.vmcontrol.model.dto.VmRequestDTO;
import com.example.vmcontrol.model.dto.VmResponseDTO;
import com.example.vmcontrol.model.enums.VMStatus;
import com.example.vmcontrol.service.VmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vms")
@RequiredArgsConstructor
@Tag(name = "Máquinas Virtuais", description = "API para gerenciamento de máquinas virtuais")
public class VmController {

    private final VmService vmService;

    @PostMapping
    @Operation(summary = "Criar uma nova VM")
    public ResponseEntity<VmResponseDTO> criarVm(@Valid @RequestBody VmRequestDTO vmRequest) {
        VmResponseDTO vmResponse = vmService.criarVm(vmRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(vmResponse);
    }

    @GetMapping
    @Operation(summary = "Listar todas as VMs")
    public ResponseEntity<List<VmResponseDTO>> listarTodasVms() {
        List<VmResponseDTO> vms = vmService.listarTodasVms();
        return ResponseEntity.ok(vms);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar VM por ID")
    public ResponseEntity<VmResponseDTO> buscarPorId(@PathVariable Long id) {
        VmResponseDTO vm = vmService.buscarPorId(id);
        return ResponseEntity.ok(vm);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar VM")
    public ResponseEntity<VmResponseDTO> atualizarVm(
            @PathVariable Long id,
            @Valid @RequestBody VmRequestDTO vmRequest) {
        VmResponseDTO vmAtualizada = vmService.atualizarVm(id, vmRequest);
        return ResponseEntity.ok(vmAtualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar VM")
    public ResponseEntity<Void> deletarVm(@PathVariable Long id) {
        vmService.deletarVm(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status da VM")
    public ResponseEntity<VmResponseDTO> alterarStatus(
            @PathVariable Long id,
            @RequestParam VMStatus status) {
        VmResponseDTO vmAtualizada = vmService.alterarStatus(id, status);
        return ResponseEntity.ok(vmAtualizada);
    }
}