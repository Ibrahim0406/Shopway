import React from 'react';
import Wishlist from "../common/Wishlist.jsx";
import AccountIcon from "../common/AccountIcon.jsx";
import CartIcon from "../common/CartIcon.jsx";
import {Link, NavLink} from "react-router-dom";
import './Navigation.css'


function Navigation() {
    return (
        <nav className='flex items-center py-6 px-16 justify-between gap-40'>
            <div className={"flex items-center gap-6"}>
                <a href="/" className={"text-black text-3xl gap-8 font-bold"}>Shopway</a>
            </div>
            <div className="flex flex-wrap items-center gap-10 flex-1">
                <ul className={"flex gap-14 text-gray-600 hover:text-black"}>
                    <li><NavLink to="/" className={({isActive})=>isActive? 'active-link':''}>Shop</NavLink></li>
                    <li><NavLink to="/men" className={({isActive})=>isActive? 'active-link':''}>Men</NavLink></li>
                    <li><NavLink to="/women" className={({isActive})=>isActive? 'active-link':''}>Women</NavLink></li>
                    <li><NavLink to="/kids" className={({isActive})=>isActive? 'active-link':''}>Kids</NavLink></li>
                </ul>
            </div>
            <div className="flex items-center border pl-3 gap-2 bg-white border-gray-500/30 h-[46px] rounded-md overflow-hidden max-w-md w-full">
                <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 30 30" fill="#6B7280">
                    <path d="M13 3C7.489 3 3 7.489 3 13s4.489 10 10 10a9.95 9.95 0 0 0 6.322-2.264l5.971 5.971a1 1 0 1 0 1.414-1.414l-5.97-5.97A9.95 9.95 0 0 0 23 13c0-5.511-4.489-10-10-10m0 2c4.43 0 8 3.57 8 8s-3.57 8-8 8-8-3.57-8-8 3.57-8 8-8"/>
                </svg>
                <input type="text" placeholder="Search for products" className="w-full h-full outline-none text-gray-500 placeholder-gray-500 text-sm" />
            </div>
            <div className="flex flex-wrap items-center gap-4">
                <ul className={"flex items-center gap-8"}>
                    <li><button><Wishlist></Wishlist></button></li>
                    <li><button><AccountIcon></AccountIcon></button></li>
                    <li><Link to={"/cart-items"}><CartIcon/></Link></li>
                </ul>
            </div>
        </nav>
    );
}

export default Navigation;