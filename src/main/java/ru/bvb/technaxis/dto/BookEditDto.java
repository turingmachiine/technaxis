package ru.bvb.technaxis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookEditDto {
    private String title;
    private String description;
    private String isbn;
    private Integer printYear;
}
