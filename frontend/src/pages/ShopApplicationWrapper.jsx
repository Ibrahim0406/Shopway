import React from 'react';
import Navigation from "../components/Navigation/Navigation.jsx";
import {Outlet} from "react-router-dom";
import Loader from "../Loader/Loader.jsx";
import {useSelector} from "react-redux";

function ShopApplicationWrapper(props) {
    const isLoading = useSelector((state) => state?.commonState?.loading);
    return (
        <div>
            <Navigation/>
            <Outlet/>
            {isLoading && <Loader/>}
        </div>
    );
}

export default ShopApplicationWrapper;