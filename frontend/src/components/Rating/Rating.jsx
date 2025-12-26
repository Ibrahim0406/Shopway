import React, {useMemo} from 'react';
import SvgStarIcon from "../common/SvgStarIcon.jsx";
import SvgEmptyStarIcon from "../common/SvgEmptyStarIcon.jsx";

function Rating({ rating }) {
    const ratingNumber = useMemo(()=>{
        return Array(Math.floor(Number(rating))).fill();
    }, [rating]);
    return (
        <div className={"flex items-center"}>
            {
                ratingNumber?.map((_, index) => (
                    <SvgStarIcon key={index}/>
                ))
            }
            {
                new Array(5-ratingNumber?.length).fill().map((_,index)=>(
                    <SvgEmptyStarIcon key={'empty'+index}/>
                ))
            }
            <p className={"px-2 text-gray-600"}>{rating}</p>
        </div>
    );
}

export default Rating;