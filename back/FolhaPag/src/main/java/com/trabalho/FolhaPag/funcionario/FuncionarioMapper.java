package com.trabalho.FolhaPag.funcionario;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class FuncionarioMapper {

    public static FuncionarioMapper INSTANCE = Mappers.getMapper(FuncionarioMapper.class);

    public abstract FuncionarioResponseDTO toFuncionarioResponseDTO(Funcionario funcionario);
}
