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

function App() {
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(setLoading(true));

        fetchCategories()
            .then(res => {
                dispatch(loadCategories(res));
            })
            .catch(error => {
                console.error(error);
            })
            .finally(() => {
                dispatch(setLoading(false));
            });

    }, [dispatch]);

    return (
        <div className="App">
            <HeroSection />
            <NewArrivals />

            {content?.pages?.shop?.sections?.map((item, index) => (
                <Category
                    key={item?.title || index}
                    {...item}
                />
            ))}

            <Footer />
        </div>
    );
}

export default App;