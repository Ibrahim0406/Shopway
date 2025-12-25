import Banner from "../components/Banner/Banner.jsx";
import Navigation from "../components/Navigation/Navigation.jsx";
import { Outlet } from "react-router-dom";

function HomeLayout() {
    return (
        <>
            <Banner />
            <Navigation />
            <Outlet />
        </>
    );
}

export default HomeLayout;
