package com.example.tomatomall.service;

import com.example.tomatomall.vo.ColumnsVO;

import java.util.List;

public interface ColumnsService {
    ColumnsVO addColumn(ColumnsVO columnVO);

    String deleteColumn(Integer id);

    String updateColumn(ColumnsVO columnsVO);

    List<ColumnsVO> getAllColumns();
}
