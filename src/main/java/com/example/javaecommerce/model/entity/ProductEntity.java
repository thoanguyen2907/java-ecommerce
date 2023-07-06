package com.example.javaecommerce.model.entity;

import com.example.javaecommerce.model.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "products")
public class ProductEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @NotBlank(message = "name must not be empty")
    private String name;
    private String image;
    @NotNull
    @NotBlank(message = "brand must not be empty")
    private String brand;
    @NotNull
    @NotBlank(message = "description must not be empty")
    private String description;
    private int rating;
    @NotNull(message = "price may not be empty")
    private int price;
    @NotNull(message = "number of stock may not be empty")
    private int countInStock;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
}


