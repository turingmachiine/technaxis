package ru.bvb.technaxis.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bvb.technaxis.models.Book;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {

    @Query("from Book book where " +
            "upper(book.author) like concat('%', upper(:query), '%') or " +
            "upper(book.title) like concat('%', upper(:query), '%') or " +
            "upper(book.description) like concat('%', upper(:query), '%')")
    Page<Book> search(@Param("query") String query, Pageable pageable);

}
