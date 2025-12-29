import {getToken} from "../utils/jwt-helper.jsx";

export const API_URLS = {
    GET_PRODUCTS: '/api/products',
    GET_PRODUCT: (id) => `/api/products/${id}`,
    GET_CATEGORIES: '/api/category',
}

export const API_BASE_URL = "http://localhost:8080";

export const getHeaders = ()=>{
    return {
        'Authorization':`Bearer ${getToken()}`
    }

}