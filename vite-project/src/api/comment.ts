import axios from 'axios';
import {COMMENT_MODULE} from "./_prefix.ts";
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

type updateInfo = {
    id: number;
    content: string;
    score: number;
}



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

export const updateComment = (updateInfo: updateInfo) => {
    console.log("UpdateInfo:", updateInfo);
    const token = sessionStorage.getItem('token');
    return axios.put(`${COMMENT_MODULE}/${updateInfo.id}`, updateInfo, {
        headers: {
            'Content-Type': 'application/json',
            'token': token
        }
    })
        .then(res => {
            console.log("Update res:", res.data);
            return res;
        })
        .catch(error => {
            console.error("Update comment error:", error);
            throw error;
        })
}

export const deleteComment = (commentId: number) => {
    const token = sessionStorage.getItem('token');
    return axios.delete(`${COMMENT_MODULE}/${commentId}`, {
        headers: {
            'token': token
        },
        params: {
            id: commentId
        }
    })
        .then(res => {
            console.log("delete res:", res.data);
            return res;
        })
        .catch(error => {
            console.error("Delete comment error:", error);
            throw error;
        })
}

export const likeComment = (commentId: number, userId: number) => {
    const token = sessionStorage.getItem('token');
    return axios.post(`${COMMENT_MODULE}/like/${commentId}`, null, {
        headers: {
            'token': token
        },
        params: {
            id: commentId,
            userId: userId
        }
    })
}
