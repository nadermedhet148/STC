package com.stc.files.management.web.dto.responses;

import com.stc.files.management.domain.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@Getter
@Setter
public class ItemDTO {
    private Long id;

    private ItemType type;
    private String name;
    private FileDTO file;
    private ItemDTO parent;


}
