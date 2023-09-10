package com.stc.files.management.infra.repository;

import com.stc.files.management.domain.Item;
import com.stc.files.management.domain.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByIdAndType(Long id, ItemType type);


}
