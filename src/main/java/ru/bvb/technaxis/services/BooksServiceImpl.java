package ru.bvb.technaxis.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bvb.technaxis.dto.BookCreationDto;
import ru.bvb.technaxis.dto.BookEditDto;
import ru.bvb.technaxis.models.Book;
import ru.bvb.technaxis.repositories.BooksRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class BooksServiceImpl implements BooksService {

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${amazon.aws.bucket}")
    private String bucketName;

    @Override
    public List<Book> getBooks(Integer page, Integer count) {
        Pageable pageRequest = PageRequest.of(page, count);
        Page<Book> books = booksRepository.findAll(pageRequest);
        return books.getContent();
    }

    @Override
    public Book getBook(Long id) {
        Book book = booksRepository.getOne(id);
        return book;
    }

    @Override
    public Book readBook(Long id) {
        Book book = booksRepository.getOne(id);
        if (!book.getReadAlready()) {
            book.setReadAlready(true);
            booksRepository.save(book);
        }
        return book;
    }

    @Override
    public Book changeBook(Long id, BookEditDto bookDto, MultipartFile file) {
        Book book = booksRepository.getOne(id);
        if (bookDto != null) {
            if (bookDto.getTitle() != null) {
                book.setTitle(bookDto.getTitle());
            }
            if (bookDto.getDescription() != null) {
                book.setDescription(bookDto.getDescription());
            }
            if (bookDto.getIsbn() != null) {
                book.setIsbn(bookDto.getIsbn());
            }
            if (bookDto.getPrintYear() != null) {
                book.setPrintYear(bookDto.getPrintYear());
            }
        }
        if (file != null) {
            book.setImage(uploadFile(file));
        }
        book.setReadAlready(false);
        booksRepository.save(book);
        return book;
    }

    @Override
    public List<Book> searchBooks(String query, Integer page, Integer count) {
        Pageable pageRequest = PageRequest.of(page, count);
        Page<Book> books = booksRepository.search(query, pageRequest);
        return books.getContent();
    }

    @Override
    public Book save(BookCreationDto dto, MultipartFile file) {
        Book book = Book.builder()
                .author(dto.getAuthor())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .isbn(dto.getIsbn())
                .readAlready(false)
                .printYear(dto.getPrintYear())
                .image(uploadFile(file))
                .build();
        return booksRepository.save(book);
    }

    private String uploadFile(MultipartFile multipartFile) {
        File file = convert(multipartFile);
        String name = UUID.randomUUID() + file.getName();
        amazonS3.putObject(new PutObjectRequest(bucketName, name, file));
        return name;
    }

    private File convert(MultipartFile multipartFile) {
        File converted = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(converted)) {
            fos.write(multipartFile.getBytes());
        } catch (FileNotFoundException e) {
            log.error("File not found, cant convert");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return converted;
    }
}
