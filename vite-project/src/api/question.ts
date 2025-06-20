// src/api/question.ts
import { axios } from '../utils/request';

export interface Question {
    id: number;
    title: string;
    content: string;
    createdAt?: string;
    updatedAt?: string;
}

const QUESTION_MODULE = '/api/question';

const getTokenHeader = () => ({
    headers: {
        token: sessionStorage.getItem('token') || ''
    }
});

// 获取今日问题（用户用）
export const getTodayQuestion = () => {
    return axios.get(`${QUESTION_MODULE}/today`, getTokenHeader());
};

// 获取单个问题（用于编辑）
export const getQuestionById = (id: number) => {
    return axios.get(`${QUESTION_MODULE}/${id}`, getTokenHeader());
};

// 创建问题（管理员用）
export const createQuestion = (question: { title: string; content: string }) => {
    return axios.post(`${QUESTION_MODULE}`, question, {
        headers: {
            'Content-Type': 'application/json',
            token: sessionStorage.getItem('token') || ''
        }
    });
};

// 更新问题（管理员用）
export const updateQuestion = (question: Question) => {
    return axios.put(`${QUESTION_MODULE}`, question, {
        headers: {
            'Content-Type': 'application/json',
            token: sessionStorage.getItem('token') || ''
        }
    });
};

// 删除问题（管理员用）
export const deleteQuestion = (id: number) => {
    return axios.delete(`${QUESTION_MODULE}/${id}`, {
        ...getTokenHeader(),
        params: { id }
    });
};
