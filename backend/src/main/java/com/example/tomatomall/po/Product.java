package com.example.tomatomall.po;


import com.example.tomatomall.vo.ProductVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//用来和数据库进行交互
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="id",nullable = false)
    private Integer id;

    @Basic
    @Column(name="title",nullable = false)
    private String title;

    @Basic
    @Column(name="price",nullable = false)
    private BigDecimal price;

    @Basic
    @Column(name="rate",nullable = false)
    private Float rate;

    @Basic
    @Column(name="description")
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_covers", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "cover_url")
    private List<String> cover = new ArrayList<>();


    @Basic
    @Column(name = "detail")
    private String detail;

    //和规格的关联
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Specification> specifications; // 关联规格

    //和库存的关联
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stockpile stockpile;

    @ManyToMany
    @JoinTable(
            name = "product_columns",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "columns_id")
    )
    private Set<Columns> columns = new HashSet<>();


    public void setSpecifications(Set<Specification> specifications) {

        for (Specification spec : specifications) {
            spec.setProduct(this);  // 关键：让 Specification 关联 Product
        }
        this.specifications = specifications;
    }

    public ProductVO toVO(){
        ProductVO productVO=new ProductVO();
        productVO.setId(this.id);
        productVO.setPrice(this.price);
        productVO.setRate(this.rate);
        productVO.setDetail(this.detail);
        productVO.setCover(this.cover);
        productVO.setTitle(this.title);
        productVO.setDescription(this.description);
        productVO.setSpecifications(this.specifications);
        if (this.columns != null) {
            List<Integer> columnIds = this.columns.stream()
                    .map(Columns::getId)
                    .collect(Collectors.toList());
            productVO.setColumnIds(columnIds);
        }

        return productVO;
    }
}
