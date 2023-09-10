package com.stc.files.management.infra.repository;

import com.stc.files.management.domain.ItemType;
import com.stc.files.management.domain.Permission;
import com.stc.files.management.domain.PermissionLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query(nativeQuery = true, value = "WITH RECURSIVE space_hierarchy AS (\n"
            + "  SELECT id, parent_item_id, permission_group_id\n"
            + "  FROM public.items\n"
            + "  WHERE type = :item_type AND id = :item_id\n"
            + "  UNION ALL\n"
            + "  SELECT i.id, i.parent_item_id, i.permission_group_id\n"
            + "  FROM public.items AS i\n"
            + "  JOIN space_hierarchy AS sh ON i.id = sh.parent_item_id\n"
            + ")\n"
            + "SELECT p.id, p.permission_group_id, p.permission_level, p.user_email\n"
            + "FROM space_hierarchy s\n"
            + "JOIN public.permissions p ON p.permission_group_id = s.permission_group_id\n"
            + "WHERE parent_item_id IS NULL AND p.user_email = :user_email AND p.permission_level in (:permission_levels)")
    Optional<Permission> findByUserAndLevelAndFile(@Param("user_email") String userEmail,
                                                   @Param("permission_levels") List<String> permissionLevels,
                                                   @Param("item_id") Long itemId,
                                                   @Param("item_type") String itemType
    );

}
