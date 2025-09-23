import React from 'react';

const CardList = ({ items, onItemClick }) => {
  return (
    <div className="space-y-4">
      {items.map((item, index) => (
        <div
          key={index}
          onClick={() => onItemClick?.(item)}
          className="bg-white rounded-lg shadow-sm border p-4 hover:shadow-md transition-shadow cursor-pointer"
        >
          <h4 className="font-medium text-gray-800 mb-2">{item.title}</h4>
          <p className="text-gray-600 text-sm">{item.description}</p>
          {item.meta && (
            <div className="flex items-center justify-between mt-3 pt-3 border-t border-gray-100">
              <span className="text-xs text-gray-500">{item.meta}</span>
              {item.status && (
                <span className={`px-2 py-1 rounded-full text-xs ${
                  item.status === 'active' ? 'bg-green-100 text-green-800' :
                  item.status === 'pending' ? 'bg-yellow-100 text-yellow-800' :
                  'bg-gray-100 text-gray-800'
                }`}>
                  {item.status}
                </span>
              )}
            </div>
          )}
        </div>
      ))}
    </div>
  );
};

export default CardList;