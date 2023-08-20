package com.test.fakeapitest.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@NoArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // userId 자동 생성
    private Long id;
    private Long memberId;
    private String date;

    
    // cart : cartItem 
    // 1 : N 매핑
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CartItem> cartItem = new ArrayList<>();


}
