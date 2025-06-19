package com.example.tomatomall.exception;

/**
 * 定义Exception
 */
public class TomatoMallException extends RuntimeException {
  private final String code;

  public TomatoMallException(String message, String code) {
    super(message);
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public static TomatoMallException productNotExist(){return new TomatoMallException("商品不存在","400");}

  public static TomatoMallException titleAlreadyExist(){return new TomatoMallException("该商品名已经存在","000");}

  public static TomatoMallException usernameAlreadyExists(){return new TomatoMallException("用户名已存在","000");}

  public static TomatoMallException usernameOrPasswordError(){return new TomatoMallException("用户名或密码错误","400");}

  public static TomatoMallException userNotExist(){return new TomatoMallException("用户不存在","000");}

  public static TomatoMallException fileUploadFail(){return new TomatoMallException("文件上传错误","000");}

  public static TomatoMallException notLogin(){return new TomatoMallException("未登录","401");}

  public static TomatoMallException exceedAmount(){return new TomatoMallException("超出商品可卖库存数","000");}

  public static TomatoMallException productNotExistInCart(){return new TomatoMallException("购物车中不存在该商品","000");}

  public static TomatoMallException noPermissionToDelete(){return new TomatoMallException("无权限删除该商品","000");}

  public static TomatoMallException orderNotExist(){return new TomatoMallException("订单不存在","000");}

  public static TomatoMallException alipayFailure(){return new TomatoMallException("支付宝签名认证失败","000");}

  public static TomatoMallException invalidProductId(){return new TomatoMallException("无效的商品id","400");}

  public static TomatoMallException advertisementNotExist(){return new TomatoMallException("广告不存在","400");}

  public static TomatoMallException commentNotExist(){return new TomatoMallException("评论不存在","400");}

  public static TomatoMallException postNotExist(){return new TomatoMallException("帖子不存在","400");}

  public static TomatoMallException checkinExist(){return new TomatoMallException("今日已签到","000");}

  public static TomatoMallException columnsAlreadyExist(){return new TomatoMallException("栏目名已经存在","000");}

  public static TomatoMallException columnsNotExist(){return new TomatoMallException("栏目不存在","400");}

  public static TomatoMallException AlreadyAnswer(){return new TomatoMallException("已回答过该问题","000");}

  public static TomatoMallException questionNotExist(){return new TomatoMallException("不存在该问题","000");}

  public static TomatoMallException parentCommentNotExist(){return new TomatoMallException("父评论不存在","400");}
}
