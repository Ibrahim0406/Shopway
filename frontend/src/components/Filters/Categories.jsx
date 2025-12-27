import React from 'react';

function Categories({types}) {
    return (
        <div>
            {types?.map((type, index) => {
                return (
                    <div key={type?.code || index} className={"flex items-center"}>
                        <input type="checkbox" name={type?.code} className={"border rounded-lg w-4 h-4 accent-black text-black"}/>
                        <label className={"text-[14px] px-2 text-gray-600"} htmlFor={type?.code}>{type?.name}</label>
                    </div>
                )
            })}
        </div>
    );
}

export default Categories;