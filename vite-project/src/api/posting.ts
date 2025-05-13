
//import axios from 'axios';
import {axios} from '../utils/request'
//import {Specification} from './specification.ts'
// 定义帖子接口
export interface Posting {
    id: number;
    userId: number;
    title: string;
    content: string;
    cover: string;
    //time: LocalDateTime;
    time: string;//后端转为字符串
    like: number;
    dislike: number;
}

import {POSTING_MODULE} from './_prefix';

type postingInfo = {

    userId: number;
    title: string;
    content: string;
    cover: string;

};

export const createPosting = (postingInfo: postingInfo) => {
    // 使用反引号来构建 URL
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
    return axios.post(`${POSTING_MODULE}/`, postingInfo, {
        headers: {
            'Content-Type': 'application/json',
            'token': token // 使用 'token' 作为请求头
        }
    }).then(res => {
        console.log("createRes:",res);
        return res;
    });
};

export const getAllPosting = () => {
    console.log("Fetching all postings...");
    const token = sessionStorage.getItem('token');
    console.log("token:",token);
    return axios.get(`${POSTING_MODULE}`,{
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

export const getPostingDetail = (id: number) => {
    console.log("Fetching postingDetail:", id);
    const token = sessionStorage.getItem('token');
    console.log("token:",token);
    return axios.get(`${POSTING_MODULE}/${id}`, {
        headers: {
            'token': token
        },
        params: {
            id: id
        }
    }).then(res => {
        console.log("Res data:", res.data);
        return res;
    })
    .catch(error => {
        console.error("Error fetching postingDetail:", error);
        throw error;
    })
}

export const deletePost = (id: number) => {
    console.log("Deleting post:", id);
    const token = sessionStorage.getItem('token');
    console.log("token:",token);
    return axios.delete(`${POSTING_MODULE}`, {
        headers: {
            'token': token
        },
        params: {
            postId: id
        }
    }).then(res => {
            console.log('Res data:', res.data);
            return res;
        })
    .catch(error => {
        console.error("Error deleting post:", error);
        throw error;
    })
}

export const likePost = (id: number) => {
    console.log("Liked post:", id);
    const token = sessionStorage.getItem('token');
    console.log("token:",token);

    return axios.post(`${POSTING_MODULE}/like`, null, {
        headers: {
            'token': token,
        },
        params: {
            postId: id
        }
    }).then(res => {
            console.log("Res:", res.data);
            return res;
        })
    .catch(error => {
        console.error("Error when like post:", error);
        throw error;
    })
}

export const dislikePost = (id: number) => {
    console.log("Disliked post:", id);
    const token = sessionStorage.getItem('token');
    console.log("token:",token);
    return axios.post(`${POSTING_MODULE}/dislike`, null, {
        headers: {
            'token': token,
        },
        params: {
            postId: id
        }
    }).then(res => {
        console.log("Res:", res.data);
        return res;
    })
        .catch(error => {
            console.error("Error when dislike post:", error);
            throw error;
        })
}

export const getProduct = (productId: string) => {
    const token = sessionStorage.getItem('token'); // 从 sessionStorage 获取 token
    return axios.get(`${POSTING_MODULE}/${productId}`,  {
        headers: {
            'token': token // 使用 'token' 作为请求头
        }
    }).then(res => {
        console.log("ProductsThe",res.data);
        return res;
    });

};


export const deleteTheProduct = (productId: string) => {
    const token = sessionStorage.getItem('token');
    return axios.delete(`${POSTING_MODULE}/${productId}`, {
        params: { productId },
        headers: {
            'token': token
        }
    }).then(res => {
        console.log("Deleted product:", res.data);
        return res;
    })
}
