package com.stc.files.management.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "files")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    @Lob
    private byte[] content;

    @Column(name = "extension")
    private String extension;


    @OneToOne(mappedBy = "file")
    private Item item;


}
