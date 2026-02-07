// import {axios} from '../utils/request'
// import {POSTING_MODULE} from './_prefix';
//
// // 修正1: cover 改为 covers: string[]
// export interface Posting {
//     id: number;
//     userId: number;
//     title: string;
//     content: string;
//     covers: string[];
//     time: string;
//     like: number;
//     dislike: number;
//     productIds?: number[]; // 新增: 详情中可能包含关联的商品ID
// }
//
// // 修正2: postingInfo 中也使用 covers，并新增 productIds
// type postingInfo = {
//     userId: number;
//     title: string;
//     content: string;
//     covers: string[];
//     productIds: number[]; // 新增: 创建时需要提交关联的商品ID
// };
//
// export const createPosting = (postingInfo: postingInfo) => {
//     const token = sessionStorage.getItem('token');
//     return axios.post(`${POSTING_MODULE}/`, postingInfo, {
//         headers: {
//             'Content-Type': 'application/json',
//             'token': token
//         }
//     }).then(res => {
//         return res;
//     });
// };
//
// // --- 以下函数保持您提供的版本不变 ---
//
// export const getAllPosting = () => {
//     console.log("Fetching all postings...");
//     const token = sessionStorage.getItem('token');
//     console.log("token:",token);
//     return axios.get(`${POSTING_MODULE}`,{
//         headers: {
//             'token': token
//         }
//     })
//         .then(res => {
//             console.log("Res data:", res.data);
//             return res;
//         })
//         .catch(error => {
//             console.error("Error fetching products:", error);
//             throw error;
//         });
// };
//
// export const getPostingDetail = (id: number) => {
//     console.log("Fetching postingDetail:", id);
//     const token = sessionStorage.getItem('token');
//     console.log("token:",token);
//     return axios.get(`${POSTING_MODULE}/${id}`, {
//         headers: { 'token': token },
//         params: { id: id }
//     }).then(res => {
//         console.log("Res data:", res.data);
//         return res;
//     })
//         .catch(error => {
//             console.error("Error fetching postingDetail:", error);
//             throw error;
//         })
// }
//
// export const deletePost = (id: number) => {
//     console.log("Deleting post:", id);
//     const token = sessionStorage.getItem('token');
//     console.log("token:",token);
//     return axios.delete(`${POSTING_MODULE}`, {
//         headers: { 'token': token },
//         params: { postId: id }
//     }).then(res => {
//         console.log('Res data:', res.data);
//         return res;
//     })
//         .catch(error => {
//             console.error("Error deleting post:", error);
//             throw error;
//         })
// }
//
// export const likePost = (id: number, userId: number) => {
//     console.log("Liked post:", id);
//     const token = sessionStorage.getItem('token');
//     console.log("token:",token);
//     return axios.post(`${POSTING_MODULE}/like`, null, {
//         headers: { 'token': token, },
//         params: { postId: id, userId: userId }
//     }).then(res => {
//         console.log("Res:", res.data);
//         return res;
//     })
//         .catch(error => {
//             console.error("Error when like post:", error);
//             throw error;
//         })
// }
//
// export const dislikePost = (id: number, userId: number) => {
//     console.log("Disliked post:", id);
//     const token = sessionStorage.getItem('token');
//     console.log("token:",token);
//     return axios.post(`${POSTING_MODULE}/dislike`, null, {
//         headers: { 'token': token, },
//         params: { postId: id, userId: userId }
//     }).then(res => {
//         console.log("Res:", res.data);
//         return res;
//     })
//         .catch(error => {
//             console.error("Error when dislike post:", error);
//             throw error;
//         })
// }

import {axios} from '../utils/request'
import { POSTING_MODULE } from './_prefix';

// Posting 类型
export interface Posting {
    id: number;
    userId: number;
    title: string;
    content: string;
    covers: string[];
    time: string;
    like: number;
    dislike: number;
    productIds?: number[];
}

// 创建帖子类型
export type PostingInfo = {
    userId: number;
    title: string;
    content: string;
    covers: string[];
    productIds: number[];
};

// 创建帖子
export const createPosting = (postingInfo: PostingInfo) => {
    const token = sessionStorage.getItem('token');
    return axios.post(`${POSTING_MODULE}/`, postingInfo, {
        headers: { 'Content-Type': 'application/json', token }
    });
};

// 获取所有帖子
export const getAllPosting = () => {
    const token = sessionStorage.getItem('token');
    return axios.get<Posting[]>(`${POSTING_MODULE}`, {
        headers: { token }
    }).then(res => res.data);
};

// 获取帖子详情
export const getPostingDetail = (id: number) => {
    const token = sessionStorage.getItem('token');
    return axios.get<Posting>(`${POSTING_MODULE}/${id}`, {
        headers: { token }
    }).then(res => res.data);
};

// 删除帖子
export const deletePost = (id: number) => {
    const token = sessionStorage.getItem('token');
    return axios.delete(`${POSTING_MODULE}`, {
        headers: { token },
        params: { postId: id }
    }).then(res => res.data);
};

// // 点赞
// export const likePost = (id: number, userId: number) => {
//     const token = sessionStorage.getItem('token');
//     return axios.post(`${POSTING_MODULE}/like`, { postId: id, userId }, {
//         headers: { token }
//     }).then(res => res.data);
// };
export const likePost = (id: number, userId: number) => {
    const token = sessionStorage.getItem('token');
    return axios.post(`${POSTING_MODULE}/like`, null, {
        headers: { token },
        params: { postId: id, userId }  // 注意这里用 params
    }).then(res => res.data);
};

// 踩
// export const dislikePost = (id: number, userId: number) => {
//     const token = sessionStorage.getItem('token');
//     return axios.post(`${POSTING_MODULE}/dislike`, { postId: id, userId }, {
//         headers: { token }
//     }).then(res => res.data);
// };
export const dislikePost = (id: number, userId: number) => {
    const token = sessionStorage.getItem('token');
    return axios.post(`${POSTING_MODULE}/dislike`, null, {
        headers: { token },
        params: { postId: id, userId }  // 注意这里用 params
    }).then(res => res.data);
};