package com.example.javaecommerce.model.entity;

import com.example.javaecommerce.model.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ReviewEntity extends BaseEntity {
    private int rating;
    @ManyToOne()
    @JoinColumn(
            name = "productId")
    private ProductEntity product;
}
