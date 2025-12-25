import React from 'react';
import ArrowIcon from "../../components/common/ArrowIcon.jsx";
import FavouriteIcon from "../../components/common/FavouriteIcon.jsx";

function ProductCard({title, description, price, discount, rating, brand, thumbnail}) {
    return (
        <div className={"flex flex-col hover:scale-105 transition relative"}>
            <img
                src={thumbnail}
                alt="imagehere"
                loading="lazy"
                style={{
                    objectFit: 'cover',
                    objectPosition: 'center'
                }}
                className="rounded cursor-pointer h-[320px] max-h-[320px] w-[280px] max-w-[280px]"
            />
            <button onClick={()=>console.log("click button")} className={"absolute top-0 right-0 pt-2 pr-2"}><FavouriteIcon/></button>
            <div className="flex justify-between">
                <div className="flex flex-col pt-2">
                    <p className="text-[16px] font-semibold p-1">{title}</p>
                    {description && <p className={"text-[12px] text-gray-600 px-1"}>{brand}</p>}
                </div>
                <div>
                    <p>${price}</p>
                </div>
            </div>
        </div>
    );
}

export default ProductCard;