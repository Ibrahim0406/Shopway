import React from 'react';
import ArrowIcon from "../common/ArrowIcon.jsx";

function Card({imagePath, title, description, actionArrow, height, width}) {
    return (
        <div className={"flex flex-col p-8"}>
            <img
                src={imagePath}
                alt="imagehere"
                loading="lazy"
                style={{
                    height: height || '240px',
                    width: width || '200px',
                    objectFit: 'cover',
                    objectPosition: 'center'
                }}
                className="rounded hover:scale-105 cursor-pointer transition-all"
            />
            <div className="flex justify-between">
                <div className="flex flex-col p-2">
                    <p className="text-[16px]">{title}</p>
                    {description && <p className={"text-[12px] text-gray-600"}>{description}</p>}
                </div>
                {actionArrow && <span className={"cursor-pointer pr-2 py-2 items-center"}><ArrowIcon></ArrowIcon></span>}
            </div>
        </div>
    );
}

export default Card;