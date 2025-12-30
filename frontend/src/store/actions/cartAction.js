import { addToCart } from "../features/cart"


export const addItemToCartAction = (productItem)=>{
    return (dispatch,state) =>{
        dispatch(addToCart(productItem));
        const {cartState} = state();
        localStorage.setItem("cart",JSON.stringify(cartState?.cart))
    }
}