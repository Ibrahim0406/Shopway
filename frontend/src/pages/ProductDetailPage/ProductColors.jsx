import React from 'react';

function ProductColors({colors}) {
    return (
        <div className={"flex pt-2"}>
            {
                colors?.map((color, index) => (
                    <div style={{background: color?.toLowerCase()}} className={'rounded-[50%] w-4 h-4 mx-2'}></div>
                ))
            }
        </div>
    );
}

export default ProductColors;