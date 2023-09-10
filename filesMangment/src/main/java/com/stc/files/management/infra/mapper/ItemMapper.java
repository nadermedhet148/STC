package com.stc.files.management.infra.mapper;

import com.stc.files.management.domain.Item;
import com.stc.files.management.web.dto.responses.ItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper( ItemMapper.class );

    ItemDTO toDto(Item item);
}