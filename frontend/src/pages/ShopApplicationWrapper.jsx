import React from 'react';
import Navigation from "../components/Navigation/Navigation.jsx";
import {Outlet} from "react-router-dom";

function ShopApplicationWrapper(props) {
    return (
        <div>
            <Navigation/>
            <Outlet/>
        </div>
    );
}

export default ShopApplicationWrapper;