import React, { useState, useMemo, useEffect } from "react";
import Breadcrumb from "../../components/Breadcrumb/Breadcrumb.jsx";
import content from "../../data/content.json";
import {useLoaderData} from "react-router-dom"
import Rating from "../../components/Rating/Rating.jsx";
import {Link} from "react-router-dom"
import SizeFilter from "../../components/Filters/SizeFilter.jsx";
import ProductColors from "./ProductColors.jsx";
import SvgCreditCard from "../../components/common/SvgCreditCard.jsx";
import SvgShipping from "../../components/common/SvgShipping.jsx";
import SvgCloth from "../../components/common/SvgCloth.jsx";
import SvgReturn from "../../components/common/SvgReturn.jsx";
import SectionHeading from "../../components/sections/SectionHeading/SectionHeading.jsx";
import ProductCard from "../ProductListPage/ProductCard.jsx";

const categories = content?.categories;
const extraSections = [
    {
        icon:<SvgCreditCard/>,
        label:'Secure Payment'
    },
    {
        icon:<SvgShipping/>,
        label:'Free Shipping'
    },
    {
        icon:<SvgCloth/>,
        label:'Size & Fit'
    },
    {
        icon:<SvgReturn/>,
        label:'Free Shipping and Returns'
    }
]

function ProductDetails() {
    const { product } = useLoaderData();

    const [image, setImage] = useState(
        product?.images[0]?.startsWith("http") ? product?.images[0] : product?.thumbnail
    );

    useEffect(() => {
        if (!product) return;

        setImage(
            product?.images?.[0]?.startsWith("http")
                ? product.images[0]
                : product.thumbnail
        );
    }, [product]);

    const [breadCrumbLinks, setBreadCrumbLink] = useState([]);

    const similarProducts = useMemo(() => {
        return content?.products?.filter((item)=>item?.type_id === product?.type_id && item?.id !== product?.id)
    },[product])

    const productCategory = useMemo(() => {
        return categories?.find(category => category?.id === product?.category_id);
    }, [product]);

    useEffect(() => {
        if (!productCategory) return;

        const arrayLinks = [
            { title: "Shop", path: "/" },
            { title: productCategory.name, path: productCategory.path }
        ];

        const productType = productCategory?.types?.find(item =>
            (item.id || item.type_id) === product?.type_id
        );

        if (productType) {
            arrayLinks.push({ title: productType.name, path: productType.path || "#" });
        }

        setBreadCrumbLink(arrayLinks);
    }, [productCategory, product]);

    return (
        <>
            <div className="flex flex-col md:flex-row px-10">
                <div className="w-full lg:w-1/2 md:w-2/5">
                    {/*image*/}
                    <div className="flex flex-col md:flex-row">
                        <div className="w-full md:w-1/5 justify-center h-10 md:h-[420px]">
                            <div className="flex flex-row md:flex-col justify-center h-full">
                                {product?.images?.length > 0 &&
                                    product.images.map((item, index) => (
                                        <button key={index} onClick={() => setImage(item)}>
                                            <img
                                                src={item}
                                                alt={`sample-${index}`}
                                                className="h-20 w-20 bg-cover bg-center p-2 hover:scale-105"
                                            />
                                        </button>
                                    ))}
                            </div>
                        </div>
                        <div className="w-full md:w-4/5 max-h-[620px] flex items-center justify-center bg-gray-50 rounded md:pt-0 pt-10">
                            <img
                                src={image}
                                alt="product"
                                className="max-h-[620px] max-w-full object-contain"
                            />
                        </div>
                    </div>
                </div>
                <div className="w-3/5 px-10">
                    {/*product description*/}
                    <Breadcrumb links={breadCrumbLinks} />
                    <p className={"text-3xl pt-4"}>{product?.title}</p>
                    <Rating rating={product?.rating} />
                    <p className="text-xl font-bold py-2">${product?.price}</p>
                    <div className="flex flex-col">
                        <div className="flex gap-3">
                            <p className="text-sm bold">Select Size</p>
                            <a
                                href="https://esencasizing.com/how-to-measure-yourself-at-home-a-complete-guide/"
                                target="_blank"
                                rel="noopener noreferrer"
                                className="text-sm text-gray-600 hover:text-gray-800 transition"
                            >
                                Size Guide →
                            </a>
                        </div>
                        <SizeFilter sizes={product?.size} hideTitle={true} />
                        <div>
                            <p className={"text-lg font-semibold"}>Avaliable Colors</p>
                            <ProductColors colors={product?.color} />
                        </div>
                        <div className="flex">
                            <button
                                className="px-12 py-2 bg-slate-800 hover:shadow-lg transition-all text-white mt-6 uppercase">
                                Add to cart
                            </button>
                        </div>
                        <div className="grid grid-cols-2 pt-4 gap-4">
                            {
                                extraSections?.map((section)=>(
                                    <div className={"flex items-center"}>
                                        {section?.icon}
                                        <p className={"px-2"}>{section?.label}</p>
                                    </div>
                                ))
                            }
                        </div>
                    </div>
                </div>

                {/*PRODUCT DESCRIPTION*/}
            </div>
            <div className={"md:w-[50%] w-full p-10"}>
                <SectionHeading title={"Product Description"} />
                <p className={"px-8"}>{product?.description}</p>
            </div>
            <div className="px-10">
                <SectionHeading title={"Similar Products"} />
                <div className={"flex px-10"}>
                    <div className={"pt-4 px-2 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3  gap-8"}>
                        {
                            similarProducts?.map((item, index) => (
                                <ProductCard key={index} {...item} />
                            ))
                        }
                        {
                            !similarProducts.length && <p>Similar products not found</p>
                        }
                    </div>
                </div>
            </div>
        </>
    );
}

export default ProductDetails;
