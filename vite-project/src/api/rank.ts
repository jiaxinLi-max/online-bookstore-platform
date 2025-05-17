// rank.ts
import { axios } from '../utils/request'
import { Product } from './product'
import { RANK_MODULE } from './_prefix'  // 一般为 '/rank'

// 获取评分最高的产品
export const getTopRatedProducts = (): Promise<{ data: Product[] }> => {
    const token = sessionStorage.getItem('token');
    return axios.get(`${RANK_MODULE}/top-rated`, {
        headers: {
            'token': token
        }
    });
};


