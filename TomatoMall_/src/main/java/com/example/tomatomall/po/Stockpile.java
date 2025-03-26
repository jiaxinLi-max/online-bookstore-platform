package com.example.tomatomall.po;

import com.example.tomatomall.vo.StockpileVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Stockpile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 库存的唯一标识符


    @Column(name = "amount", nullable = false)
    private Integer amount; // 可卖库存数

    @Column(name="frozen",nullable = false)
    private Integer frozen; // 冻结库存数（不可卖）

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product; // 与商品类的关联，避免冗余存储 productId

    public StockpileVO toVO(){
        StockpileVO stockpileVO=new StockpileVO();
        stockpileVO.setId(this.id);
        stockpileVO.setAmount(this.amount);
        stockpileVO.setFrozen(this.frozen);
        stockpileVO.setProductId(this.product.getId());
        return stockpileVO;
    }
}
