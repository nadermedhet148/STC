package com.stc.files.management.web.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SpaceDTO {
    private String name;
    private String permissionGroupName;
}
