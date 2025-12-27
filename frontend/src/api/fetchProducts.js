import axios from "axios";
import { API_BASE_URL, API_URLS} from "./constant.js";

export const getAllProducts = async (id, typeId) => {
    let url = API_BASE_URL + API_URLS.GET_PRODUCTS + `?categoryId=${id}`
    if (typeId) {
        url = url + `&typeId=${typeId}`;
    }

    try{
        const result = await axios(url, {
            method: "GET",
        });
        return result.data;
    }catch(error){
        console.log(error)
    }
}