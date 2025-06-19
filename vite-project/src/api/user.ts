//import {axios} from '../utils/request'
import {USER_MODULE} from './_prefix'
import axios from 'axios';

type LoginInfo = {
    username: string,
    password: string
}

export interface Address {
    name: string;
    phone: string;
    address: string;
}

type RegisterInfo = {
    username: string,
    password: string,
    name: string,
    avatar?: string,
    role: string,
    telephone?: string,
    email?: string,
    location?: string,
    addressBook?: Address[]
}

type UpdateInfo = {
    username: string | null,
    password?: string,
    name?: string,
    avatar?: string,
    role?: string,
    telephone?: string,
    email?: string,
    location?: string,
    addressBook?: Address[]
}

// 如果有“Vue: This may be converted to an async function”警告，可以不管
// 用户登录
export const userLogin = (loginInfo: LoginInfo) => {
    return axios.post(`${USER_MODULE}/login`, loginInfo, {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

// 用户注册
export const userRegister = (registerInfo: RegisterInfo) => {
    return axios.post(`${USER_MODULE}`, registerInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

// 获取用户信息
export const userInfo = (username: string | null) => {
    console.log("username",username);
    const token = sessionStorage.getItem('token');
    return axios.get(`${USER_MODULE}/${username}`, {
        params: { username }, // 使用对象形式
        headers: {
            'token': token // 使用 'token' 作为请求头
        }
    })
        .then(res => {
            console.log("user",res)
            return res
        })
}
export const getUserInfo = (userId: number) => {
    const token = sessionStorage.getItem('token');
    return axios.get(`${USER_MODULE}`, {
        params: { id: userId },
        headers: {
            'token': token
        }
    })
        .then (res => {
            console.log("Res data:", res.data);
            return res;
        })
        .catch (error => {
            console.error("Error fetching user info:", error);
            throw error;
        })
}
// // 获取用户信息
// export const userInfo_1 = () => {
//     return axios.get(`${USER_MODULE}`)
//         .then(res => {
//             return res.data; // 这里假设 res.data 包含用户信息，如 { name: '...', role: '...' }
//         })
//         .catch(error => {
//             console.error('获取用户信息失败:', error);
//             throw error; // 处理错误
//         });
// }

// 更新用户信息
export const userInfoUpdate = (updateInfo: UpdateInfo) => {
    console.log("updateInfo", updateInfo)
    const token = sessionStorage.getItem('token')
    return axios.put(`${USER_MODULE}`, updateInfo, {
        headers: {
            'Content-Type': 'application/json',
            'token': token
        },
    })
        .then(res => {
            return res
        })
        .catch(err => {
            console.error("UserInfo update error:", err)
        })
}


// 增加积分
export const addCredit = (updateId: number ,updateScore: number) => {
    console.log("addCredit", updateId, updateScore)
    const token = sessionStorage.getItem('token');
    return axios.put(`${USER_MODULE}/add`, null, {
        headers: {
            'Content-Type': 'application/json',
            'token': token
        },
        params: {
            userId: updateId,
            score: updateScore
        }
    })
        .then(res => {
            return res
        })
        .catch(err => {
            console.error("addCredit error:", err)
        })
}

// 更新等级
export const updateLevel = (userId: number) => {
    console.log("updateLevel", userId)
    const token = sessionStorage.getItem('token')
    return axios.put(`${USER_MODULE}/update/${userId}`, userId, {
        headers: {
            'token': token
        }
    })
        .then(res => {
            return res
        })
        .catch(error => {
            console.error("Error updating level:", error);
        })
}