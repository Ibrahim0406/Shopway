import React from 'react';
import SectionHeading from "../sections/SectionHeading/SectionHeading.jsx";
import abtimg from "../../../public/images/aboutusimgcmp.jpeg";

function AboutUs() {
    return (
        <div className="mt-40 mb-40">
            <SectionHeading title="About Us" />
            <section className="flex flex-col md:flex-row items-center justify-center gap-10 max-md:px-4">
                <div className="relative shadow-2xl shadow-indigo-600/40 rounded-2xl overflow-hidden shrink-0">
                    <img
                        className="max-w-md w-full object-cover rounded-2xl"
                        loading="lazy"
                        src={abtimg}
                        alt="Shopway clothing brand"
                    />
                </div>

                <div className="text-sm text-slate-600 max-w-lg">
                    <h1 className="text-xl uppercase font-semibold text-slate-700">
                        Who we are
                    </h1>

                    <div className="w-24 h-[3px] rounded-full bg-gradient-to-r from-indigo-600 to-[#DDD9FF]"></div>

                    <p className="mt-8">
                        <strong>Shopway</strong> is a modern clothing brand built for individuals who value
                        style, comfort, and confidence. Our mission is to deliver carefully selected apparel
                        that fits today’s lifestyle — clean designs, quality materials, and timeless aesthetics.
                    </p>

                    <p className="mt-4">
                        We focus on creating a seamless shopping experience where fashion meets simplicity.
                        Every piece in our collection is chosen with attention to detail, ensuring durability,
                        comfort, and a look that stands out without trying too hard.
                    </p>

                    <p className="mt-4">
                        Whether you’re dressing for everyday wear or looking for something special,
                        Shopway is here to help you express your identity through clothing that feels right
                        and looks even better.
                    </p>

                    <a
                        href="#"
                        className="flex items-center w-max gap-2 mt-8 hover:-translate-y-0.5 transition bg-gradient-to-r from-indigo-600 to-[#8A7DFF] py-3 px-8 rounded-full text-white"
                    >
                        <span>Explore our collection</span>
                        <svg
                            width="13"
                            height="12"
                            viewBox="0 0 13 12"
                            fill="none"
                            xmlns="http://www.w3.org/2000/svg"
                        >
                            <path
                                d="M12.53 6.53a.75.75 0 0 0 0-1.06L7.757.697a.75.75 0 1 0-1.06 1.06L10.939 6l-4.242 4.243a.75.75 0 0 0 1.06 1.06zM0 6v.75h12v-1.5H0z"
                                fill="#fff"
                            />
                        </svg>
                    </a>
                </div>
            </section>
        </div>
    );
}

export default AboutUs;
