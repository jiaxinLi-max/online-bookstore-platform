package com.example.tomatomall.po;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;

//用来和数据库进行交互
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="id")
    private String id;

    @Column(name="title")
    private String title;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="rate")
    private D

}
