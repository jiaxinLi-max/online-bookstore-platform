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
    specifications:Specification[];
    // bookTitle: string,
    // subtitle: string,
    // author: string,
    // isbn: string,
    // binding: string,
    // pages: number,
    // publicationDate: string,
    // publisher: string,

}

import {PRODUCT_MODULE} from './_prefix';
export interface Specification {
    id: string; // 规格的唯一标识符
    item: string; // 规格的名称（例如 "作者" 或 "副标题"）
    value: string; // 规格的值（例如 "Robert C. Martin" 或 "程序员的职业素养"）
    productId: string; // 关联的产品 ID
}
type productInfo = {
    title: string;
    price: number;
    rate:number;
    description: string;
    cover: string;
    detail: string;
    specifications:Specification[];

    // bookTitle: string,
    // subtitle: string,
    // author: string,
    // isbn: string,
    // binding: string,
    // pages: number,
    // publicationDate: string,
    // publisher: string,
    // amount: number,
    // frozen: number,
};

type UpdateInfo = {
    id: string,
    title: string,
    price: number,
    rate:number,
    description?: string,
    cover?: string,
    detail?: string,
    specifications?:Specification[],
}

type StockpileInfo = {
    productId: string,
    amount: number,
}


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

export const updateProductInfo = (updateInfo: UpdateInfo) => {
    const token = sessionStorage.getItem('token');
    return axios.put(`${PRODUCT_MODULE}`, updateInfo, {
        headers: {
            'Content-Type': 'application/json',
            'token': token
        }
    }).then(res => {
        console.log("Updated product:", res.data);
        return res;
    })
}

export const deleteTheProduct = (productId: string) => {
    const token = sessionStorage.getItem('token');
    return axios.delete(`${PRODUCT_MODULE}/${productId}`, {
        params: { productId },
        headers: {
            'token': token
        }
    }).then(res => {
        console.log("Deleted product:", res.data);
        return res;
    })
}

export const updateStockpile = (stockPileInfo: StockpileInfo) => {
    const token = sessionStorage.getItem('token');
    return axios.patch(`${PRODUCT_MODULE}/stockpile/${stockPileInfo.productId}`, stockPileInfo, {
        headers: {
            'token': token
        }
    }).then(res => {
        console.log("Updated stockpile:", res.data);
        return res;
    })
}

export const getStockpile = (productId: string) => {
    const token = sessionStorage.getItem('token');
    return axios.get(`${PRODUCT_MODULE}/stockpile/${productId}`, {
        params: { productId },
        headers: {
            'token': token
        }
    }).then(res => {
        console.log("GetStockpile:", res.data);
        return res;
    })
}