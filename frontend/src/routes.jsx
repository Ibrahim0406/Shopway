import { createBrowserRouter } from "react-router-dom";
import Shop from "./Shop";
import ShopApplicationWrapper from "./pages/ShopApplicationWrapper";
import ProductListPage from "./pages/ProductListPage/ProductListPage";
import ProductDetails from "./pages/ProductDetailPage/ProductDetails";
import { loadProductBySlug } from "./routes/products";
import AuthenticationWrapper from "./pages/AuthenticationWrapper.jsx";
import Login from "./pages/Login/Login.jsx";
import Register from "./pages/Register/Register.jsx";
import OAuth2LoginCallBack from "./pages/OAuth2LoginCallBack.jsx";
import Cart from "./pages/Cart/Cart.jsx";
import Account from "./pages/Account/Account.jsx";
import ProtectedRoute from "./components/ProtectedRoute/ProtectedRoute.jsx";
import Checkout from "./pages/Checkout/Checkout.jsx";
import PaymentPage from "./pages/PaymentPage/PaymentPage.jsx";
import OrderConfirmed from "./pages/OrderConfirmed/OrderConfirmed.jsx";
import Profile from "./pages/Account/Profile.jsx";
import Orders from "./pages/Account/Orders.jsx";
import Settings from "./pages/Account/Settings.jsx";
import AdminPanel from "./pages/AdminPanel/AdminPanel.jsx";


export const router = createBrowserRouter([
    {
        path: "/",
        element: <ShopApplicationWrapper />,
        children:[
            {
                path:"/",
                element:<Shop />
            },
            {
                path:"/women",
                element:<ProductListPage categoryType={'WOMEN'}/>,
            },
            {
                path:"/men",
                element:<ProductListPage categoryType={'MEN'}/>,
            },
            {
                path:"/product/:slug",
                loader: loadProductBySlug,
                element: <ProductDetails />
            },
            {
                path: "/cart-items",
                element: <Cart />
            },
            {
                path: "/account-details/",
                element: <ProtectedRoute><Account /></ProtectedRoute>,
                children:[
                    {
                        path: 'profile',
                        element: <ProtectedRoute><Profile /></ProtectedRoute>
                    },
                    {
                        path: 'orders',
                        element: <ProtectedRoute><Orders /></ProtectedRoute>
                    },
                    {
                        path: 'settings',
                        element: <ProtectedRoute><Settings /></ProtectedRoute>
                    }
                ]
            },
            {
                path: "/checkout",
                element: <ProtectedRoute><Checkout/></ProtectedRoute>
            },
            {
                path: "/orderConfirmed",
                element: <OrderConfirmed />
            }
        ]
    },
    {
        path:"/v1/",
        element:<AuthenticationWrapper />,
        children:[
            {
                path:"login",
                element:<Login />
            },
            {
                path:"register",
                element:<Register />
            }
        ]
    },
    {
        path:'/oauth2/callback',
        element:<OAuth2LoginCallBack />
    },
    {
        path: "/admin/*",
        element: <ProtectedRoute><AdminPanel/></ProtectedRoute>
    }

]);
