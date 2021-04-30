package ru.bvb.technaxis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bvb.technaxis.dto.BookCreationDto;
import ru.bvb.technaxis.dto.BookEditDto;
import ru.bvb.technaxis.models.Book;
import ru.bvb.technaxis.services.BooksService;

import java.util.List;

@RestController
@RequestMapping(("/api/books"))
public class BookController {

    @Autowired
    private BooksService booksService;

    @PutMapping("/{id}")
    public Book changeBook(@PathVariable Long id, @RequestPart(required = false) BookEditDto bookDto,
                           @RequestPart(required = false) MultipartFile file) {
        if (bookDto != null || file != null) {
            return booksService.changeBook(id, bookDto, file);
        } else {
            return booksService.readBook(id);
        }
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public Book addBook(@RequestPart("json") BookCreationDto book, @RequestPart("file") MultipartFile file) {

        return booksService.save(book, file);
    }

    @GetMapping
    public List<Book> getBooks(@RequestParam(name = "page", defaultValue = "0") Integer page,
                               @RequestParam(name = "count", defaultValue = "10") Integer count) {
        return booksService.getBooks(page, count);
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        Book book = booksService.getBook(id);
        return book;
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam("text") String text,
                                  @RequestParam(name = "page", defaultValue = "0") Integer page,
                                  @RequestParam(name = "count", defaultValue = "10") Integer count) {
        return booksService.searchBooks(text, page, count);
    }

}
