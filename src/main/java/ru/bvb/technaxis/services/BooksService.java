package ru.bvb.technaxis.services;

import org.springframework.web.multipart.MultipartFile;
import ru.bvb.technaxis.dto.BookCreationDto;
import ru.bvb.technaxis.dto.BookEditDto;
import ru.bvb.technaxis.models.Book;

import java.util.List;

public interface BooksService {
    List<Book> getBooks(Integer page, Integer count);
    Book getBook(Long id);
    Book readBook(Long id);
    Book changeBook(Long id, BookEditDto bookDto, MultipartFile file);
    List<Book> searchBooks(String query, Integer page, Integer count);
    Book save(BookCreationDto dto, MultipartFile file);

}
