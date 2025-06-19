import {COLUMNS_MODULE} from "./_prefix.ts";
import axios from "axios";

type columnsInfo = {
    theme: string,
    description: string,
    covers: String[]
}

type updateColumnsInfo = {
    id: number,
    theme: string,
    description: string,
    covers: String[]
};

export const createColumns = (info: columnsInfo) => {
    const token = sessionStorage.getItem('token')
    return axios.post(`${COLUMNS_MODULE}/`, info, {
        headers: {
            'Content-Type': 'application/json',
            'token': token
        }
    }).then(res => {
        return res;
    })
}

export const deleteColumns = (id: number) => {
    const token = sessionStorage.getItem("token");
    return axios.delete(`${COLUMNS_MODULE}/${id}`, {
        params: { id },
        headers: {
            'token': token
        }
    }).then(res => {
        console.log(res.data);
        return res;
    })
}

export const updateColumns = (info: updateColumnsInfo) => {
    const token = sessionStorage.getItem("token");
    console.log(info);
    return axios.put(`${COLUMNS_MODULE}/`, info, {
        headers: {
            'token': token,
            'Content-Type': 'application/json'
        }
    }).then(res => {
        console.log(res.data);
        return res;
    })
}

export const getAllColumns = () => {
    const token = sessionStorage.getItem("token");
    return axios.get(`${COLUMNS_MODULE}/`, {
        headers: {
            'token': token
        }
    }).then(res => {
        return res;
    })
}

export const getProductsByColumn = (id: number) => {
    const token = sessionStorage.getItem("token");
    return axios.get(`${COLUMNS_MODULE}/${id}`, {
        headers: {
            'token': token
        }
    });
}