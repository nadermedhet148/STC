package com.stc.files.management.web.dto.requests;

import lombok.*;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    private String name;
    private String userEmail;
    private String extension;
    private byte[] content;
}
