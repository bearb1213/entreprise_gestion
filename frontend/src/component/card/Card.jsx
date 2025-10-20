import React, { useState } from 'react';

const Card = ({ 
  title, 
  description, 
  children, 
  imageUrl, 
  footer,
  className = '',
  hoverEffect = true,
  width = 300,
  height = 400,
  showOverflow = false,
  variant = 'default',
  border = false,
  shadow = 'medium'
}) => {
  const [imageLoaded, setImageLoaded] = useState(false);
  const [imageError, setImageError] = useState(false);

  const variantStyles = {
    default: {
      bg: 'bg-white',
      text: 'text-gray-800',
      border: 'border-gray-200',
      header: 'bg-white',
      footer: 'bg-gray-50'
    },
    primary: {
      bg: 'bg-gradient-to-br from-blue-50 to-indigo-50',
      text: 'text-gray-800',
      border: 'border-blue-200',
      header: 'bg-gradient-to-r from-blue-50 to-indigo-50',
      footer: 'bg-gradient-to-r from-blue-50 to-indigo-50'
    },
    success: {
      bg: 'bg-gradient-to-br from-green-50 to-emerald-50',
      text: 'text-gray-800',
      border: 'border-green-200',
      header: 'bg-gradient-to-r from-green-50 to-emerald-50',
      footer: 'bg-gradient-to-r from-green-50 to-emerald-50'
    },
    warning: {
      bg: 'bg-gradient-to-br from-yellow-50 to-amber-50',
      text: 'text-gray-800',
      border: 'border-yellow-200',
      header: 'bg-gradient-to-r from-yellow-50 to-amber-50',
      footer: 'bg-gradient-to-r from-yellow-50 to-amber-50'
    },
    danger: {
      bg: 'bg-gradient-to-br from-red-50 to-pink-50',
      text: 'text-gray-800',
      border: 'border-red-200',
      header: 'bg-gradient-to-r from-red-50 to-pink-50',
      footer: 'bg-gradient-to-r from-red-50 to-pink-50'
    },
    info: {
      bg: 'bg-gradient-to-br from-cyan-50 to-blue-50',
      text: 'text-gray-800',
      border: 'border-cyan-200',
      header: 'bg-gradient-to-r from-cyan-50 to-blue-50',
      footer: 'bg-gradient-to-r from-cyan-50 to-blue-50'
    }
  };

  const shadowStyles = {
    none: 'shadow-none',
    small: 'shadow-sm',
    medium: 'shadow-lg',
    large: 'shadow-xl',
    xl: 'shadow-2xl'
  };

  const currentVariant = variantStyles[variant] || variantStyles.default;
  const currentShadow = shadowStyles[shadow] || shadowStyles.medium;

  

  return (
    <div 
      className={`
        rounded-2xl overflow-hidden
        ${currentVariant.bg} 
        ${currentVariant.text}
        ${border ? `${currentVariant.border} border` : 'border-0'}
        ${hoverEffect && 'transition-all duration-300 hover:scale-105 hover:shadow-2xl cursor-pointer'}
        ${currentShadow}
        ${className}
        group
      `}
      style={{ 
        maxWidth: `${width}px`,
        minHeight: `${height}px`,
        display: 'flex',
        flexDirection: 'column'
      }}
    >
      {/* Image avec effet de survol */}
      {imageUrl && !imageError && (
        <div 
          className="w-full h-48 overflow-hidden relative bg-gradient-to-br from-gray-100 to-gray-200"
          style={{ minHeight: '12rem', maxHeight: '12rem' }}
        >
          {!imageLoaded && (
            <div className="absolute inset-0 flex items-center justify-center">
              <div className="animate-pulse bg-gray-300 w-full h-full rounded-t-2xl"></div>
            </div>
          )}
          <img 
            src={imageUrl} 
            alt={title} 
            className={`w-full h-full object-cover transition-transform duration-500 group-hover:scale-110 ${
              imageLoaded ? 'block' : 'hidden'
            }`}
            onLoad={() => setImageLoaded(true)}
            onError={() => setImageError(true)}
          />
          {/* Overlay dégradé */}
          <div className="absolute inset-0 bg-gradient-to-t from-black/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
        </div>
      )}
      
      {/* Card content */}
      <div className={`flex-1 ${showOverflow ? 'overflow-y-auto' : 'overflow-hidden'} flex flex-col`}>
        {/* Header avec effet de survol */}
        <div className={`px-6 py-4 border-b ${currentVariant.border} ${currentVariant.header} flex-shrink-0 group-hover:bg-opacity-80 transition-colors duration-300`}>
          <h3 className="text-xl font-bold text-gray-900 mb-2 line-clamp-2 group-hover:text-blue-600 transition-colors duration-300" title={title}>
            {title}
          </h3>
          {description && (
            <p className="text-gray-600 text-sm line-clamp-2" style={{
              display: '-webkit-box',
              WebkitLineClamp: 2,
              WebkitBoxOrient: 'vertical',
              overflow: 'hidden'
            }}>{description}</p>
          )}
        </div>
        
        {/* Body avec dégradé de fond */}
        <div className="px-6 py-4 flex-1 bg-gradient-to-b from-white to-gray-50/50">
          <div className={showOverflow ? '' : 'h-full overflow-hidden'}>
            {children}
          </div>
        </div>
        
        {/* Footer avec bordure supérieure */}
        {footer && (
          <div className={`px-6 py-4 border-t ${currentVariant.border} ${currentVariant.footer} flex-shrink-0 bg-white/80 backdrop-blur-sm`}>
            {footer}
          </div>
        )}
      </div>

      {/* Effet de brillance au survol */}
      <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent -skew-x-12 -translate-x-full group-hover:translate-x-full transition-transform duration-1000 pointer-events-none"></div>
    </div>
  );
};

export default Card;