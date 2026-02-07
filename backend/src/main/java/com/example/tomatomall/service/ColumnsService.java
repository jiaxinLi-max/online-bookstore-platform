package com.example.tomatomall.service;

import com.example.tomatomall.po.Columns;
import com.example.tomatomall.vo.ColumnsVO;
import com.example.tomatomall.vo.ProductVO;

import java.util.List;

public interface ColumnsService {
    ColumnsVO addColumn(ColumnsVO columnVO);

    String deleteColumn(Integer id);

    String updateColumn(ColumnsVO columnsVO);

    List<ColumnsVO> getAllColumns();

    Columns findById(Integer id);

    List<ProductVO> getAllProductsInC(Integer id);


    ColumnsVO getColumnInfo(Integer id);
}
