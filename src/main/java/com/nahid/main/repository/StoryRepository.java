package com.nahid.main.repository;

import com.nahid.main.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StoryRepository extends JpaRepository<Story, UUID> {
    List<Story> findStoriesByStoryForContaining(String storyFor);
}
