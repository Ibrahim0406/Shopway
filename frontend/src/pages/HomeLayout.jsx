import Banner from "../components/Banner/Banner.jsx";
import Navigation from "../components/Navigation/Navigation.jsx";
import { Outlet } from "react-router-dom";
import Loader from "../Loader/Loader.jsx";
import {useSelector} from "react-redux";

function HomeLayout() {
    const isLoading = useSelector((state) => state?.commonState?.loading);
    return (
        <>
            <Banner />
            <Navigation />
            <Outlet />
            {isLoading && <Loader />}
        </>
    );
}

export default HomeLayout;
