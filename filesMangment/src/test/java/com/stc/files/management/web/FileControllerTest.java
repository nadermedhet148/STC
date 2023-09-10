package com.stc.files.management.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stc.files.management.FilesMangmentApplication;
import com.stc.files.management.core.services.ItemService;
import com.stc.files.management.domain.File;
import com.stc.files.management.domain.Item;
import com.stc.files.management.domain.ItemType;
import com.stc.files.management.web.dto.responses.ItemDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.graphql.test.tester.HttpGraphQlTester;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FilesMangmentApplication.class)
@AutoConfigureMockMvc
@AutoConfigureGraphQlTester
class FileControllerTest {

    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private GraphQlTester tester;


    @Test
    void createProduct() {
        Long itemId = 1L;
        String userEmail = "test@example.com";
        Item item = new Item();
        byte[] content = "test content".getBytes();
        item.setFile(File.builder().content(content).extension("png").build());
        item.setName("test");
        item.setId(itemId);
        item.setType(ItemType.FILE);
        when(itemService.getFile(eq(itemId), eq(userEmail))).thenReturn(item);


        ItemDTO itemDTO = this.tester
                .document("""
                        query {
                            fileById(id : 1,userEmail: "test@example.com") {
                                id,
                                name,
                                type,
                                file {
                                    extension
                                },
                            }
                        }
                        """)
                .execute()
                .errors()
                .verify()
                .path("data.fileById")
                .entity(ItemDTO.class)
                .get();
        Assertions.assertNotNull(itemDTO);
        Assertions.assertEquals(itemId , itemDTO.getId());
        Assertions.assertEquals(ItemType.FILE , itemDTO.getType());

    }


    @Test
    void testDownloadFileById() throws Exception {
        Long fileId = 1L;
        String userEmail = "test@example.com";
        Item item = new Item();
        byte[] content = "test content".getBytes();
        item.setFile(File.builder().content(content).extension("png").build());
        item.setName("test");
        when(itemService.getFile(eq(fileId), eq(userEmail))).thenReturn(item);

        mockMvc.perform(get("/api/files/{file_id}", fileId)
                        .param("user_email", userEmail))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test.png\""))
                .andExpect(content().bytes(content));
    }

}
