package com.example.book.service;

import com.example.book.dto.BookRequest;
import com.example.book.dto.BookResponse;
import com.example.book.model.Book;
import com.example.book.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void createBook_ShouldReturnBookResponse() {
        // given - test verileri hazırlanır
        BookRequest request = BookRequest.builder()
                .title("Test Kitap")
                .author("Test Yazar")
                .price(new BigDecimal("29.99"))
                .stock(10)
                .build();

        Book book = Book.builder()
                .id("1")
                .title(request.getTitle())
                .author(request.getAuthor())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // when - test edilecek metod çağrılır
        BookResponse response = bookService.createBook(request);

        // then - sonuçlar kontrol edilir
        assertNotNull(response);
        assertEquals(book.getId(), response.getId());
        assertEquals(request.getTitle(), response.getTitle());
        assertEquals(request.getAuthor(), response.getAuthor());
        assertEquals(request.getPrice(), response.getPrice());
        assertEquals(request.getStock(), response.getStock());
    }

    @Test
    void getBookById_WhenBookNotFound_ShouldThrowException() {
        // given
        String nonExistentBookId = "999";
        when(bookRepository.findById(nonExistentBookId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            bookService.getBookById(nonExistentBookId);
        });
    }

    @Test
    void updateBookStock_WhenBookNotFound_ShouldThrowException() {
        // given
        String nonExistentBookId = "999";
        int stockChange = 5;
        when(bookRepository.findById(nonExistentBookId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            bookService.updateBookStock(nonExistentBookId, stockChange);
        });
    }

    @Test
    void updateBookStock_WhenInsufficientStock_ShouldThrowException() {
        // given
        String bookId = "1";
        Book existingBook = Book.builder()
                .id(bookId)
                .title("Test Kitap")
                .author("Test Yazar")
                .price(new BigDecimal("29.99"))
                .stock(5)
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));

        // when & then
        assertThrows(RuntimeException.class, () -> {
            bookService.updateBookStock(bookId, -10); // Stoktan 10 adet düşürmeye çalışıyoruz ama sadece 5 adet var
        });
    }

    @Test
    void createBook_WhenTitleIsEmpty_ShouldThrowException() {
        // given
        BookRequest request = BookRequest.builder()
                .title("")  // Boş başlık
                .author("Test Yazar")
                .price(new BigDecimal("29.99"))
                .stock(10)
                .build();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.createBook(request);
        });
    }

    @Test
    void createBook_WhenPriceIsNegative_ShouldThrowException() {
        // given
        BookRequest request = BookRequest.builder()
                .title("Test Kitap")
                .author("Test Yazar")
                .price(new BigDecimal("-29.99"))  // Negatif fiyat
                .stock(10)
                .build();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.createBook(request);
        });
    }
}
