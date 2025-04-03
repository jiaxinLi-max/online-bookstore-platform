package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Cart;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Stockpile;
import com.example.tomatomall.repository.CartRepository;
import com.example.tomatomall.repository.ProductRepository;
import com.example.tomatomall.repository.StockpileRepository;
import com.example.tomatomall.service.CartService;
import com.example.tomatomall.vo.CartResponseVO;
import com.example.tomatomall.vo.CartVO;
import com.example.tomatomall.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public CartVO addPIntoCart(Integer userId,Integer productId,Integer quantity){
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

        // **构造 CartVO**
        CartVO cartVO = cart.toVO();
        cartVO.setTitle(product.getTitle());
        cartVO.setPrice(product.getPrice());
        cartVO.setDescription(product.getDescription());
        cartVO.setCover(product.getCover());
        cartVO.setDetail(product.getDetail());

        return cartVO;
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
    public CartResponseVO getPInCart(Integer userId){
        // 查询用户购物车中的商品列表
        List<Cart> cartList = cartRepository.findByUserId(userId);

        // 转换为 CartVO 列表，并计算总价
        List<CartVO> cartVOList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Cart cart : cartList) {
            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new RuntimeException("商品不存在"));

            CartVO cartVO = cart.toVO();
            cartVO.setTitle(product.getTitle());
            cartVO.setPrice(product.getPrice());
            cartVO.setDescription(product.getDescription());
            cartVO.setCover(product.getCover());
            cartVO.setDetail(product.getDetail());

            cartVOList.add(cartVO);

            // 计算购物车总金额
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
        }

        // 组装返回对象
        CartResponseVO response = new CartResponseVO();
        response.setItems(cartVOList);
        response.setTotal(cartVOList.size()); // 计算购物车不同商品种类的数量
        response.setTotalAmount(totalAmount);

        return response;
    }
}
