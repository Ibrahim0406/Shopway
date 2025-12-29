import React from 'react';
import heroBackground from '../../assets/img/hero-background.jpg';

function HeroSection() {
    return (
        <div
            className="relative bg-cover bg-center h-svh w-full"
            style={{ backgroundImage: `url(${heroBackground})` }}
        >
            <div className="absolute inset-0 flex items-center">
                <main className="px-10 lg:px-24 text-left">
                    <h2 className="text-2xl">Shirts / Sweaters</h2>

                    <p className="mt-3 text-6xl">
                        Spring Value Pack
                    </p>

                    <p className="mt-3 text-2xl">
                        cool / colorful / comfy
                    </p>

                    <button className="mt-6 px-7 py-3 rounded bg-indigo-600 hover:bg-indigo-700 text-white font-medium">
                        Shop Now
                    </button>
                </main>
            </div>
        </div>
  );
}

export default HeroSection;
