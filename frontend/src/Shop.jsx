import HeroSection from "./components/HeroSection/HeroSection.jsx";
import NewArrivals from "./components/sections/NewArrivals.jsx";
import Category from "./components/sections/Categories/Category.jsx";
import content from "../src/data/content.json"
import Footer from "./components/Footer/Footer.jsx";
import { useEffect } from "react";
import { fetchCategories } from "./api/fetchCategories.js";
import { useDispatch } from "react-redux";
import { loadCategories } from "./store/features/category.js";
import { setLoading } from "./store/features/common.js";

const Shop = () => {

    const dispatch = useDispatch();



    useEffect(()=>{
        dispatch(setLoading(true));
        fetchCategories().then(res=>{
            dispatch(loadCategories(res));
        }).catch(err=>{

        }).finally(()=>{
            dispatch(setLoading(false));
        })
    },[dispatch]);

    return (
        <>
            <HeroSection />
            <NewArrivals />
            {content?.pages?.shop?.sections && content?.pages?.shop?.sections?.map((item, index) => <Category key={item?.title+index} {...item} />)}
            <Footer content={content?.footer}/>
        </>
    )
}

export default Shop;