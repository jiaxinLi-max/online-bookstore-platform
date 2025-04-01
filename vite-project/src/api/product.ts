// product.ts
//import axios from 'axios';
//import axios from 'axios';
import {axios} from '../utils/request'
//import {Specification} from './specification.ts'
// 定义商品接口
export interface Product {
    id: number;
    title: string;
    price: number;
    rate:number;
    description: string;
    cover: string;
    detail: string;
    //specifications:Specification[];
    bookTitle: string,
    subtitle: string,
    author: string,
    isbn: string,
    binding: string,
    pages: number,
    publicationDate: string,
    publisher: string,

}

import {PRODUCT_MODULE} from './_prefix';

type productInfo = {
    title: string;
    price: number;
    rate:number;
    description: string;
    cover: string;
    detail: string;
    //specifications:Specification[];
    bookTitle: string,
    subtitle: string,
    author: string,
    isbn: string,
    binding: string,
    pages: number,
    publicationDate: string,
    publisher: string,
};


export const createProduct = (productInfo: productInfo) => {
    // 使用反引号来构建 URL
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
    return axios.post(`${PRODUCT_MODULE}/`, productInfo, {
        headers: {
            'Content-Type': 'application/json',
            'token': token // 使用 'token' 作为请求头
        }
    }).then(res => {
        console.log("createRes:",res);
        return res;
    });
};
// 获取所有商店
// export const getAllProduct = () => {
//     // 使用反引号来构建 URL
//     console.log("res111");
//     return axios.get(`${PRODUCT_MODULE}`, )
//         .then(res => {
//             console.log("resAllProduct",res.data);
//             return res;
//         });
// };
export const getAllProduct = () => {
    console.log("Fetching all products...");
    const token = sessionStorage.getItem('token');
    console.log("token:",token);
    return axios.get(`${PRODUCT_MODULE}`,{
        headers: {
            'token': token // 使用 'token' 作为请求头
        }
    })
        .then(res => {
            console.log("Res data:", res.data);
            return res;
        })
        .catch(error => {
            console.error("Error fetching products:", error); // 增加错误捕获
            throw error; // 将错误抛出以便其他处理
        });
};

// export const getAllProduct = async () => {
//     try {
//         const response = await axios.get('http://localhost:8080/api/products');
//         console.log("resAllProduct",response);
//         return response.data; // 返回产品数据
//     } catch (error) {
//         console.error('Error fetching products:', error);
//         throw error; // 重新抛出错误以便上层处理
//     }
// };

export const getProduct = (productId: number) => {
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
    return axios.get(`${PRODUCT_MODULE}/${productId}`,  {
        headers: {
            'token': token // 使用 'token' 作为请求头
        }
    }).then(res => {
        console.log("ProductsThe",res.data);
        return res;
    });

};