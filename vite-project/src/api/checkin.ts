import { CHECKIN_MODULE } from './_prefix';
import axios from "axios";

export const checkIn = async(checkInId: number) => {
    console.log("Checking in:", checkInId);
    const token = sessionStorage.getItem("token");
    return axios.post(`${CHECKIN_MODULE}/${checkInId}`, null, {
        headers: {
            'token': token
        },
        params: {
            userId: checkInId
        }
    })
}

export const getCheckinHistory = async (checkInId: number) => {
    console.log("Getting checkin history for:", checkInId);
    const token = sessionStorage.getItem("token");
    return axios.get(`${CHECKIN_MODULE}/${checkInId}`, {
        headers: {
            'token': token
        }
    })
}