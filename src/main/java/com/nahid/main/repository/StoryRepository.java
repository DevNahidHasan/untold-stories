package com.nahid.main.repository;

import com.nahid.main.model.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StoryRepository extends JpaRepository<Story, UUID> {

    List<Story> findStoriesByStoryForContainingIgnoreCase(String storyFor);

    Page<Story> findStoriesByStoryForContainingIgnoreCase(String storyFor, Pageable pageable);

    List<Story> findStoriesByStoryBy(String storyBy);

    Page<Story> findStoriesByStoryBy(String storyBy, Pageable pageable);

    Story findStoryByStoryId(UUID storyId);
    
    // Methods with sorting by createdAt DESC (latest to oldest)
    Page<Story> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    Page<Story> findStoriesByStoryForContainingIgnoreCaseOrderByCreatedAtDesc(String storyFor, Pageable pageable);
    
    Page<Story> findStoriesByStoryByOrderByCreatedAtDesc(String storyBy, Pageable pageable);
}
