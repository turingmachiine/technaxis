package ru.bvb.technaxis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCreationDto {
    private String title;
    private String description;
    private String author;
    private String isbn;
    private Integer printYear;
}
