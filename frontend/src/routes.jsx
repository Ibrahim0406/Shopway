import {createBrowserRouter} from "react-router-dom"
import App from "./App.jsx";
import ProductListPage from "./pages/ProductListPage/ProductListPage.jsx";
import ShopApplicationWrapper from "./pages/ShopApplicationWrapper.jsx";
import HomeLayout from "./pages/HomeLayout.jsx";
import ProductDetails from "./pages/ProductDetailPage/ProductDetails.jsx";
import {loadProductByID} from "./routes/products.js";


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
            },
            {
                path: "/product/:productId",
                loader: loadProductByID,
                element: <ProductDetails />
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