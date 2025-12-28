import React, {useEffect, useMemo, useState} from 'react';
import FilterIcon from "../../components/common/FilterIcon.jsx";
import content from '../../data/content.json';
import Categories from "../../components/Filters/Categories.jsx";
import PriceFilter from "../../components/Filters/PriceFilter.jsx";
import ColorsFilter from "../../components/Filters/ColorsFilter.jsx";
import SizeFilter from "../../components/Filters/SizeFilter.jsx";
import ProductCard from "./ProductCard.jsx";
import {useDispatch, useSelector} from "react-redux";
import {getAllProducts} from "../../api/fetchProducts.js";
import {setLoading} from "../../store/features/common.js";

const categories = content?.categories;

function ProductListPage({ categoryType }) {
    const categoryData  = useSelector((state) => state?.categoryState?.categories)
    const dispatch = useDispatch();
    const [products, setProducts] = useState([]);

    const categoryContent = useMemo(() => {
        return categories?.find(category => category.code === categoryType);
    }, [categoryType]);
    const productListItems = useMemo(()=>{
        return content?.products.filter((product)=>product?.category_id===categoryContent?.id)
    }, [categoryContent])

    const category = useMemo(()=>{
        return categoryData?.find(element => element?.code === categoryType)
    }, [categoryData])

    useEffect(() => {
        if (!category?.id) return;

        dispatch(setLoading(true));

        getAllProducts(category.id)
            .then(res => {
                setProducts(res);
            })
            .catch(error => {
                console.error(error);
            })
            .finally(() => {
                dispatch(setLoading(false));
            });

    }, [category?.id, dispatch]);

    return (
        <div>
            <div className="flex">
                <div className="w-[20%] p-[10px] border rounded-lg m-[20px]">
                    <div className="flex justify-between">
                        <p className="text-lg text-gray-600">Filter</p>
                        <FilterIcon />
                    </div>
                    <div>
                        <p className="text-lg text-black mt-5">Categories</p>
                        <Categories types={categoryContent?.types}/>
                        <hr/>
                    </div>
                    <PriceFilter/>
                    <hr/>
                    <ColorsFilter colors={categoryContent?.meta_data?.colors}/>
                    <hr/>
                    <SizeFilter sizes={categoryContent?.meta_data?.sizes}/>
                </div>

                <div className={"p-[15px]"}>
                    <p className={"text-black text-lg"}>{category?.description}</p>
                    <div className={"pt-4 px-2 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3  gap-8"}>
                        {
                            products?.map((item, index) => (
                                <ProductCard key={item?.id+"_"+index} {...item} title={item?.name} />
                            ))
                        }
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ProductListPage;
