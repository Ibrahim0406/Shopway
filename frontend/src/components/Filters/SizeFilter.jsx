import React from 'react';
import {colorSelector} from "./ColorsFilter.jsx";
import {useCallback} from "react"

function SizeFilter({sizes, hideTitle}) {
    const [appliedSize, setAppliedSize] = React.useState([]);
    const onClickDiv = useCallback((item)=>{
        setAppliedSize(prevSize => {
            if (prevSize.indexOf(item) > -1){
                return prevSize.filter(size => size !== item);
            }else{
                return [...prevSize, item];
            }
        });
    }, []); // Prazan dependency array

    return (
        <div className={"flex flex-wrap mb-4"}>
            {!hideTitle && <p className={"text-lg text-black mt-5"}>Size</p>}
            <div className={"flex flex-wrap p-4"}>
                {sizes?.map((item, index)=>{
                    return(
                        <div key={index} className={"flex flex-col mr-2"}>
                            <div
                                className="w-[50px] h-8 mb-4 mr-4 rounded-lg cursor-pointer
                                            flex items-center justify-center
                                            bg-white border border-gray-500 text-gray-500
                                                hover:outline-2 hover:scale-105 transition-all"
                                onClick={() => onClickDiv(item)}
                                style={appliedSize.includes(item) ? { background: 'black', color: 'white' } : {}}
                            >
                                {item}
                            </div>
                        </div>
                    )})}
            </div>
        </div>
    );
}

export default SizeFilter;