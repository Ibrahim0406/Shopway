import React, { useState, useMemo, useEffect } from "react";
import Breadcrumb from "../../components/Breadcrumb/Breadcrumb.jsx";
import content from "../../data/content.json";
import {useLoaderData} from "react-router-dom"

const categories = content?.categories;

function ProductDetails() {
    const { product } = useLoaderData();

    const [image, setImage] = useState(
        product?.images[0]?.startsWith("http") ? product?.images[0] : product?.thumbnail
    );

    const [breadCrumbLinks, setBreadCrumbLink] = useState([]);

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
            </div>
        </div>
    );
}

export default ProductDetails;
