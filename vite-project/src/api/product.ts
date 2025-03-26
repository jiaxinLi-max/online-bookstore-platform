// product.ts
import axios from 'axios';
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
export const getAllProduct = () => {
    // 使用反引号来构建 URL
    return axios.get(`${PRODUCT_MODULE}/all`, )
        .then(res => {console.log("res111",res.data);
            return res;
        });
};

export const getProductsByStoreId = (storeId: number) => {
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
    return axios.get(`${PRODUCT_MODULE}/store/${storeId}`,  {
        headers: {
            'token': token // 使用 'token' 作为请求头
        }
    }).then(res => {
        console.log("ProductsThe",res.data);
        return res;
    });

};