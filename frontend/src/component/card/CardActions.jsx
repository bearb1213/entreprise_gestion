import React from 'react';

const CardActions = ({ children, align = 'end' }) => {
  const alignment = {
    start: 'justify-start',
    center: 'justify-center',
    end: 'justify-end',
    between: 'justify-between'
  };

  return (
    <div className={`flex gap-2 mt-4 ${alignment[align]}`}>
      {children}
    </div>
  );
};

// Composant de bouton pour la carte
const CardButton = ({ children, variant = 'secondary', ...props }) => {
  const variants = {
    primary: 'bg-blue-500 text-white hover:bg-blue-600',
    secondary: 'bg-gray-100 text-gray-700 hover:bg-gray-200',
    danger: 'bg-red-500 text-white hover:bg-red-600'
  };

  return (
    <button
      className={`px-3 py-2 rounded text-sm font-medium transition-colors ${variants[variant]}`}
      {...props}
    >
      {children}
    </button>
  );
};

export { CardActions, CardButton };