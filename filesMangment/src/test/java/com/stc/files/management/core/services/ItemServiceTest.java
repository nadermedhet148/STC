package com.stc.files.management.core.services;

import com.stc.files.management.core.errors.NotAuthorizedException;
import com.stc.files.management.core.errors.NotFoundEntityException;
import com.stc.files.management.domain.*;
import com.stc.files.management.infra.repository.ItemRepository;
import com.stc.files.management.infra.repository.PermissionGroupRepository;
import com.stc.files.management.infra.repository.PermissionRepository;
import com.stc.files.management.web.dto.requests.FileDTO;
import com.stc.files.management.web.dto.requests.FolderDTO;
import com.stc.files.management.web.dto.requests.SpaceDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemServiceTest {

    @Mock
    private PermissionGroupRepository permissionGroupRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testCreateSpace() {
        SpaceDTO spaceDTO = new SpaceDTO();
        spaceDTO.setName("Test Space");
        spaceDTO.setPermissionGroupName("Test Group");

        PermissionGroup permissionGroup = new PermissionGroup();
        permissionGroup.setGroupName("Test Group");

        when(permissionGroupRepository.findByGroupName("Test Group")).thenReturn(Optional.of(permissionGroup));
        when(itemRepository.save(any(Item.class))).then((a) -> a.getArguments()[0]);

        Item result = itemService.createSpace(spaceDTO);

        assertEquals(ItemType.SPACE, result.getType());
        assertEquals("Test Space", result.getName());
    }

    @Test
    public void testCreateSpaceWithInvalidPermissionGroup() {
        SpaceDTO spaceDTO = new SpaceDTO();
        spaceDTO.setName("Test Space");
        spaceDTO.setPermissionGroupName("Invalid Group");

        when(permissionGroupRepository.findByGroupName("Invalid Group")).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> itemService.createSpace(spaceDTO));
    }


    @Test
    public void testCreateFolder() {
        Long spaceId = 1L;
        String userEmail = "test@example.com";

        FolderDTO folderDTO = new FolderDTO();
        folderDTO.setName("Test Folder");
        folderDTO.setUserEmail(userEmail);

        Item space = new Item();
        space.setType(ItemType.SPACE);
        space.setPermissionGroup(new PermissionGroup());



        when(permissionRepository.findByUserAndLevelAndFile(eq(userEmail), anyList(), eq(spaceId), eq(ItemType.SPACE.name()))).thenReturn(Optional.of(new Permission()));
        when(itemRepository.findByIdAndType(eq(spaceId), eq(ItemType.SPACE))).thenReturn(Optional.of(space));
        when(itemRepository.save(any(Item.class))).then((a) -> a.getArguments()[0]);


        Item result = itemService.createFolder(spaceId, folderDTO);

        assertEquals(ItemType.FOLDER, result.getType());
        assertEquals("Test Folder", result.getName());
    }

    @Test
    public void testCreateFolderWithInvalidSpaceId() {
        Long spaceId = 1L;
        FolderDTO folderDTO = new FolderDTO();
        folderDTO.setUserEmail("test@example.com");

        when(itemRepository.findByIdAndType(eq(spaceId), eq(ItemType.SPACE))).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> itemService.createFolder(spaceId, folderDTO));
    }

    @Test
    public void testCreateFolderWithUnauthorizedUser() {
        Long spaceId = 1L;
        String userEmail = "test@example.com";

        FolderDTO folderDTO = new FolderDTO();
        folderDTO.setUserEmail(userEmail);

        Item space = new Item();
        space.setType(ItemType.SPACE);
        space.setPermissionGroup(new PermissionGroup());

        Permission permission = new Permission();
        permission.setPermissionLevel(PermissionLevel.VIEW);
        permission.setUserEmail(userEmail);

        space.getPermissionGroup().setPermissions(List.of(permission));

        when(itemRepository.findByIdAndType(eq(spaceId), eq(ItemType.SPACE))).thenReturn(Optional.of(space));

        assertThrows(NotAuthorizedException.class, () -> itemService.createFolder(spaceId, folderDTO));
    }

    @Test
    void testCreateFileFolderNotFound() {
        Long folderId = 1L;
        FileDTO fileDTO = new FileDTO("test.txt", "user@example.com", "text/plain", new byte[]{});

        when(itemRepository.findByIdAndType(eq(folderId), eq(ItemType.FOLDER))).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> itemService.createFile(folderId, fileDTO));
    }

    @Test
    void testCreateFileUserDoesntHaveEditPermission() {
        Long folderId = 1L;
        FileDTO fileDTO = new FileDTO("test.txt", "user@example.com", "text/plain", new byte[]{});

        Item folder = new Item();
        folder.setId(folderId);
        folder.setType(ItemType.FOLDER);

        when(itemRepository.findByIdAndType(eq(folderId), eq(ItemType.FOLDER))).thenReturn(Optional.of(folder));
        when(permissionRepository.findByUserAndLevelAndFile(eq(fileDTO.getUserEmail()), anyList(), eq(folderId), eq(ItemType.FOLDER.name()))).thenReturn(Optional.empty());

        assertThrows(NotAuthorizedException.class, () -> itemService.createFile(folderId, fileDTO));
    }

    @Test
    void testCreateSuccessfully() {
        Long folderId = 1L;
        FileDTO fileDTO = new FileDTO("Test File", "user@example.com", "text/plain", new byte[]{});

        Item folder = new Item();
        folder.setId(folderId);
        folder.setType(ItemType.FOLDER);

        Permission permission = new Permission();
        permission.setUserEmail(fileDTO.getUserEmail());
        permission.setPermissionLevel(PermissionLevel.EDIT);

        when(itemRepository.findByIdAndType(eq(folderId), eq(ItemType.FOLDER))).thenReturn(Optional.of(folder));
        when(permissionRepository.findByUserAndLevelAndFile(eq(fileDTO.getUserEmail()), anyList(), eq(folderId), eq(ItemType.FOLDER.name()))).thenReturn(Optional.of(permission));
        when(itemRepository.save(any(Item.class))).then((a) -> a.getArguments()[0]);

        Item result = itemService.createFile(folderId, fileDTO);

        assertEquals(ItemType.FILE, result.getType());
        assertEquals("Test File", result.getName());
    }


    @Test
    void testGetFileUserDoesntHaveAccess() {
        Long fileId = 1L;
        String userEmail = "user@example.com";

        when(permissionRepository.findByUserAndLevelAndFile(eq(userEmail), anyList(), eq(fileId), eq(ItemType.FILE.name()))).thenReturn(Optional.empty());

        assertThrows(NotAuthorizedException.class, () -> itemService.getFile(fileId, userEmail));
    }

    @Test
    void testGetFileNotFound() {
        Long fileId = 1L;
        String userEmail = "user@example.com";

        Permission permission = new Permission();
        permission.setUserEmail(userEmail);
        permission.setPermissionLevel(PermissionLevel.VIEW);

        when(permissionRepository.findByUserAndLevelAndFile(eq(userEmail), anyList(), eq(fileId), eq(ItemType.FILE.name()))).thenReturn(Optional.of(permission));
        when(itemRepository.findByIdAndType(eq(fileId), eq(ItemType.FILE))).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> itemService.getFile(fileId, userEmail));
    }

    @Test
    void testGetFileSuccessfully() {
        Long fileId = 1L;
        String userEmail = "user@example.com";

        Permission permission = new Permission();
        permission.setUserEmail(userEmail);
        permission.setPermissionLevel(PermissionLevel.VIEW);

        Item file = new Item();
        file.setName("Test File");
        file.setId(fileId);

        file.setType(ItemType.FILE);

        when(permissionRepository.findByUserAndLevelAndFile(eq(userEmail), anyList(), eq(fileId), eq(ItemType.FILE.name()))).thenReturn(Optional.of(permission));
        when(itemRepository.findByIdAndType(eq(fileId), eq(ItemType.FILE))).thenReturn(Optional.of(file));

        Item result = itemService.getFile(fileId, userEmail);

        assertEquals(ItemType.FILE, result.getType());
        assertEquals("Test File", result.getName());
    }

}