import axios from 'axios';
import {ADVERTISEMENT_MODULE, PRODUCT_MODULE} from "./_prefix.ts";
import {Specification} from "./product.ts";
export interface Advertisement {
    id: number;
    title: string;
    content: string;
    imageUrl: string;
    productId: number;
}
type advertisementInfo = {
    title: string;
    content: string;
    imgUrl: string;
    productId: string;

};
export const createAdvertisement = (advertisementInfo: advertisementInfo) => {
    // 使用反引号来构建 URL
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
    return axios.post(`${ADVERTISEMENT_MODULE}/`, advertisementInfo, {
        headers: {
            'Content-Type': 'application/json',
            'token': token // 使用 'token' 作为请求头
        }
    }).then(res => {
        console.log("createRes:",res);
        return res;
    });
};

export const getAllAdvertisement = () => {
    console.log("Fetching all advertisements...");
    const token = sessionStorage.getItem('token');
    console.log("token:",token);
    return axios.get(`${ADVERTISEMENT_MODULE}`,{
        headers: {
            'token': token // 使用 'token' 作为请求头
        }
    })
        .then(res => {
            console.log("Res data:", res.data);
            return res;
        })
        .catch(error => {
            console.error("Error fetching advertisements:", error); // 增加错误捕获
            throw error; // 将错误抛出以便其他处理
        });
};