// // src/api/question.ts
// import { axios } from '../utils/request';
//
// export interface Question {
//     id: number;
//     title: string;
//     content: string;
//     createdAt?: string;
//     updatedAt?: string;
// }
//
// const QUESTION_MODULE = '/api/question';
//
// const getTokenHeader = () => ({
//     headers: {
//         token: sessionStorage.getItem('token') || ''
//     }
// });
//
// // 获取今日问题（用户用）
// export const getTodayQuestion = () => {
//     return axios.get(`${QUESTION_MODULE}/today`, getTokenHeader());
// };
//
// // 获取单个问题（用于编辑）
// export const getQuestionById = (id: number) => {
//     return axios.get(`${QUESTION_MODULE}/${id}`, getTokenHeader());
// };
//
// // 创建问题（管理员用）
// export const createQuestion = (question: { title: string; content: string }) => {
//     return axios.post(`${QUESTION_MODULE}`, question, {
//         headers: {
//             'Content-Type': 'application/json',
//             token: sessionStorage.getItem('token') || ''
//         }
//     });
// };
//
// // 更新问题（管理员用）
// export const updateQuestion = (question: Question) => {
//     return axios.put(`${QUESTION_MODULE}`, question, {
//         headers: {
//             'Content-Type': 'application/json',
//             token: sessionStorage.getItem('token') || ''
//         }
//     });
// };
//
// // 删除问题（管理员用）
// export const deleteQuestion = (id: number) => {
//     return axios.delete(`${QUESTION_MODULE}/${id}`, {
//         ...getTokenHeader(),
//         params: { id }
//     });
// };
import { axios } from '../utils/request'
import { QUESTION_MODULE } from './_prefix'

// 问题实体类型（后端返回用）
export interface Question {
    id: number;
    content: string;
    createTime: string; // LocalDateTime 格式字符串
    ddl: string;
    answer: string;
    options: string[];
    analysis?: string;
}

// 创建问题时用的表单类型
export type QuestionForm = {
    content: string;
    options: string[];
    answer: string;
    ddl: string;
    analysis?: string;
};

// 用户回答记录类型
export type QuestionUserAnswer = {
    id: number;
    question_id: number;
    user_id: number;
    answer_time: string;
    answer: string;
};

/**
 * 1. 创建问题（管理员）
 * POST /api/question/create
 */
export const createQuestion = (questionInfo: QuestionForm) => {
    const token = sessionStorage.getItem('token');
    return axios.post(`${QUESTION_MODULE}/create`, questionInfo, {
        headers: {
            'Content-Type': 'application/json',
            'token': token
        }
    }).then(res => res);
};

/**
 * 2. 提交回答
 * POST /api/question/answer
 */
export const submitAnswera = (data: { userId: number; questionId: number; answer: string }) => {
    const token = sessionStorage.getItem('token');
    return axios.post(`${QUESTION_MODULE}/question`, data, {
        headers: {
            'Content-Type': 'application/json',
            'token': token
        }
    }).then(res => {
        console.log("Res data:", res.data);
        return res;
    });
};


/**
 * 3. 获取所有问题列表
 * GET /api/question/list
 */
export const getAllQuestions = () => {
    return axios.get(`${QUESTION_MODULE}/list`)
        .then(res => res);
};

/**
 * 4. 获取问题详情
 * GET /api/question/detail/{questionId}
 */
export const getQuestionDetail = (questionId: number) => {
    return axios.get(`${QUESTION_MODULE}/detail/${questionId}`)
        .then(res => res);
};

/**
 * 5. 获取用户已回答问题列表
 * GET /api/question/answered/{userId}
 */
export const getAnsweredQuestions = (userId: number) => {
    return axios.get(`${QUESTION_MODULE}/answered/${userId}`)
        .then(res => res);
};

/**
 * 6. 获取用户未回答问题列表
 * GET /api/question/unanswered/{userId}
 */
export const getUnansweredQuestions = (userId: number) => {
    return axios.get(`${QUESTION_MODULE}/unanswered/${userId}`)
        .then(res => res);
};

/**
 * 7. 获取用户某一问题的答案
 * GET /api/question/userAnswer?userId=xx&questionId=xx
 */
export const getUserAnswera = ( questionId: number,userId: number) => {
    return axios.get(`${QUESTION_MODULE}/userAnswer`, {
        params: {
            userId,questionId,
        }
    }).then(res => {
        console.log("answer data:", res.data);
        return res;
    });
};
