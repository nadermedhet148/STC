package com.stc.files.management.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stc.files.management.FilesMangmentApplication;
import com.stc.files.management.core.services.ItemService;
import com.stc.files.management.domain.Item;
import com.stc.files.management.domain.ItemType;
import com.stc.files.management.web.dto.requests.FileDTO;
import com.stc.files.management.web.dto.requests.FolderDTO;
import com.stc.files.management.web.dto.requests.SpaceDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FilesMangmentApplication.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void testCreateSpace() throws Exception {
        SpaceDTO spaceDTO = new SpaceDTO("Test Space", "user@example.com");

        Item createdSpace = new Item();
        createdSpace.setId(1L);
        createdSpace.setName(spaceDTO.getName());
        createdSpace.setType(ItemType.SPACE);

        when(itemService.createSpace(any())).thenReturn(createdSpace);

        mockMvc.perform(post("/api/items/spaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(spaceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdSpace.getId()))
                .andExpect(jsonPath("$.name").value(createdSpace.getName()))
                .andExpect(jsonPath("$.type").value(createdSpace.getType().toString()));
    }

    @Test
    void testCreateFolder() throws Exception {
        Long spaceId = 1L;
        FolderDTO folderDTO = new FolderDTO("Test Folder", "user@example.com");

        Item createdFolder = new Item();
        createdFolder.setId(2L);
        createdFolder.setName(folderDTO.getName());
        createdFolder.setType(ItemType.FOLDER);

        when(itemService.createFolder(eq(spaceId), any())).thenReturn(createdFolder);

        mockMvc.perform(post("/api/items/spaces/{space_id}/folders", spaceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(folderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdFolder.getId()))
                .andExpect(jsonPath("$.name").value(createdFolder.getName()))
                .andExpect(jsonPath("$.type").value(createdFolder.getType().toString()));
    }

    @Test
    void testCreateFile() throws Exception {
        Long folderId = 1L;
        String fileName = "test";
        String userEmail = "user@example.com";
        byte[] content = "Hello, world!".getBytes();

        Item createdFile = new Item();
        createdFile.setId(3L);
        createdFile.setName(fileName);
        createdFile.setType(ItemType.FILE);

        when(itemService.createFile(eq(folderId), any(FileDTO.class))).thenReturn(createdFile);

        MockMultipartFile file = new MockMultipartFile("file", fileName, "text/plain", content);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/items/folders/{folder_id}/files", folderId)
                        .file(file)
                        .param("name", fileName)
                        .param("userEmail", userEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdFile.getId()))
                .andExpect(jsonPath("$.name").value(createdFile.getName()))
                .andExpect(jsonPath("$.type").value(createdFile.getType().toString()));
    }
}
