import {combineReducers, configureStore} from "@reduxjs/toolkit";
import productReducer from "./features/products.js";
import cartReducer from "./features/cart.js";
import categoryReducer from "./features/category.js";
import commonReducer from "./features/common.js"

const rootReducer = combineReducers({
    productState: productReducer,
    cartState: cartReducer,
    categoryState: categoryReducer,
    commonState: commonReducer,
})

const store = configureStore({
    reducer: rootReducer
})

export default store;