import React from 'react';
import {useCallback} from "react";

export const colorSelector = {
    "Purple":"#8434E1",
    "Black":"#252525",
    "White":"#FFFFFF",
    "Gray": "#808080",
    "Blue": "#0000FF",
    "Red": "#FF0000",
    "Orange": "#FFA500",
    "Navy": "#000080",
    "Grey": "#808080",
    "Yellow": "#FFFF00",
    "Pink": "#FFC0CB",
    "Green": "#008000"
}

function ColorsFilter({colors}) {
    const [appliedColors, setAppliedColors] = React.useState([]);
    const onClickDiv = useCallback((item)=>{
        setAppliedColors(prevColors => {
            if (prevColors.indexOf(item) > -1){
                return prevColors.filter(color => color !== item);
            }else{
                return [...prevColors, item];
            }
        });
    }, []); // Prazan dependency array jer koristimo functional update

    return (
        <div className={"flex flex-wrap mb-4"}>
            <p className={"text-lg text-black mt-5"}>Colors</p>
            <div className={"flex flex-wrap p-4"}>
                {colors?.map((item, index)=>{
                    return(
                        <div key={index} className={"flex flex-col mr-2"}>
                            <div className="w-8 h-8 rounded-lg bg-orange-400 mr-4 cursor-pointer hover:outline-2 hover:scale-105 transition-all" onClick={()=>onClickDiv(item)} style={{background: `${colorSelector[item]}`}}></div>
                            <p className={"text-sm text-gray-600 mb-2"} style={{color:`${appliedColors?.includes(item) ? 'black' : ''}`}}>{item}</p>
                        </div>
                    )})}
            </div>
        </div>
    );
}

export default ColorsFilter;