package com.example.book.controller;

import com.example.book.exception.ResourceNotFoundException;
import com.example.book.model.Book;
import com.example.book.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Validated
@Tag(name = "Book Controller", description = "Book management endpoints")
public class BookController {

    private final BookRepository bookRepository;

    @Operation(summary = "Create a new book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public Book createBook(@Valid @RequestBody Book book) {
        return bookRepository.save(book);
    }

    @Operation(summary = "Get all books")
    @ApiResponse(responseCode = "200", description = "List all books")
    @GetMapping
    public Page<Book> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author) {
        PageRequest pageRequest = PageRequest.of(page, size);
        
        if (title != null) {
            return bookRepository.findByTitleContainingIgnoreCase(title, pageRequest);
        } else if (author != null) {
            return bookRepository.findByAuthorContainingIgnoreCase(author, pageRequest);
        }
        
        return bookRepository.findAll(pageRequest);
    }

    @Operation(summary = "Get a book by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book found"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    @Cacheable(value = "books", key = "#id")
    public Book getBookById(@PathVariable String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Operation(summary = "Update a book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book updated successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PutMapping("/{id}")
    @CacheEvict(value = "books", key = "#id")
    public Book updateBook(@PathVariable String id, @Valid @RequestBody Book book) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    book.setId(id);
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Operation(summary = "Delete a book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/{id}")
    @CacheEvict(value = "books", key = "#id")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
}
