package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Cart;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Stockpile;
import com.example.tomatomall.repository.CartRepository;
import com.example.tomatomall.repository.ProductRepository;
import com.example.tomatomall.repository.StockpileRepository;
import com.example.tomatomall.service.CartService;
import com.example.tomatomall.vo.CartVO;
import com.example.tomatomall.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    StockpileRepository stockpileRepository;

    @Autowired
    CartRepository cartRepository;

    @Override
    public Boolean addPIntoCart(Integer userId,Integer productId,Integer quantity){
        Product product=productRepository.findById(productId).orElse(null);
        if(product==null){
            throw TomatoMallException.productNotExist();
        }
        Stockpile stockpile=stockpileRepository.findByProductId(productId);
        //超出可卖库存数
        if(stockpile==null){
            throw TomatoMallException.exceedAmount();
        }
        if(quantity>stockpile.getAmount()){
            throw TomatoMallException.exceedAmount();
        }
        Cart cart=cartRepository.findByUserIdAndProductId(userId,productId);
        if(cart==null){
            //购物车中无记录
            cart=new Cart();
            cart.setQuantity(quantity);
            cart.setUserId(userId);
            cart.setProductId(productId);
        }else{
            //购物车中有记录
            Integer newQuantity=cart.getQuantity()+quantity;
            if(newQuantity>stockpile.getAmount()){
                throw TomatoMallException.exceedAmount();
            }
            cart.setQuantity(newQuantity);
        }
        cartRepository.save(cart);
        return true;
    }

    @Override
    public Boolean deletePInCart(Integer userId,Integer cart_item_id){
        Cart cart=cartRepository.findById(cart_item_id).orElse(null);
        if(cart==null){
            throw TomatoMallException.productNotExistInCart();
        }
        if(!cart.getUserId().equals(userId)){
            throw TomatoMallException.noPermissionToDelete();
        }
        cartRepository.delete(cart);
        return true;
    }

    @Override
    public Boolean modifyQuantity(Integer userId,Integer cart_item_id,Integer quantity){
        Cart cart=cartRepository.findById(cart_item_id).orElse(null);
        if(cart==null){
            throw TomatoMallException.productNotExistInCart();
        }
        if(!cart.getUserId().equals(userId)){
            throw TomatoMallException.noPermissionToDelete();
        }
        Stockpile stockpile=stockpileRepository.findByProductId(cart.getProductId());
        if (stockpile==null){
            throw TomatoMallException.exceedAmount();
        }
        if(quantity>stockpile.getAmount()){
            throw TomatoMallException.exceedAmount();
        }
        cart.setQuantity(quantity);
        cartRepository.save(cart);
        return true;
    }

    @Override
    public List<CartVO> getPInCart(Integer userId){
        return cartRepository.findByUserId(userId).stream().map(Cart::toVO).collect(Collectors.toList());

    }
}
