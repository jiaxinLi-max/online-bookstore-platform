package com.example.tomatomall.exception;

/**
 * 定义Exception
 */
public class TomatoMallException extends RuntimeException {
  public TomatoMallException(String message) {
    super(message);
  }

  public static TomatoMallException productNotExist(){return new TomatoMallException("商品不存在");}

  public static TomatoMallException titleAlreadyExist(){return new TomatoMallException("该商品名已经存在");}


}
