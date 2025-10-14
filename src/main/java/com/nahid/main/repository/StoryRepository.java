package com.nahid.main.repository;

import com.nahid.main.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoryRepository extends JpaRepository<Story, UUID> {

}
