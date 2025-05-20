package com.example.tomatomall.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.*;
import com.example.tomatomall.repository.*;
import com.example.tomatomall.service.AccountService;
import com.example.tomatomall.service.CartService;
import com.example.tomatomall.service.OrderService;
import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.vo.OrderVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceLmpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @Autowired
    StockpileRepository stockpileRepository;

    @Autowired
    CartsOrdersRelationRepository cartsOrdersRelationRepository;

    @Autowired
    AccountService accountService;


    @Value("${alipay.serverUrl}")
    private String serverUrl;
    @Value("${alipay.notifyUrl}")
    private String notifyUrl;

    @Value("${alipay.appId}")
    private String appId;
    @Value("${alipay.appPrivateKey}")
    private String privateKey;
    @Value("${alipay.alipayPublicKey}")
    private String alipayPublicKey;

    private static final String FORMAT = "JSON";
//    @Override
//    public OrderVO createOrder(Integer userId, List<String> cartItemIds, Object shipping_address, String payment_method){
//
//    }

    @Override
    public OrderVO createOrder(OrderVO orderVO){
        Order order=orderVO.toPO();
        order.setCreateTime(new Date());
        order.setTotalAmount(calculateTotalAmount(orderVO.getCartItemIds()));
        Account account= accountService.findById(orderVO.getId());
        if(account==null){
            throw TomatoMallException.userNotExist();
        }
        double discount=1.0-account.getGrade()*0.05;
        order.setRealAmount(order.getTotalAmount().multiply(BigDecimal.valueOf(discount)));
        order.setStatus("PENDING");
        orderRepository.save(order);
        order.setExpireTime(LocalDateTime.now().plusMinutes(30));
        return order.toVO();
    }

    @Override
    public OrderVO sendPay(Integer order_id, HttpServletResponse httpResponse){
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId,
                    privateKey, FORMAT, "utf-8", alipayPublicKey, "RSA2");
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();  // 发送请求的 Request类
            request.setNotifyUrl(notifyUrl);
            Order order = orderRepository.findById(order_id).orElseThrow(TomatoMallException::orderNotExist);
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", String.valueOf(order_id));
            bizContent.put("total_amount", String.valueOf(order.getRealAmount()));

            bizContent.put("subject", "番茄书城订单 #" + order_id); // 商品标题（必填）
            bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");  // 固定配置
            request.setBizContent(bizContent.toString());
            String form = "";
            try {
                form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
            order.setPaymentForm(form);
//            httpResponse.setContentType("text/html;charset=" + "utf-8");
//            httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
//            httpResponse.getWriter().flush();
//            httpResponse.getWriter().close();

            orderRepository.save(order);
            return order.toVO();
        }
        catch(Exception e) {
            System.out.println("支付失败: " + e.getMessage());
            return null; // 或者返回错误的 OrderVO
        }
    }

    @Override
    public OrderVO payNotify(HttpServletRequest request, HttpServletResponse response){
        try{
//            Map<String, String> params = new HashMap<>();
//            Map<String, String[]> requestParams = request.getParameterMap();
//            for (String name : requestParams.keySet()) {i
//                params.put(name, request.getParameter(name));
//                // System.out.println(name + " = " + request.getParameter(name));
//            }
//            String sign = params.get("sign");
//            String content = AlipaySignature.getSignCheckContentV1(params);
//            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8"); // 验证签名
            Map<String, String> params = request.getParameterMap().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0]));

            // 2. 验证支付宝签名（防止伪造请求）
            boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayPublicKey, "UTF-8", "RSA2");

