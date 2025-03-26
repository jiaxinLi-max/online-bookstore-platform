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

  public static TomatoMallException usernameAlreadyExists(){return new TomatoMallException("用户名已存在");}

  public static TomatoMallException usernameOrPasswordError(){return new TomatoMallException("用户名或密码错误");}
  public static TomatoMallException userNotExist(){return new TomatoMallException("用户不存在");}
  public static TomatoMallException fileUploadFail(){return new TomatoMallException("文件上传错误");}
  public static TomatoMallException notLogin(){return new TomatoMallException("未登录");}
}
