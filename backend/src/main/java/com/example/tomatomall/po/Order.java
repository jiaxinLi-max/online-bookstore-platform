package com.example.tomatomall.po;

import com.example.tomatomall.vo.OrderVO;
import com.example.tomatomall.vo.ProductVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Basic
    @Column(name = "user_id",nullable = false)
    private Integer userId;

    @ElementCollection
    @CollectionTable(name = "orders_cart_items", joinColumns = @JoinColumn(name = "orders_id"))
    @Column(name = "cart_item_id",nullable = false)
    private List<Integer> cartItemIds;



//    @Basic
//    @Column(name ="cart_item_ids")
//    private List<Integer> cartItemIds;


//    @Embeddable
//    public class Address {
//        private String name;
//        private String phone;
//        private String address;
//    }

    @Embedded
    private Address shippingAddress;


    @Basic
    @Column(name ="payment_method")
    private String paymentMethod;

    @Basic
    @Column(name = "total_amount",nullable = false)
    private BigDecimal totalAmount;

    //积分
    @Basic
    @Column(name = "real_amount",nullable = false)
    private BigDecimal realAmount;

    @Basic
    @Column(name = "create_time",nullable = false)
    private Date createTime;

    @Basic
    @Column(name = "status",nullable = false)
    private  String status;

    @Basic
    @Column(name = "payment_time")
    private Date paymentTime;

    @Basic
    @Column(name = "trade_no")
    private String tradeNo;

    @Basic
    @Column(name = "payment_form", length = 1024)
    private String paymentForm;

    @Basic
    @Column(name = "expire_time")
    private LocalDateTime expireTime;



    public OrderVO toVO(){
        OrderVO orderVO=new OrderVO();
        orderVO.setId(this.id);
        orderVO.setUserId(this.userId);
        orderVO.setCartItemIds(this.cartItemIds);
        orderVO.setShipping_address(this.shippingAddress);
        orderVO.setPayment_method(this.paymentMethod);
        orderVO.setTotalAmount(this.totalAmount);
        orderVO.setCreateTime(this.createTime);
        orderVO.setStatus(this.status);
        orderVO.setPaymentTime(this.paymentTime);
        orderVO.setTradeNo(this.tradeNo);
        orderVO.setPaymentForm(this.paymentForm);
        orderVO.setRealAmount(this.realAmount);
        return orderVO;
    }
}
