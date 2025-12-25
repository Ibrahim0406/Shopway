import Navigation from "./components/Navigation/Navigation.jsx";
import HeroSection from "./components/HeroSection/HeroSection.jsx";
import NewArrivals from "./components/sections/NewArrivals.jsx";
import Banner from "./components/Banner/Banner.jsx";
import Card from "./components/Card/Card.jsx";
import Category from "./components/sections/Categories/Category.jsx";
import content from "../src/data/content.json"
import Footer from "./components/Footer/Footer.jsx";

function App() {

  return (
    <div className={"App"}>
        <HeroSection />
        <NewArrivals />
        {content?.pages.shop.sections && content?.pages?.shop.sections.map((item, index)=><Category key={item?.title+index} {...item}></Category>)}
        <Footer />
    </div>
  )
}

export default App
