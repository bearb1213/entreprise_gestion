import React from 'react';

const CardGrid = ({ children, gap = 4, cardClassName = '' }) => {
  return (
    <div className="relative">
      {/* Container avec défilement horizontal */}
      <div className={`flex overflow-x-auto scrollbar-hide snap-x snap-mandatory gap-${gap} py-2`}>
        {React.Children.map(children, (child, index) => (
          <div className={`flex-shrink-0 snap-start ${cardClassName}`}>
            {child}
          </div>
        ))}
      </div>
      
      {/* Indicateurs de défilement (optionnel) */}
      <div className="flex justify-center mt-4 space-x-2">
        {React.Children.map(children, (_, index) => (
          <div className="w-2 h-2 bg-gray-300 rounded-full"></div>
        ))}
      </div>
    </div>
  );
};

export default CardGrid;