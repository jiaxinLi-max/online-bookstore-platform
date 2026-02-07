import axios from 'axios';
import {ADVERTISEMENT_MODULE} from "./_prefix.ts";
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

type updateInfo = {
    id: string;
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

export const deleteAdvertisement = (id: number) => {
    const token = sessionStorage.getItem('token');

    return axios.delete(`${ADVERTISEMENT_MODULE}/${id}`, {
        headers: { 'token': token },
        params: { id }
    }) .then(res => {
        console.log("deleteAdvertisement, res:", res.data);
        return res;
    })
}

export const updateAdvertisement = (newInfo: updateInfo) => {
    const token = sessionStorage.getItem('token');

    return axios.put(`${ADVERTISEMENT_MODULE}`, newInfo, {
        headers: { 'token': token},
        // params: {
        //     id: newInfo.id,
        //     title: newInfo.title,
        //     content: newInfo.content,
        //     imgUrl: newInfo.imgUrl,
        //     productId: newInfo.productId,
        // }
    }) .then (res => {
        console.log("updateAdvertisement:", res.data);
        return res;
    })
}