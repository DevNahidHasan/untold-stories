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
    private final HashUtil hashUtil;

    /**
     * Retrieves a paginated list of all stories ordered by creation date (newest first).
     * 
     * <p>This method fetches all stories from the database with pagination support
     * and automatically truncates story descriptions to 200 characters for display
     * in list views. The truncation ensures consistent UI presentation and
     * improves page load performance.</p>
     *
     * @param pageable pagination information including page number, size, and sorting
     * @return a {@link Page} of {@link Story} objects with truncated descriptions,
     *         ordered by creation date in descending order
     * @see #truncateDescription(String)
     */
    public Page<Story> getStories(Pageable pageable){
        Page<Story> storyList = storyRepository.findAllByOrderByCreatedAtDesc(pageable);

        for (Story story : storyList){
            String shortDescription = story.getDescription();
            shortDescription = truncateDescription(shortDescription);
            story.setDescription(shortDescription);
        }

        return storyList;
    }

    /**
     * Searches for stories by title with case-insensitive matching and pagination.
     * 
     * <p>This method performs a substring search on story titles, ignoring case
     * sensitivity. Results are ordered by creation date (newest first) and
     * descriptions are automatically truncated to 200 characters for display.</p>
     * 
     * <p>Example: Searching for "love" will match stories with titles like
     * "Love Story", "Unloved", "LOVE AND LOSS", etc.</p>
     *
     * @param storyFor the search term to match against story titles (case-insensitive)
     * @param pageable pagination information including page number, size, and sorting
     * @return a {@link Page} of {@link Story} objects matching the search criteria,
     *         with truncated descriptions, ordered by creation date in descending order
     * @see #truncateDescription(String)
     */
    public Page<Story> searchStoryByNamePageable(String storyFor, Pageable pageable){
       Page<Story> storyList = storyRepository.findStoriesByStoryForContainingIgnoreCaseOrderByCreatedAtDesc(storyFor,pageable);

        for (Story story : storyList){
            String shortDescription = story.getDescription();
            shortDescription = truncateDescription(shortDescription);
            story.setDescription(shortDescription);
        }

        return storyList;
    }

    /**
     * Retrieves all stories posted by a specific user with pagination.
     * 
     * <p>This method searches for stories using the hashed username to maintain
     * user anonymity at the database level. The username is hashed before querying,
     * and only stories matching the hashed identifier are returned. This allows
     * users to manage their own stories while keeping their identity anonymous
     * to the system and other users.</p>
     * 
     * <p>The hash-based approach ensures:</p>
     * <ul>
     *   <li>Complete anonymity at the database level (no direct user-story relationship)</li>
     *   <li>Story ownership verification without exposing user identity</li>
     *   <li>Ability for users to edit/delete only their own stories</li>
     * </ul>
     *
     * @param username the username of the story author (will be hashed internally)
     * @param pageable pagination information including page number, size, and sorting
     * @return a {@link Page} of {@link Story} objects authored by the specified user,
     *         with truncated descriptions, ordered by creation date in descending order
     * @see HashUtil#hash(String)
     * @see #truncateDescription(String)
     */
    public Page<Story> searchStoryByUserPageable(String username, Pageable pageable) {
        username = hashUtil.hash(username);
//        System.out.println(username);
        Page<Story> storyList = storyRepository.findStoriesByStoryByOrderByCreatedAtDesc(username,pageable);

        for (Story story : storyList){
            String shortDescription = story.getDescription();
            shortDescription = truncateDescription(shortDescription);
            story.setDescription(shortDescription);
        }

        return storyList;
    }

    /**
     * Retrieves a single story by its unique identifier.
     * 
     * <p>This method fetches the complete story details including the full
     * (non-truncated) description. Typically used when displaying the full
     * story view page or when editing a story.</p>
     *
     * @param storyId the unique UUID identifier of the story
     * @return the {@link Story} object with the specified ID, or {@code null}
     *         if no story exists with that ID
     */
    public Story getStoryById(UUID storyId){
        return storyRepository.findStoryByStoryId(storyId);
    }

    /**
     * Deletes a story from the database by its unique identifier.
     * 
     * <p>This method permanently removes the story from the database.
     * Authorization checks should be performed at the controller level
     * before calling this method to ensure only the story author can
     * delete their own stories.</p>
     * 
     * <p><strong>Warning:</strong> This operation is irreversible. Once deleted,
     * the story cannot be recovered.</p>
     *
     * @param storyId the unique UUID identifier of the story to delete
     */
    public void deleteStoryById(UUID storyId){
        storyRepository.deleteById(storyId);
    }

    /**
     * Saves a new story or updates an existing story in the database.
     * 
     * <p>This method handles both story creation and updates. Before saving,
     * it hashes the author's username to maintain anonymity at the database level.
     * The hashing ensures that stories are not directly linked to user accounts
     * in the database schema, while still allowing ownership verification.</p>
     * 
     * <p>For new stories, this creates a database record with:</p>
     * <ul>
     *   <li>Auto-generated UUID as the primary key</li>
     *   <li>Hashed username as the author identifier</li>
     *   <li>Current timestamp as the creation date</li>
     * </ul>
     * 
     * <p>For existing stories (updates), this preserves the original story ID
     * and creation date while updating the content fields.</p>
     *
     * @param story the {@link Story} object to save or update. The {@code storyBy}
     *              field should contain the plain username, which will be hashed
     *              automatically before persisting
     * @see HashUtil#hash(String)
     */
    public void saveStory(Story story) {
        story.setStoryBy(hashUtil.hash(story.getStoryBy()));
        storyRepository.save(story);
    }

    /**
     * Truncates a story description to 200 characters for display in list views.
     * 
     * <p>This utility method ensures consistent presentation in story listings
     * by limiting description length. If the description exceeds 200 characters,
     * it is cut off and an ellipsis ("...") is appended to indicate truncation.
     * Descriptions of 200 characters or less are returned unchanged.</p>
     * 
     * <p>This method is called internally by all story retrieval methods that
     * return lists or pages of stories to optimize UI rendering and maintain
     * consistent card layouts.</p>
     *
     * @param description the full story description text to truncate
     * @return the truncated description (max 200 chars + "...") if longer than
     *         200 characters, otherwise returns the original description unchanged.
     *         Returns the original value if {@code null}
     */
    public String truncateDescription(String description){
        if(description != null && description.length() > 200){
            return description.substring(0,200) + "...";
        }else {
            return description;
        }

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


}
