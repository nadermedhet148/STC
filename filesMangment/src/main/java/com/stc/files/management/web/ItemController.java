package com.stc.files.management.web;

import com.stc.files.management.core.services.ItemService;
import com.stc.files.management.domain.Item;
import com.stc.files.management.infra.mapper.ItemMapper;
import com.stc.files.management.web.dto.requests.FileDTO;
import com.stc.files.management.web.dto.requests.FolderDTO;
import com.stc.files.management.web.dto.requests.SpaceDTO;
import com.stc.files.management.web.dto.responses.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/api/items/spaces")
    public ResponseEntity<ItemDTO> createSpace(@RequestBody SpaceDTO spaceDTO) {
        Item createdSpace = itemService.createSpace(spaceDTO);
        return ResponseEntity.ok().body(ItemMapper.INSTANCE.toDto(createdSpace));
    }

    @PostMapping("/api/items/spaces/{space_id}/folders")
    public ResponseEntity<ItemDTO> createFolder(@PathVariable("space_id") Long spaceId ,  @RequestBody FolderDTO folderDTO) {
        Item createdFolder = itemService.createFolder(spaceId , folderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ItemMapper.INSTANCE.toDto(createdFolder));
    }
    @PostMapping("/api/items/folders/{folder_id}/files")
    public ResponseEntity<ItemDTO> createFile(
            @PathVariable("folder_id") Long folderId, @RequestPart(value = "file", required = true) MultipartFile file,
                                                   @RequestParam(value = "name")  String fileName, @RequestParam(value = "userEmail")  String userEmail) throws IOException {
        Item createdFile = itemService.createFile(folderId , FileDTO.builder()
                .content(file.getBytes())
                .userEmail(userEmail)
                .extension(file.getContentType())
                .name(fileName).build());
        return ResponseEntity.ok().body(ItemMapper.INSTANCE.toDto(createdFile));
    }
}
