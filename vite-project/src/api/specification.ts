//import axios from 'axios';
import {axios} from '../utils/request'
export interface Specification {
    id: string; // 规格的唯一标识符
    item: string; // 规格的名称（例如 "作者" 或 "副标题"）
    value: string; // 规格的值（例如 "Robert C. Martin" 或 "程序员的职业素养"）
    productId: string; // 关联的产品 ID
}
type SpecificationInfo = {
    id: string; // 规格的唯一标识符
    item: string; // 规格的名称（例如 "作者" 或 "副标题"）
    value: string; // 规格的值（例如 "Robert C. Martin" 或 "程序员的职业素养"）
    productId: string; // 关联的产品 ID
};