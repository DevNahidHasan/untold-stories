package com.nahid.main.service;

import com.nahid.main.model.Story;
import com.nahid.main.repository.StoryRepository;
import com.nahid.main.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class StoryService {

    private final StoryRepository storyRepository;
//    private final TextEncryptor textEncryptor;
    private final HashUtil hashUtil;
    private final PasswordEncoder passwordEncoder;

    public Page<Story> getStories(Pageable pageable){
        Page<Story> storyList = storyRepository.findAll(pageable);

        for (Story story : storyList){
            String shortDescription = story.getDescription();
            shortDescription = truncateDescription(shortDescription);
            story.setDescription(shortDescription);
        }

        return storyList;
    }

    public Page<Story> searchStoryByNamePageable(String storyFor, Pageable pageable){
       Page<Story> storyList = storyRepository.findStoriesByStoryForContainingIgnoreCase(storyFor,pageable);

        for (Story story : storyList){
            String shortDescription = story.getDescription();
            shortDescription = truncateDescription(shortDescription);
            story.setDescription(shortDescription);
        }

        return storyList;
    }

//    public List<Story> searchStoryByName(String storyFor){
//        List<Story> storyList = storyRepository.findStoriesByStoryForContainingIgnoreCase(storyFor);
//
//        for (Story story : storyList){
//            String shortDescription = story.getDescription();
//            shortDescription = truncateDescription(shortDescription);
//            story.setDescription(shortDescription);
//        }
//
//        return storyList;
//    }

    public Page<Story> searchStoryByUserPageable(String username, Pageable pageable) {
        username = hashUtil.hash(username);
        System.out.println(username);
        Page<Story> storyList = storyRepository.findStoriesByStoryBy(username,pageable);

        for (Story story : storyList){
            String shortDescription = story.getDescription();
            shortDescription = truncateDescription(shortDescription);
            story.setDescription(shortDescription);
        }

        return storyList;
    }


//    public List<Story> searchStoryByUser(String storyBy){
//        List<Story> storyList = storyRepository.findStoriesByStoryBy(storyBy);
//
//        for (Story story : storyList){
//            String shortDescription = story.getDescription();
//            shortDescription = truncateDescription(shortDescription);
//            story.setDescription(shortDescription);
//        }
//
//        return storyList;
//    }

    public Story getStoryById(UUID storyId){
        return storyRepository.findStoryByStoryId(storyId);
    }

    public void deleteStoryById(UUID storyId){
        storyRepository.deleteById(storyId);
    }

    public void saveStory(Story story) {
        story.setStoryBy(hashUtil.hash(story.getStoryBy()));
        storyRepository.save(story);
    }

    public String truncateDescription(String description){
        if(description != null && description.length() > 200){
            return description.substring(0,200) + "...";
        }else {
            return description;
        }

    }


}
