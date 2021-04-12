package com.geekbrains.rabbitmqjsonproducts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonManagedReference
    private Category category;

    @Column(name = "vendor_code")
    @Pattern(regexp = "([0-9]{1,})")
    @Size(min = 8, max = 8)
    private String vendorCode;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "product")
    @JsonBackReference
    private List<ProductImage> images;

    @NotNull(message = "Title not null")
    @Size(min = 6, message = "Title length min 5 sym")
    @Column(name = "title")
    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "full_description")
    private String fullDescription;

    @NotNull(message = "price not null")
    @Min(value = 1, message = "Min 1")
    @Column(name = "price")
    private double price;

    @Column(name = "create_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
