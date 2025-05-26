//import axios from 'axios';
import {axios} from '../utils/request'
import {PRODUCT_MODULE} from "./_prefix.ts";

export const getImage = async (file: File) => {
    const formData = new FormData();
    formData.append('file', file); // 将文件添加到 FormData

    try {
        const response = await axios.post('api/images', formData, {
            headers: {
                'Content-Type': 'multipart/form-data', // 确保设置正确的 Content-Type
            },
        });
        console.log("response", response);
        return response.data; // 根据 API 的返回格式调整
    } catch (error) {
        console.error('获取用户头像 失败:', error);
        throw error; // 重新抛出错误
    }
};

// 类型定义：post帖子 或 comment评价
type SortType = 'post' | 'comment'

// 按时间排序
export const sortByTime = (type: SortType, productId?: number) => {
    const token = sessionStorage.getItem('token');
    return axios.get('/api/sortByTime', {
        headers: {
            token
        },
        params: {
            type,
            ...(productId !== undefined ? { productId } : {})
        }
    });
}

// 按点赞排序
export const sortByLike = (type: SortType, productId?: number) => {
    const token = sessionStorage.getItem('token');
    return axios.get('/api/sortByLike', {
        headers: {
            token
        },
        params: {
            type,
            ...(productId !== undefined ? { productId } : {})
        }
    });
}