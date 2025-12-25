import {createBrowserRouter} from "react-router-dom"
import App from "./App.jsx";
import ProductListPage from "./pages/ProductListPage/ProductListPage.jsx";
import ShopApplicationWrapper from "./pages/ShopApplicationWrapper.jsx";
import HomeLayout from "./pages/HomeLayout.jsx";


export const router = createBrowserRouter([
    {
        element: <ShopApplicationWrapper />, // navigation za sve OSTALO
        children: [
            {
                path: "/women",
                element: <ProductListPage categoryType={'WOMEN'}/>
            },
            {
                path: "/men",
                element: <ProductListPage categoryType={'MEN'}/>
            }
        ]
    },
    {
        path: "/",
        element: <HomeLayout />, // banner + navigation
        children: [
            {
                path: "",
                element: <App />
            }
        ]
    }
]);