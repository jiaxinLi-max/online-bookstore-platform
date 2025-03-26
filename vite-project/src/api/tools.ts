import axios from 'axios';


export const getImage = async (file: File) => {
    const formData = new FormData();
    formData.append('file', file); // 将文件添加到 FormData

    try {
        const response = await axios.post('api/images', formData, {
            headers: {
                'Content-Type': 'multipart/form-data', // 确保设置正确的 Content-Type
            },
        });
        console.log("response", response);
        return response.data; // 根据 API 的返回格式调整
    } catch (error) {
        console.error('获取用户头像 失败:', error);
        throw error; // 重新抛出错误
    }
};