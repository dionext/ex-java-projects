package com.dionext.config;

import com.dionext.entity.Book;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.dionext.repository.BookRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(BookRepository bookRepository) {
        return args -> {
            // Create sample books
            Book book1 = new Book();
            book1.setTitle("The Great Gatsby");
            book1.setAuthor("F. Scott Fitzgerald");
            book1.setIsbn("978-0743273565");
            book1.setPublicationYear(1925);

            Book book2 = new Book();
            book2.setTitle("To Kill a Mockingbird");
            book2.setAuthor("Harper Lee");
            book2.setIsbn("978-0446310789");
            book2.setPublicationYear(1960);

            Book book3 = new Book();
            book3.setTitle("1984");
            book3.setAuthor("George Orwell");
            book3.setIsbn("978-0451524935");
            book3.setPublicationYear(1949);

            // Save books to database
            bookRepository.save(book1);
            bookRepository.save(book2);
            bookRepository.save(book3);
        };
    }
} 