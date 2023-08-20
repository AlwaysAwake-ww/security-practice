package com.test.fakeapitest.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Rating {

    private Double rate;
    private Integer count;

}
