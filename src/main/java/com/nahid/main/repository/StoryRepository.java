package com.nahid.main.repository;

import com.nahid.main.model.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StoryRepository extends JpaRepository<Story, UUID> {

//TODO - ContainingIgnoreCase
    List<Story> findStoriesByStoryForContainingIgnoreCase(String storyFor);

    Page<Story> findStoriesByStoryForContainingIgnoreCase(String storyFor, Pageable pageable);

    List<Story> findStoriesByStoryBy(String storyBy);

    Story findStoryByStoryId(UUID storyId);
}
