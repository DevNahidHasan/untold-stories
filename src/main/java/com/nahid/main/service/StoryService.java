package com.nahid.main.service;

import com.nahid.main.model.Story;
import com.nahid.main.repository.StoryRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class StoryService {

    private final StoryRepository storyRepository;

    public List<Story> getStories(){
        return storyRepository.findAll();
    }
}
