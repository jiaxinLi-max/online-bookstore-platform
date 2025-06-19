import {COLUMNS_MODULE} from "./_prefix.ts";

type columnsInfo = {
    theme: string,
    description: string,
    covers: List<String>
}

type updateColumnsInfo = {
    id: number,
    theme: string,
    description: string,
    covers: List<String>
}

export const createColumns = (info: columnsInfo) => {
    const token = localStorage.getItem('token')
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
    return axios.delete(`${COLUMNS_MODULE}/${id}`, null, {
        params: { id },
        headers: {
            'token': token
        }
    }).then(res => {
        return res;
    })
}

export const updateColumns = (info: updateColumnsInfo) => {
    const token = sessionStorage.getItem("token");
    return axios.put(`${COLUMNS_MODULE}/`, info, {
        headers: {
            'token': token,
            'Content-Type': 'application/json'
        }
    }).then(res => {
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