package com.example.book.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
@CompoundIndex(name = "title_author_idx", def = "{'title': 1, 'author': 1}")
public class Book implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;
    
    @NotBlank(message = "Title is required")
    @Indexed
    private String title;
    
    @NotBlank(message = "Author is required")
    @Indexed
    private String author;
    
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private BigDecimal price;
    
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private Integer stock;
}
