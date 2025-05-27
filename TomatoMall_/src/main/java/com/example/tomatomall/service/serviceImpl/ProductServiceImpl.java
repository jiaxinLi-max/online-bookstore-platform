package com.example.tomatomall.service.serviceImpl;


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
    public ProductVO getProductInfo(String id){
        try {

            Integer productId = Integer.valueOf(id);  // 把String类型的id转为Integer
            System.out.println(productId);  // 打印错误的 ID 格式
            Product product = productRepository.findById(productId).orElse(null);
            if (product == null) {
                throw TomatoMallException.productNotExist();
            }
            return product.toVO();
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format: " + id);  // 打印错误的 ID 格式
            throw TomatoMallException.invalidProductId();
        }
    }

    //更新商品信息
    @Override
    @Transactional
    public String updateProductInfo(ProductVO productVO){
        Product product = productRepository.findById(productVO.getId()).orElse(null);
        if (product == null) {
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
        return "更新成功";
    }

    //添加商品
    @Override
    public ProductVO addProduct(ProductVO productVO){
        Product product = productRepository.findByTitle(productVO.getTitle());
        if (product != null) {
            throw TomatoMallException.titleAlreadyExist();
        }
        Product newProduct = productVO.toPO();
        productRepository.save(newProduct);
        return newProduct.toVO();
    }

    //删除商品
    @Override
    @Transactional
    public String deleteProduct(String id){
        try {
            Integer productId = Integer.valueOf(id);  // 转换为 Integer 类型
            Product product = productRepository.findById(productId).orElse(null);
            if (product == null) {
                throw TomatoMallException.productNotExist();
            }

            stockpileRepository.deleteByProductId(productId);  // 根据 productId 删除库存信息
            specificationRepository.deleteByProductId(productId);  // 根据 productId 删除规格信息

            // 删除商品
            productRepository.deleteById(productId);
            return "删除成功";
        } catch (NumberFormatException e) {
            throw TomatoMallException.invalidProductId();  // 处理无效的ID格式
        }
    }

    //更新商品库存
    @Override
    public String updateStock(String productId, StockpileVO stockpileVO){
        try {
            Integer id = Integer.valueOf(productId);  // 转换为 Integer 类型
            Product product = productRepository.findById(id).orElse(null);
            if (product == null) {
                throw TomatoMallException.productNotExist();
            }
            Stockpile stockpile = stockpileRepository.findByProductId(id);
            if (stockpile == null) {
                stockpile = new Stockpile();
                stockpile.setAmount(0);
                stockpile.setProduct(product);
            }

            stockpile.setAmount(stockpileVO.getAmount()); // 更新库存数
            stockpileRepository.save(stockpile);

            return "调整库存成功"; // 返回操作成功
        } catch (NumberFormatException e) {
            throw TomatoMallException.invalidProductId();  // 处理无效的ID格式
        }
    }

    //查询商品库存
    @Override
    public StockpileVO getStock(String id){
        try {
            Integer productId = Integer.valueOf(id);  // 转换为 Integer 类型
            Product product = productRepository.findById(productId).orElse(null);
            if (product == null) {
                throw TomatoMallException.productNotExist();
            }
            Stockpile stockpile = stockpileRepository.findByProductId(productId);
            if (stockpile == null) {
                stockpile = new Stockpile();
                stockpile.setAmount(0);
                stockpile.setProduct(product);
                stockpileRepository.save(stockpile);
            }
            return stockpile.toVO();
        } catch (NumberFormatException e) {
            throw TomatoMallException.invalidProductId();  // 处理无效的ID格式
        }
    }

    @Override
    public Product findByTitle(String title){
        return productRepository.findByTitle(title);
    }

    @Override
    public List<Product>findAllByOrderByRateDesc(){
        return productRepository.findAllByOrderByRateDesc();
    }

    @Override
    public Product findById(Integer id){
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void saveProduct(Product product){
        productRepository.save(product);
    }

    //搜索书籍
    @Override
    public List<ProductVO> searchProducts(String keyword) {
        Set<Character> chars = keyword.toLowerCase().chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toSet());

        return productRepository.findAll().stream()
                .filter(product -> {
                    String title = product.getTitle().toLowerCase();
                    return chars.stream().anyMatch(ch -> title.contains(String.valueOf(ch)));
                })
                .map(Product::toVO)
                .collect(Collectors.toList());
    }



    @Override
    public void stockDeleteByProductId(Integer id){
        stockpileRepository.deleteByProductId(id);
    }

    @Override
    public Stockpile stockFindByProductId(Integer productId){
        return stockpileRepository.findByProductId(productId);
    }

    @Override
    public void stockpileSave(Stockpile stockpile){
        stockpileRepository.save(stockpile);
    }


}
