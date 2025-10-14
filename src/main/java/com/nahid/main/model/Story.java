package com.nahid.main.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "story_table")
public class Story {

    @Id
    @GeneratedValue
    private UUID storyId;
    private String storyFor;
    private String storyBy;
    private String title;
    @Lob
    private String message;
    private boolean isPositive;
    private LocalDateTime createdAt;

}
