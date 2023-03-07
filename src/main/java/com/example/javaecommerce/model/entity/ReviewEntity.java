package com.example.javaecommerce.model.entity;

import com.example.javaecommerce.model.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ReviewEntity extends BaseEntity {
    private int rating;

    @ManyToOne(fetch= FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private ProductEntity product;
}
