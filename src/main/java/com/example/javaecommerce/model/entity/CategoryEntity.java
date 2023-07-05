package com.example.javaecommerce.model.entity;

import com.example.javaecommerce.model.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "category")
public class CategoryEntity extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "id", updatable = false, nullable = false)
    private Long id;

    @NotBlank(message = "category name  must not be empty")
    private String name;
}
