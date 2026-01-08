import React from 'react';
import {Admin, fetchUtils, Resource, withLifecycleCallbacks} from "react-admin";
import simpleRestProvider from "ra-data-simple-rest"
import ProductList from "./ProductList.jsx";
import EditProduct from "./EditProduct.jsx";
import CreateProduct from "./CreateProduct.jsx";
import CategoryList from "./Category/CategoryList.jsx";
import CategoryEdit from "./Category/CategoryEdit.jsx";
import {fileUploadAPI} from "../../api/fileUpload.js";

const CDN_URL = 'https://codedev.b-cdn.net';

const httpClient = (url, options={})=>{
    const token = localStorage.getItem("authToken");
    if(!options.headers) options.headers = new Headers();
    options.headers.set('Authorization', `Bearer ${token}`);
    return fetchUtils.fetchJson(url, options);
}

const dataProvider = withLifecycleCallbacks(simpleRestProvider('http://localhost:8080/api',httpClient),[
    {
        resource:"products",
        beforeSave: async (params) => {

            const requestBody = { ...params };

            // ✅ THUMBNAIL
            if (params?.thumbnail?.rawFile) {
                const fileName = params.thumbnail.rawFile.name.replaceAll(" ", "-");
                const formData = new FormData();
                formData.append("file", params.thumbnail.rawFile);
                formData.append("fileName", fileName);

                await fileUploadAPI(formData);
                requestBody.thumbnail = `${CDN_URL}/${fileName}`;
            }

            // ✅ PRODUCT RESOURCES
            const productResList = params?.productResources ?? [];

            requestBody.productResources = await Promise.all(
                productResList.map(async (productResource) => {

                    if (!productResource?.url?.rawFile) {
                        return productResource;
                    }

                    const fileName = productResource.url.rawFile.name.replaceAll(" ", "-");
                    const formData = new FormData();
                    formData.append("file", productResource.url.rawFile);
                    formData.append("fileName", fileName);

                    await fileUploadAPI(formData);

                    return {
                        ...productResource,
                        url: `${CDN_URL}/${fileName}`,
                    };
                })
            );

            return requestBody;
        }

    }
]);
function AdminPanel() {
    return (
        <Admin dataProvider = {dataProvider} basename={"/admin"}>
            <Resource name={"products"} list={ProductList} edit={EditProduct} create={CreateProduct}/>
            <Resource name={'category'} list={CategoryList} edit={CategoryEdit}/>
        </Admin>
    );
}

export default AdminPanel;