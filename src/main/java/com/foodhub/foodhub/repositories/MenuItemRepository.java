package com.foodhub.foodhub.repositories;

import com.foodhub.foodhub.entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
