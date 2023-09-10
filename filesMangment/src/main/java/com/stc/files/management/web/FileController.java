package com.stc.files.management.web;

import com.stc.files.management.core.services.ItemService;
import com.stc.files.management.domain.Item;
import com.stc.files.management.infra.mapper.ItemMapper;
import com.stc.files.management.web.dto.responses.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FileController {
    @Autowired
    private ItemService itemService;

    @QueryMapping
    public ItemDTO fileById(@Argument Long id, @Argument String userEmail) {
        return ItemMapper.INSTANCE.toDto(itemService.getFile(id,userEmail));
    }

    @GetMapping("/api/files/{file_id}")
    public ResponseEntity<Resource> downloadFileById(@PathVariable("file_id") Long fileId, @RequestParam("user_email") String userEmail) {
        Item item =  itemService.getFile(fileId,userEmail);
        Resource resource = new ByteArrayResource(item.getFile().getContent());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + item.getName() + "." + item.getFile().getExtension() + "\"")
                .body(resource);

    }
}
