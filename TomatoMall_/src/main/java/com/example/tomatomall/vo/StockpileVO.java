package com.example.tomatomall.vo;

import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Stockpile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockpileVO {
    private Integer id;        // 库存ID
    private Integer amount=0;       // 剩余库存数量
    private Integer frozen=0;       // 已冻结库存（可能用于订单未支付状态）
    private Integer productId; // 关联的商品ID

    public Stockpile toPO(){
        Stockpile stockpile=new Stockpile();
        stockpile.setId(this.id);
        stockpile.setAmount(this.amount);
        stockpile.setFrozen(this.frozen);

        Product product=new Product();
        product.setId(this.productId);
        stockpile.setProduct(product);
        return stockpile;
    }
    public StockpileVO(Integer productId, Integer amount, Integer frozen){
        this.productId=productId;
        this.amount=amount;
        this.frozen=frozen;
    }
}
