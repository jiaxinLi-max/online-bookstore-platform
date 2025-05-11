import axios from 'axios';
import {COMMENT_MODULE, PRODUCT_MODULE} from "./_prefix.ts";
export interface Comment {
    id: number;
    productId: number;
    userId: number;
    content: string;
    score:number;//float?
    likes:number;
    time: string;
}

type commentInfo = {
    productId: number;
    userId: number;
    content: string;
    score:number;//float?
};



export const createComment = (commentInfo: commentInfo) => {
    // 使用反引号来构建 URL
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
    return axios.post(`${COMMENT_MODULE}/`, commentInfo, {
        headers: {
            'Content-Type': 'application/json',
            'token': token // 使用 'token' 作为请求头
        }
    }).then(res => {
        console.log("createRes:",res);
        return res;
    });
};
export const getTheAllComment = (productId: string) => {
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
    return axios.get(`${COMMENT_MODULE}/${productId}`,  {
        headers: {
            'token': token // 使用 'token' 作为请求头
        }
    }).then(res => {
        console.log("ProductsThe",res.data);
        return res;
    });

};
export const getComment = (commentId: string) => {
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
    return axios.get(`${COMMENT_MODULE}/detail/${commentId}`,  {
        headers: {
            'token': token // 使用 'token' 作为请求头
        }
    }).then(res => {
        console.log("CommentThe",res.data);
        return res;
    });

};
