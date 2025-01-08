package com.dionext.repositories;

import com.dionext.db.entity.JoomContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoomContentRepository extends JpaRepository<JoomContent, String> {
}