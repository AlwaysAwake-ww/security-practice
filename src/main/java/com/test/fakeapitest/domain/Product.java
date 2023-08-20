package com.test.fakeapitest.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="product")
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productTitle;

    private Double price;

    private String description;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String imageUrl;


    // Embedded : JPA에서 사용
    // Entity 안의 Column 을 하나의 객체로 활용하고자 할 떄 사용
    // product 테이블에는 Rating 클래스의 내용들이 함께 생성됨
    @Embedded
    private Rating rating;

}
