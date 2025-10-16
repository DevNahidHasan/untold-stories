package com.nahid.main.service;

import com.nahid.main.model.Story;
import com.nahid.main.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class StoryService {

    private final StoryRepository storyRepository;

    public Page<Story> getStories(Pageable pageable){
        Page<Story> storyList = storyRepository.findAll(pageable);

        for (Story story : storyList){
            String shortDescription = story.getDescription();
            shortDescription = truncateDescription(shortDescription);
            story.setDescription(shortDescription);
        }

        return storyList;
    }

    public String truncateDescription(String description){
        if(description != null && description.length() > 200){
            return description.substring(0,200) + "...";
        }else {
            return description;
        }

    }

    public List<Story> searchStoryByName(String storyFor){
        List<Story> storyList = storyRepository.findStoriesByStoryForContaining(storyFor);

        for (Story story : storyList){
            String shortDescription = story.getDescription();
            shortDescription = truncateDescription(shortDescription);
            story.setDescription(shortDescription);
        }

        return storyList;
    }

    public Story getStoryById(UUID storyId){
        return storyRepository.findStoryByStoryId(storyId);
    }

    public void deleteStoryById(UUID storyId){
        storyRepository.deleteById(storyId);
    }

}
