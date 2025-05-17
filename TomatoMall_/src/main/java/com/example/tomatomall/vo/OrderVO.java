package com.example.tomatomall.vo;

import com.example.tomatomall.po.Address;
import com.example.tomatomall.po.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@NoArgsConstructor
@Getter
@Setter
public class OrderVO {

    private Integer id;


    private Integer userId;

    private List<Integer> cartItemIds;

    private Address shipping_address;

    private String payment_method;

    private BigDecimal totalAmount;

    private Date createTime;

    private  String status;

    private Date paymentTime;

    private String tradeNo;

    private String paymentForm;

    //积分
    private BigDecimal realAmount;

    public Order toPO(){
        Order order=new Order();
        order.setId(this.id);
        order.setUserId(this.userId);
        order.setCartItemIds(this.cartItemIds);
        order.setShippingAddress(this.shipping_address);
        order.setPaymentMethod(this.payment_method);
        order.setTotalAmount(this.totalAmount);
        order.setCreateTime(this.createTime);
        order.setStatus(this.status);
        order.setPaymentTime(this.paymentTime);
        order.setTradeNo(this.tradeNo);
        order.setPaymentForm(this.paymentForm);
        order.setRealAmount(this.realAmount);
        return order;
    }
}