//            boolean signVerified=true;
            if (!signVerified) {
//                response.getWriter().print("fail"); // 签名验证失败，返回 fail
                throw TomatoMallException.alipayFailure();
            }

                String tradeStatus = params.get("trade_status");
                String orderId = params.get("out_trade_no");
                Integer id = Integer.valueOf(orderId);
                Order order = orderRepository.findById(Integer.parseInt(orderId))
                        .orElseThrow(TomatoMallException::orderNotExist);
                if ("TRADE_SUCCESS".equals(tradeStatus)) {
                    // TODO: 处理支付成功逻辑
                    // - 获取订单信息
                    // - 验证订单金额
                    // - 更新订单状态
                    // - 更新库存记录
                    order.setStatus("TRADE_SUCCESS");
                    String tradeNo=params.get("trade_no");
                    order.setTradeNo(tradeNo);
                    order.setPaymentTime(new Date());
                    orderRepository.save(order);

                    decreaseStockpile(order.getCartItemIds(),id);
//                    response.getWriter().print("success");


                } else if ("TRADE_CLOSED".equals(tradeStatus)) {
                    // TODO: 处理交易关闭逻辑
                    // - 更新订单状态
                    // - 记录关闭原因
                    // - 解除库存锁定
                    order.setStatus("TRADE_CLOSED");
                    orderRepository.save(order);
                    releaseFrozenStock(order.getCartItemIds());

                }
                System.out.println("支付回调处理成功: ");
                return order.toVO();


        } catch(Exception e){
            System.out.println("支付回调处理失败: " + e.getMessage());
            return null; // 或者返回错误的 OrderVO
        }
    }

    @Override
    public OrderVO getOrderById(Integer order_id){
        return orderRepository.findById(order_id)
                .orElseThrow(TomatoMallException::orderNotExist)
                .toVO();
    }

    private BigDecimal calculateTotalAmount(List<Integer> cartItemIds) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Integer cartItemId : cartItemIds) {
            // 查询购物车商品信息
            Cart cartItem = cartService.findById(cartItemId);
            if (cartItem != null) {
                Integer productId=cartItem.getProductId();
                // 单价 * 数量
                Stockpile stockpile=stockpileRepository.findByProductId(productId);
                //超出可卖库存数
                if(stockpile==null){
                    throw TomatoMallException.exceedAmount();
                }
                if(cartItem.getQuantity()>stockpile.getAmount()){
                    throw TomatoMallException.exceedAmount();
                }
                stockpile.setAmount(stockpile.getAmount()-cartItem.getQuantity());
                stockpile.setFrozen(stockpile.getFrozen()+cartItem.getQuantity());
                stockpileRepository.save(stockpile);
                Product product= productService.findById(productId);
                if(product==null){
                    throw TomatoMallException.productNotExist();
                }
                BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                totalAmount = totalAmount.add(itemTotal);
            }
        }

        return totalAmount;
    }


    private void decreaseStockpile(List<Integer> cartItemIds,Integer orderId){
        for(Integer cartItemId:cartItemIds){
            Cart cartItem = cartService.findById(cartItemId);
            if (cartItem != null) {
                Integer productId=cartItem.getProductId();
                Stockpile stockpile=stockpileRepository.findByProductId(productId);
                //超出可卖库存数
                if(stockpile==null){
                    throw TomatoMallException.exceedAmount();
                }
                if(cartItem.getQuantity()>stockpile.getAmount()){
                    throw TomatoMallException.exceedAmount();
                }
                stockpile.setFrozen(stockpile.getFrozen()-cartItem.getQuantity());
                CartsOrdersRelation cartsOrdersRelation_new=new CartsOrdersRelation();
                cartsOrdersRelation_new.setOrderId(orderId);
                cartsOrdersRelation_new.setCartitemId(cartItemId);
                cartsOrdersRelationRepository.save(cartsOrdersRelation_new);
                stockpileRepository.save(stockpile);
            }
        }
    }

    //如果支付失败的话
    private void releaseFrozenStock(List<Integer> cartItemIds) {
        for (Integer cartItemId : cartItemIds) {
            Cart cartItem = cartService.findById(cartItemId);
            if (cartItem != null) {
                Stockpile stockpile = stockpileRepository.findByProductId(cartItem.getProductId());
                if (stockpile != null) {
                    stockpile.setAmount(stockpile.getAmount() + cartItem.getQuantity());
                    stockpile.setFrozen(stockpile.getFrozen() - cartItem.getQuantity());
                    stockpileRepository.save(stockpile);
                }
            }
        }
    }


}
