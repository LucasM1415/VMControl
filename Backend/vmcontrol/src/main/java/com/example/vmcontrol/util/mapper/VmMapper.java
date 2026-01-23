package com.example.vmcontrol.util.mapper;

import com.example.vmcontrol.model.dto.VirtualMachine.VmRequestDTO;
import com.example.vmcontrol.model.dto.VirtualMachine.VmResponseDTO;
import com.example.vmcontrol.model.entity.Vm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VmMapper {
    VmMapper INSTANCE = Mappers.getMapper(VmMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    Vm toEntity(VmRequestDTO dto);

    VmResponseDTO toResponseDTO(Vm entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    void updateEntity(VmRequestDTO dto, @MappingTarget Vm entity);
}
