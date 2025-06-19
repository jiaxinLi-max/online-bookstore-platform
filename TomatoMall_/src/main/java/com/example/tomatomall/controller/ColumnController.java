package com.example.tomatomall.controller;


import com.example.tomatomall.po.Product;
import com.example.tomatomall.service.ColumnsService;
import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.vo.ColumnsVO;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/columns")
public class ColumnController {

    @Autowired
    ColumnsService columnService;

    @Autowired
    ProductService productService;

    @PostMapping
    public Response<ColumnsVO> addColumn(@RequestBody ColumnsVO columnVO) {
        return Response.buildSuccess(columnService.addColumn(columnVO));
    }

    @DeleteMapping("/{id}")
    public Response<String> deleteColumn(@RequestParam Integer id){
        return Response.buildSuccess(columnService.deleteColumn(id));
    }

    @PutMapping()
    public Response<String>updateColumn(@RequestBody ColumnsVO columnVO){
        return Response.buildSuccess(columnService.updateColumn(columnVO));
    }

    @GetMapping
    public Response<List<ColumnsVO>>getAllColumns(){
        return Response.buildSuccess(columnService.getAllColumns());
    }

    @GetMapping("/{id}")
    public Response<List<ProductVO>>getAllProductsInC(@PathVariable Integer id){
        return Response.buildSuccess(columnService.getAllProductsInC(id));
    }

}
