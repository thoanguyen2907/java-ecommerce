package com.example.javaecommerce.model.entity;

import com.example.javaecommerce.model.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {
    @NotBlank(message = "name must not be empty")
    private String name;
    @NotNull
    @NotBlank(message = "brand must not be empty")
    private String brand;
    @NotNull
    @NotBlank(message = "description must not be empty")
    private String description;
    private int rating;
    @NotNull(message= "price may not be empty")
    private int price;
    @NotNull(message= "number of stock may not be empty")
    private int count_in_stock;
    @ManyToOne()
    @JoinColumn(
            name = "categoryId")
    private CategoryEntity category;
    @ManyToOne()
    @JoinColumn(
            name = "order_detail_id")
    private OrderDetailEntity orderDetail;
}
