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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    PermissionGroupRepository permissionGroupRepository;

    @Autowired
    PermissionRepository permissionRepository;


    public Item createSpace(SpaceDTO spaceDTO) {
        PermissionGroup permissionGroup = permissionGroupRepository.findByGroupName(spaceDTO.getPermissionGroupName())
                .orElseThrow(() -> new NotFoundEntityException("PermissionGroup.Not_FOUND"));

        return this.itemRepository.save(Item.builder().permissionGroup(permissionGroup).type(ItemType.SPACE).name(spaceDTO.getName()).build());
    }

    public Item createFolder(Long spaceId , FolderDTO folderDTO) {
        Item space = this.itemRepository.findByIdAndType(spaceId , ItemType.SPACE).orElseThrow(() -> new NotFoundEntityException("SPACE.Not_FOUND"));
        checkIfUserHasAccessToItem(spaceId, folderDTO.getUserEmail() , List.of(PermissionLevel.EDIT.name()) , ItemType.SPACE.name());

        return this.itemRepository.save(Item.builder().parent(space).type(ItemType.FOLDER).name(folderDTO.getName()).build());
    }



    public Item createFile(Long folderId , FileDTO fileDTO) {
        Item folder = this.itemRepository.findByIdAndType(folderId , ItemType.FOLDER).orElseThrow(() -> new NotFoundEntityException("FOLDER.Not_FOUND"));

        checkIfUserHasAccessToItem(folderId, fileDTO.getUserEmail() , List.of(PermissionLevel.EDIT.name()) , ItemType.FOLDER.name());

        return this.itemRepository.save(Item.builder()
                .parent(folder)
                .type(ItemType.FILE)
                .name(fileDTO.getName())
                .file(File.builder().content(fileDTO.getContent()).extension(fileDTO.getExtension().split("/")[1]).build())
                .build());
    }

    public Item getFile(Long fileId , String userEmail) {
        checkIfUserHasAccessToItem(fileId, userEmail , List.of(PermissionLevel.EDIT.name() , PermissionLevel.VIEW.name()) , ItemType.FILE.name());

        return this.itemRepository.findByIdAndType(fileId , ItemType.FILE).orElseThrow(() -> new NotFoundEntityException("FILE.Not_FOUND"));
    }

    private void checkIfUserHasAccessToItem(Long itemId, String userEmail , List<String> permissionLevels , String itemType) {
        permissionRepository.findByUserAndLevelAndFile(userEmail , permissionLevels , itemId, itemType)
                .orElseThrow(() -> new NotAuthorizedException("USER.DON'T_HAS_ACCESS"));
    }

}
