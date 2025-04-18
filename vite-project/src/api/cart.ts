import axios,{ AxiosResponse }  from 'axios';
import {CART_MODULE, ORDER_MODULE} from './_prefix'

// 定义 API 响应的接口
interface ApiResponse<T> {
    code: string;
    data: T;
    msg: string | null;
}
// 定义购物车商品结构
export interface Cart {
    cartItemId: number;
    productId: number;
    title: string;
    price: number;
    description: string;
    cover: string;
    details: string;
    quantity: number;
}

export interface shippingAddress {
    name: string,
    telephone: string,
    address: string,
}

type form = {
    cartItemIds: number[];
    shipping_address: shippingAddress;
    payment_method: string;
}

const paymentMethod = 'ALIPAY'

export const addCart = (userId: number, productId: number, quantity: number) => {
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token

    // 构建请求的 URL，添加查询参数
    const url = `${CART_MODULE}?userId=${userId}&productId=${productId}&quantity=${quantity}`;

    return axios.post(url, null, {
        headers: {
            'Content-Type': 'application/json',
            'token': token // 使用 'token' 作为请求头
        }
    }).then(res => {
        console.log("createRes:", res);
        return res; // 返回响应数据
    }).catch(error => {
        console.error("添加商品到购物车失败:", error);
        throw error; // 抛出错误以便后续处理
    });
};

// 删除购物车商品


// export const removeFromCart = (userId: number, cartItemId: number) => {
//     const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
//
//     // 构建请求的 URL
//     const url = `${CART_MODULE}/${cartItemId}`; // 只需 cartItemId 作为路径参数
//
//     return axios.delete(url, {
//         headers: {
//             'Content-Type': 'application/json',
//             'token': token, // 使用 'token' 作为请求头
//         },
//         params: {
//             userId: userId // 将 userId 作为查询参数传递
//         }
//     }).then(res => {
//         console.log("createRes:", res);
//         return res; // 返回响应数据
//     }).catch(error => {
//         console.error("删除商品失败:", error);
//         throw error; // 抛出错误以便后续处理
//     });
// };
export const removeItemFromCart = (userId: number, cartItemId: number) => {
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token

    // 构建请求的 URL
    const url = `${CART_MODULE}/${cartItemId}`; // 只需 cartItemId 作为路径参数

    return axios.delete(url, {
        headers: {
            'Content-Type': 'application/json',
            'token': token, // 使用 'token' 作为请求头
        },
        params: {
            userId: userId // 将 userId 作为查询参数传递
        }
    }).then(res => {
        console.log("Response:", res);
        return res; // 返回响应数据，通常是 res.data??注意这里
    }).catch(error => {
        console.error("删除商品失败:", error);
        throw error; // 抛出错误以便后续处理
    });
};

// 修改购物车商品数量
// export const updateCartItemQuantity = (userId: number, cartItemId: number, quantity: number) => {
//     const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
//
//     // 构建请求的 URL
//     const url = `${CART_MODULE}/${cartItemId}/quantity`; // 使用 cartItemId 作为路径参数
//
//     return axios.patch(url, {
//         quantity: quantity // 使用 JSON 格式的请求体传递 quantity
//     }, {
//         headers: {
//             'Content-Type': 'application/json',
//             'token': token, // 使用 'token' 作为请求头
//         },
//         params: {
//             userId: userId // 将 userId 作为查询参数传递
//         }
//     }).then(res => {
//         console.log("更新数量响应:", res);
//         return res; // 返回响应数据
//     }).catch(error => {
//         console.error("修改数量失败:", error);
//         throw error; // 抛出错误以便后续处理
//     });
// };

