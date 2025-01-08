package com.dionext.repositories;

import com.dionext.db.entity.JoomCategories;
import com.dionext.db.entity.JoomContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoomCategoriesRepository extends JpaRepository<JoomCategories, String> {
}