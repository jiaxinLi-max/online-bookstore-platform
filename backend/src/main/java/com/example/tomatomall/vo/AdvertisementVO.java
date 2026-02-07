package com.example.tomatomall.vo;


import com.example.tomatomall.po.Advertisement;
import com.example.tomatomall.po.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdvertisementVO {
    private Integer id;
    private String title;
    private String content;
    private String imgUrl;
    private Integer productId;

    public Advertisement toPO(){
        Advertisement advertisement=new Advertisement();
        advertisement.setId(this.id);
        advertisement.setTitle(this.title);
        advertisement.setContent(this.content);
        advertisement.setImgUrl(this.imgUrl);

        Product product = new Product();
        product.setId(this.productId);
        advertisement.setProduct(product);
        return advertisement;
    }
}
