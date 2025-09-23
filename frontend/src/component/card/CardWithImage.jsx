import React from 'react';

const CardWithImage = ({ 
  imageUrl, 
  imageAlt, 
  title, 
  description, 
  overlay = false 
}) => {
  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden group cursor-pointer">
      <div className="relative overflow-hidden">
        <img 
          src={imageUrl} 
          alt={imageAlt} 
          className="w-full h-48 object-cover transition-transform duration-300 group-hover:scale-105"
        />
        {overlay && (
          <div className="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-20 transition-all duration-300"></div>
        )}
      </div>
      
      <div className="p-4">
        <h3 className="font-semibold text-lg mb-2 text-gray-800">{title}</h3>
        <p className="text-gray-600 text-sm">{description}</p>
      </div>
    </div>
  );
};

export default CardWithImage;