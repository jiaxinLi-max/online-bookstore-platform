import axios from 'axios';

// 定义响应结构
interface Response<T> {
    code: number;
    data: T;
    msg: string | null;
}

// 定义购物车商品结构
export interface CartItem {
    cartItemId: number;
    productId: number;
    quantity: number;
    title: string;
    price: number;
}

// 定义购物车接口
export const cartService = {
    // 添加商品到购物车
    async addToCart(userId: number, productId: number, quantity: number): Promise<Response<boolean>> {
        return await axios.post(`/api/cart`, { userId, productId, quantity });
    },

    // 删除购物车商品
    async removeFromCart(userId: number, cartItemId: number): Promise<Response<boolean>> {
        return await axios.delete(`/api/cart/${cartItemId}`, { params: { userId } });
    },

    // 修改购物车商品数量
    async updateCartItemQuantity(userId: number, cartItemId: number, quantity: number): Promise<Response<boolean>> {
        return await axios.patch(`/api/cart/${cartItemId}/quantity`, { userId, quantity });
    },

    // 获取购物车商品列表
    async getCartItems(userId: number): Promise<Response<CartItem[]>> {
        return await axios.get(`/api/cart/`, { params: { userId } });
    },
};
