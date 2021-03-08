package com.cos.phoneapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

    private Long id;
    private String name;
    private String tel;

    public Phone (String name, String tel) {
        this.name = name;
        this.tel = tel;
    }

}
