//import axios from 'axios';
import { STORE_MODULE } from './_prefix';
import {axios} from '../utils/request'
type storeInfo = {
    name: string;
    location: string;
    logoUrl: string;
};

// 创建商店
export const createStore = (storeInfo: storeInfo) => {
    // 使用反引号来构建 URL
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
    return axios.post(`${STORE_MODULE}/`, storeInfo, {
        headers: {
                'Content-Type': 'application/json',
                'token': token // 使用 'token' 作为请求头
            }
    }).then(res => {
        return res;
    });
};

// export const createStore = async (storeInfo:storeInfo) => {
//     const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
//
//     try {
//         const response = await axios.post(`${STORE_MODULE}/`, storeInfo, {
//             headers: {
//                 'Content-Type': 'application/json',
//                 'token': token // 使用 'token' 作为请求头
//             }
//         });
//
//         // 检查 response.data 是否存在
//         if (!response || !response.data) {
//             throw new Error('未获取到有效的响应数据');
//         }
//
//         return response.data; // 返回响应数据
//     } catch (error) {
//         console.error('创建商店请求失败:', error);
//         // 重新抛出错误，以便调用处可以捕获
//         throw error;
//     }
// };


// export const createStore = async (storeInfo: storeInfo) => {
//     const token = localStorage.getItem('token'); // 从 localStorage 获取 token
//
//     const response = await fetch('http://localhost:8080/api/stores/hello', { // 更新为新 URL
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//             'Authorization': `Bearer ${token}`, // 如果需要 token
//         },
//         body: JSON.stringify(storeInfo), // 将 storeInfo 转换为 JSON 字符串
//     });
//
//     if (!response.ok) {
//         throw new Error('创建商店请求失败');
//     }
//
//     return response.json();
// };

// 获取指定商店
// export const getStore = async (storeId: number): Promise<Store> => {
//     try {
//         const response = await axios.get(`${STORE_MODULE}/${storeId}`);
//         return response; // 直接返回 result 中的数据
//     } catch (error) {
//         console.error('获取商店信息失败:', error);
//         throw error; // 如果需要，可以在这里处理错误
//     }
// };
export const getStore = (storeId: number) => {
    // 使用反引号来构建 URL
    return axios.get(`${STORE_MODULE}/${storeId}`, )
        .then(res => {console.log("resThe",res.data);
            return res;
        });
};
// 获取所有商店
export const getAllStore = () => {
    // 使用反引号来构建 URL
    return axios.get(`${STORE_MODULE}/all`, )
        .then(res => {console.log("res111",res.data);
        return res;
    });
};
// ../../api/store.ts
export interface Store {
    id: number;
    name: string;
    location: string;
    phone: string;
    description: string;
    logoUrl: string;
}

// // 模拟商店数据
// const stores: Store[] = [
//     { id: 1, name: '商店1', address: '地址1', phone: '123456789', description: '描述1' },
//     { id: 2, name: '商店2', address: '地址2', phone: '987654321', description: '描述2' },
// ];

// // 获取商店详情的函数
// export async function getStoreById(id: number): Promise<Store | null> {
//     return new Promise((resolve) => {
//         const store = stores.find(store => store.id === id);
//         setTimeout(() => {
//             resolve(store || null);
//         }, 1000); // 模拟异步请求
//     });
// }
