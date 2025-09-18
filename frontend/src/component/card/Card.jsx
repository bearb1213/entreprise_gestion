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
  variant = 'default', // 'default', 'primary', 'success', 'warning', 'danger', 'info'
  border = false,
  shadow = 'medium'
}) => {
  const [imageLoaded, setImageLoaded] = useState(false);
  const [imageError, setImageError] = useState(false);

  // Variant color mappings
  const variantStyles = {
    default: {
      bg: 'bg-white',
      text: 'text-gray-800',
      border: 'border-gray-200',
      header: 'bg-gray-50',
      footer: 'bg-gray-50'
    },
    primary: {
      bg: 'bg-blue-50',
      text: 'text-blue-900',
      border: 'border-blue-200',
      header: 'bg-blue-100',
      footer: 'bg-blue-100'
    },
    success: {
      bg: 'bg-green-50',
      text: 'text-green-900',
      border: 'border-green-200',
      header: 'bg-green-100',
      footer: 'bg-green-100'
    },
    warning: {
      bg: 'bg-yellow-50',
      text: 'text-yellow-900',
      border: 'border-yellow-200',
      header: 'bg-yellow-100',
      footer: 'bg-yellow-100'
    },
    danger: {
      bg: 'bg-red-50',
      text: 'text-red-900',
      border: 'border-red-200',
      header: 'bg-red-100',
      footer: 'bg-red-100'
    },
    info: {
      bg: 'bg-cyan-50',
      text: 'text-cyan-900',
      border: 'border-cyan-200',
      header: 'bg-cyan-100',
      footer: 'bg-cyan-100'
    }
  };

  // Shadow intensity
  const shadowStyles = {
    none: 'shadow-none',
    small: 'shadow-sm',
    medium: 'shadow-md',
    large: 'shadow-lg',
    xl: 'shadow-xl'
  };

  const currentVariant = variantStyles[variant] || variantStyles.default;
  const currentShadow = shadowStyles[shadow] || shadowStyles.medium;

  return (
    <div 
      className={`
        rounded-lg overflow-hidden
        ${currentVariant.bg} 
        ${currentVariant.text}
        ${border ? `${currentVariant.border} border` : 'border-0'}
        ${hoverEffect && 'transition-all duration-200 hover:scale-[1.02]'}
        ${currentShadow}
        ${className}
      `}
      style={{ 
        maxWidth: `${width}px`, 
        maxHeight: `${height}px`,
        display: 'flex',
        flexDirection: 'column'
      }}
    >
      {/* Optional image */}
      {imageUrl && !imageError && (
        <div 
          className="w-full h-48 overflow-hidden relative bg-gray-100"
          style={{ minHeight: '12rem', maxHeight: '12rem' }}
        >
          {!imageLoaded && (
            <div className="absolute inset-0 flex items-center justify-center">
              <div className="animate-pulse bg-gray-200 w-full h-full"></div>
            </div>
          )}
          <img 
            src={imageUrl} 
            alt={title} 
            className={`w-full h-full object-cover ${imageLoaded ? 'block' : 'hidden'}`}
            onLoad={() => setImageLoaded(true)}
            onError={() => setImageError(true)}
          />
        </div>
      )}
      
      {/* Card content with scrolling if needed */}
      <div className={`flex-1 ${showOverflow ? 'overflow-y-auto' : 'overflow-hidden'} flex flex-col`}>
        {/* Header */}
        <div className={`px-4 py-3 border-b ${currentVariant.border} ${currentVariant.header} flex-shrink-0`}>
          <h3 className="text-lg font-semibold truncate" title={title}>{title}</h3>
        </div>
        
        {/* Body */}
        <div className="px-4 py-3 flex-1">
          {description && (
            <p className="mb-3 line-clamp-3" style={{
              display: '-webkit-box',
              WebkitLineClamp: 3,
              WebkitBoxOrient: 'vertical',
              overflow: 'hidden'
            }}>{description}</p>
          )}
          <div className={showOverflow ? '' : 'h-full overflow-hidden'}>
            {children}
          </div>
        </div>
        
        {/* Optional footer */}
        {footer && (
          <div className={`px-4 py-3 border-t ${currentVariant.border} ${currentVariant.footer} flex-shrink-0`}>
            {footer}
          </div>
        )}
      </div>
    </div>
  );
};

export default Card;