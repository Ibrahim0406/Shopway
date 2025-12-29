import React from 'react';
import SectionHeading from "./SectionHeading/SectionHeading.jsx";
import Card from "../Card/Card.jsx";
import Jeans from '../../assets/img/jeans.jpg'
import Coats from '../../assets/img/coats.jpg'
import Tshirts from '../../assets/img/tshirts.jpg'
import Dresses from '../../assets/img/dresses.jpg'
import Joggers from '../../assets/img/joggers.jpg'
import Shirts from '../../assets/img/shirts.jpg'
import Carousel from 'react-multi-carousel';
import {responsive} from "../../utils/Section.constant.js";
import './NewArrivals.css'

function NewArrivals() {
    const items = [
        {
            'title': 'Jeans',
            imagePath: Jeans,
        },
        {
            'title': 'Coats',
            imagePath: Coats,
        },
        {
            'title': 'T-shirts',
            imagePath: Tshirts,
        },
        {
            'title': 'Shirts',
            imagePath: Shirts,
        },
        {
            'title': 'Dresses',
            imagePath: Dresses,
        },
        {
            'title': 'Joggers',
            imagePath: Joggers,
        },
    ]
    return (
        <>
            <SectionHeading title={'New Arrivals'}></SectionHeading>
            <Carousel responsive={responsive} autoPlay={false} swipeable={true} draggable={false} showDots={false}
            infinite={false} partialVisible={false} itemClass={'react-slider-custom-item'} className={"px-8"}>
                {items && items?.map((item,index)=><span className={"font-semibold"}><Card title={item.title} imagePath={item.imagePath}
                                                               key={item?.title+index}></Card></span>)}
            </Carousel>
        </>
    );
}

export default NewArrivals;