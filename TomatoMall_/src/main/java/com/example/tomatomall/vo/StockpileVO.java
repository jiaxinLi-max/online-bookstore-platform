package com.example.tomatomall.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockpileVO {
    private String id;        // 库存ID
    private Integer amount;       // 剩余库存数量
    private Integer frozen;       // 已冻结库存（可能用于订单未支付状态）
    private String productId; // 关联的商品ID
}
