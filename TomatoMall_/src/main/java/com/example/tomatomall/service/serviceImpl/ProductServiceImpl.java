package com.example.tomatomall.service.serviceImpl;


import com.example.tomatomall.TomatoMallApplication;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.po.Specification;
import com.example.tomatomall.po.Stockpile;
import com.example.tomatomall.repository.ProductRepository;
import com.example.tomatomall.repository.SpecificationRepository;
import com.example.tomatomall.repository.StockpileRepository;
import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.StockpileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    StockpileRepository stockpileRepository;

    @Autowired
    SpecificationRepository specificationRepository;


    //获取商品列表
    @Override
    public List<ProductVO> getAllProductInfo(){
        return productRepository.findAll().stream().map(Product::toVO).collect(Collectors.toList());
    }

    //获取商品特定信息
    @Override
    public ProductVO getProductInfo(Integer id ){
        Product product=productRepository.findById(id).orElse(null);
        if(product==null){
            throw TomatoMallException.productNotExist();
        }
        return product.toVO();
    }

    //更新商品信息
    @Override
    @Transactional
    public Boolean updateProductInfo(ProductVO productVO){
        Product product=productRepository.findById(productVO.getId()).orElse(null);
        if(product==null){
            throw TomatoMallException.productNotExist();
        }
        product.setTitle(productVO.getTitle());
        product.setPrice(productVO.getPrice());
        product.setRate(productVO.getRate());
        product.setDescription(productVO.getDescription());
        product.setCover(productVO.getCover());
        product.setDetail(productVO.getDetail());
        // 操作原有集合，而非替换引用
        Set<Specification> specifications = product.getSpecifications();
        specifications.clear(); // 清空现有集合

        // 添加新的 Specification 到原集合中
        for (Specification spec : productVO.getSpecifications()) {
            spec.setProduct(product); // 确保关联关系
            specifications.add(spec); // 添加到原有集合
        }

        productRepository.save(product);
        return true;


    }

    //添加商品
    @Override
    public Boolean addProduct(ProductVO productVO){
        Product product=productRepository.findByTitle(productVO.getTitle());
        if(product!=null){
            throw TomatoMallException.titleAlreadyExist();
        }
        Product newProduct=productVO.toPO();
        productRepository.save(newProduct);
        return true;
    }

    //删除商品
    @Override
    @Transactional
    public Boolean deleteProduct(Integer id){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            // 商品不存在，抛出异常
            throw TomatoMallException.productNotExist();
        }

        stockpileRepository.deleteByProductId(id);  // 根据 productId 删除库存信息

        specificationRepository.deleteByProductId(id);  // 根据 productId 删除规格信息

        // 删除商品
        productRepository.deleteById(id);
        return true;  // 返回操作成功
    }

    //更新商品库存
    @Override
    public Boolean updateStock(Integer productId, Integer amount){
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            // 商品不存在，抛出异常
            throw TomatoMallException.productNotExist();
        }
        Stockpile stockpile = stockpileRepository.findByProductId(productId);
        if (stockpile == null) {
            stockpile = new Stockpile();
            stockpile.setProduct(product);
        }

        stockpile.setAmount(amount); // 更新库存数
        stockpileRepository.save(stockpile);

        return true; // 返回操作成功
    }
    //查询商品库存
    @Override
    public StockpileVO getStock(Integer id){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            // 商品不存在，抛出异常
            throw TomatoMallException.productNotExist();
        }
        Stockpile stockpile = stockpileRepository.findByProductId(id);
        if (stockpile == null) {
            stockpile=new Stockpile();
            stockpile.setAmount(0);
            stockpile.setProduct(product);
            stockpileRepository.save(stockpile);
        }
        return stockpile.toVO();
    }
}
