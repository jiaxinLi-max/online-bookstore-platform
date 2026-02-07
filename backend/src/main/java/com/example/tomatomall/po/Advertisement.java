package com.example.tomatomall.po;

import com.example.tomatomall.vo.AdvertisementVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Advertisement {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="id",nullable = false)
    private Integer id;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "img_url")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore  // 关键：防止 product 进入无限递归
    private Product product; // 与商品类的关联，避免冗余存储 productId

    public AdvertisementVO toVO(){
        AdvertisementVO advertisementVO=new AdvertisementVO();
        advertisementVO.setId(this.id);
        advertisementVO.setTitle(this.title);
        advertisementVO.setContent(this.content);
        if(this.product!=null) {
            advertisementVO.setProductId(product.getId());
        }else{
            advertisementVO.setProductId(null);
        }
        advertisementVO.setImgUrl(this.imgUrl);
        return advertisementVO;
    }
}
