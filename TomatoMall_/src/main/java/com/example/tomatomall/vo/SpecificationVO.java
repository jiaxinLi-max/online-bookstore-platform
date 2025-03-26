package com.example.tomatomall.vo;

import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Specification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecificationVO {
    private Integer id;       // 规格项ID
    private String item;     // 规格项名称（如“作者”）
    private String value;    // 规格项内容（如“Robert C. Martin”）
    private Integer productId; // 关联的商品ID

    public Specification toPO(){
        Specification specification=new Specification();
        specification.setId(this.id);
        specification.setItem(this.item);
        specification.setValue(this.value);

        Product product = new Product();
        product.setId(this.productId);
        specification.setProduct(product);
        return specification;
    }
}
