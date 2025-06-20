package com.example.tomatomall.service.serviceImpl;


import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Columns;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.repository.ColumnsRepository;
import com.example.tomatomall.service.ColumnsService;
import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.vo.ColumnsVO;
import com.example.tomatomall.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ColumnsServiceImpl implements ColumnsService {

    @Autowired
    ColumnsRepository columnsRepository;



    @Override
    public ColumnsVO addColumn(ColumnsVO columnsVO){
        Columns columns=columnsRepository.findByTheme(columnsVO.getTheme());
        if(columns!=null){
            throw TomatoMallException.columnsAlreadyExist();
        }
        Columns newColumns=columnsVO.toPO();
        columnsRepository.save(newColumns);
        return newColumns.toVO();
    }

    @Override
    public String deleteColumn(Integer id){
        Columns columns = columnsRepository.findById(id)
                .orElseThrow(() -> TomatoMallException.columnsNotExist());
        if (!columns.getProducts().isEmpty()) {
            throw TomatoMallException.cannotDelete();
        }
        // 删除栏目

        columnsRepository.delete(columns);
        return "删除成功";

    }

    @Override
    public String updateColumn(ColumnsVO columnsVO){
        Columns columns=columnsRepository.findById(columnsVO.getId()).orElse(null);
        if(columns==null){
            throw TomatoMallException.columnsNotExist();
        }
        columns.setTheme(columnsVO.getTheme());
        columns.setDescription(columnsVO.getDescription());
        columns.setCovers(columnsVO.getCovers());
        columnsRepository.save(columns);
        return "更新成功";
    }

    @Override
    public List<ColumnsVO>getAllColumns(){
        return columnsRepository.findAll().stream().map(Columns::toVO).collect(Collectors.toList());

    }

    @Override
    public Columns findById(Integer id){
        return columnsRepository.findById(id).orElse(null);
    }

    @Override
    public List<ProductVO>getAllProductsInC(Integer id){
        List<Product> products = columnsRepository.findProductsByColumnId(id);
        return products.stream()
                .map(Product::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public ColumnsVO getColumnInfo(Integer id){
        Columns columns=columnsRepository.findById(id).orElse(null);
        if(columns==null){
            throw TomatoMallException.columnsNotExist();
        }
        return columns.toVO();
    }
}
