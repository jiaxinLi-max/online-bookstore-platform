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
    @Column(name = "user_id")
    private Integer userId;

    @ElementCollection
    @CollectionTable(name = "order_cart_items", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "cart_item_id")
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
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Basic
    @Column(name = "create_time")
    private Date createTime;

    @Basic
    @Column(name = "status")
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
        orderVO.setShippingAddress(this.shippingAddress);
        orderVO.setPaymentMethod(this.paymentMethod);
        orderVO.setTotalAmount(this.totalAmount);
        orderVO.setCreateTime(this.createTime);
        orderVO.setStatus(this.status);
        orderVO.setPaymentTime(this.paymentTime);
        orderVO.setTradeNo(this.tradeNo);
        orderVO.setPaymentForm(this.paymentForm);
        return orderVO;
    }
}
