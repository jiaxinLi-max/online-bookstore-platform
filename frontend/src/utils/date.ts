import { format } from 'date-fns';

export const formatTime = (
    timeStr: string | number,
    pattern = 'yyyy-MM-dd HH:mm'
) => {
    try {
        return format(new Date(timeStr), pattern);
    } catch {
        return '无效时间';
    }
};