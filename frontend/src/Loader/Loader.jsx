import React from 'react';

function Loader() {
    return (
        <div className="flex justify-center items-center h-screen fixed top-0 left-0 right-0 bottom-0 w-full z-50 overflow-hidden bg-gray-100 opacity-75">
            <div className="spinner-border inline-block w-8 h-8 rounded-full" role="status">
        <span className="visually-hidden">
            <div className="flex flex-row gap-2">
              <div className="w-4 h-4 rounded-full bg-blue-700 animate-bounce [animation-delay:.7s]"></div>
              <div className="w-4 h-4 rounded-full bg-blue-700 animate-bounce [animation-delay:.3s]"></div>
              <div className="w-4 h-4 rounded-full bg-blue-700 animate-bounce [animation-delay:.7s]"></div>
            </div>
            <span className="sr-only">Loading...</span>
        </span>
            </div>
        </div>
    );
}

export default Loader;