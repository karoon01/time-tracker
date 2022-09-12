package com.yosypchuk.tracker.mapper;

import com.yosypchuk.tracker.model.DTO.RequestDTO;
import com.yosypchuk.tracker.model.entity.Request;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    Request mapRequest(RequestDTO requestDTO);

    RequestDTO mapRequestDTO(Request request);
}