//不是json
export const updateCartItemQuantity = (userId: number, cartItemId: number, quantity: number) => {
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token

    // 构建请求的 URL
    const url = `${CART_MODULE}/${cartItemId}/quantity`; // 使用 cartItemId 作为路径参数

    return axios.patch(url, null, { // 不需要请求体，因此传递 null
        headers: {
            'Content-Type': 'application/json',
            'token': token, // 使用 'token' 作为请求头
        },
        params: {
            userId: userId, // 将 userId 作为查询参数传递
            quantity: quantity // 将 quantity 作为查询参数传递
        }
    }).then(res => {
        console.log("更新数量响应:", res);
        return res; // 返回响应数据
    }).catch(error => {
        console.error("修改数量失败:", error);
        throw error; // 抛出错误以便后续处理
    });
};


export const getCartItems = (userId: number) => {
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token

    // 构建请求的 URL
    const url = `${CART_MODULE}/`; // 只需 cartItemId 作为路径参数

    return axios.get(url, {
        headers: {
            'token': token, // 使用 'token' 作为请求头
        },
        params: {
            userId: userId // 将 userId 作为查询参数传递
        }
    }).then(res => {
        console.log("Response:", res);
        return res; // 返回响应数据
    }).catch(error => {
        console.error("从购物车获取失败:", error);
        throw error; // 抛出错误以便后续处理
    });
};
// export const addCart = (userId: number, productId: number, quantity: number) => {
//     // 使用反引号来构建 URL
//     const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
//
//     // 构建请求体
//     const requestBody = {
//         userId,
//         productId,
//         quantity
//     };
//
//     return axios.post(`${CART_MODULE}`, requestBody, {
//         headers: {
//             'Content-Type': 'application/json',
//             'token': token // 使用 'token' 作为请求头
//         }
//     }).then(res => {
//         console.log("createRes:", res);
//         return res.data; // 返回响应数据
//     }).catch(error => {
//         console.error("添加商品到购物车失败:", error);
//         throw error; // 抛出错误以便后续处理
//     });
// };
export const cartService = {
    // 添加商品到购物车
    // async addToCart(userId: number, productId: number, quantity: number): Promise<Response<boolean>> {
    //     return await axios.post(`/api/cart`, { userId, productId, quantity });
    // },

    // 删除购物车商品
    // async removeFromCart(userId: number, cartItemId: number): Promise<Response<boolean>> {
    //     return await axios.delete(`/api/cart/${cartItemId}`, { params: { userId } });
    // },
    //
    // // 修改购物车商品数量
    // async updateCartItemQuantity(userId: number, cartItemId: number, quantity: number): Promise<Response<boolean>> {
    //     return await axios.patch(`/api/cart/${cartItemId}/quantity`, { userId, quantity });
    // },
    //
    // // 获取购物车商品列表
    // async getCartItems(userId: number): Promise<Response<CartItem[]>> {
    //     return await axios.get(`/api/cart/`, { params: { userId } });
    // },
};

export const placeOrder = (Form: form) => {
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token

    // 构建请求的 URL
    const url = `${CART_MODULE}/checkout`; // 只需 cartItemId 作为路径参数

    return axios.post(url, Form,{
        headers: {
            'token': token, // 使用 'token' 作为请求头
            'Content-Type': 'application/json',
        },
    }).then(res => {
        console.log("发起订单成功, Response:", res);
        return res; // 返回响应数据
    }).catch(error => {
        console.error("发起订单失败", error);
        throw error; // 抛出错误以便后续处理
    });
}

export const postOrder = (orderId: number) => {
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token

    // 构建请求的 URL
    const url = `${ORDER_MODULE}/${orderId}/pay`; // 只需 cartItemId 作为路径参数

    return axios.post(url, { orderId },{
        headers: {
            'token': token, // 使用 'token' 作为请求头
        },
        // params: {
        //     orderId: order,
        // }
    }).then(res => {
        console.log("发起支付成功, Response:", res);
        return res; // 返回响应数据
    }).catch(error => {
        console.error("发起支付失败:", error);
        throw error; // 抛出错误以便后续处理
    });
}