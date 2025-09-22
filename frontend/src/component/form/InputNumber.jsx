import React from 'react';

const InputNumber = ({
  label,
  name,
  value,
  onChange,
  placeholder,
  required = false,
  disabled = false,
  error = '',
  min,
  max,
  step = 1,
  className = '',
  helperText = '',
}) => {
  const handleChange = (e) => {
    const inputValue = e.target.value;
    
    // Permettre la valeur vide
    if (inputValue === '') {
      onChange({
        ...e,
        target: {
          ...e.target,
          name,
          value: '',
        },
      });
      return;
    }

    // Validation basique pour s'assurer que c'est un nombre
    if (!/^-?\d*\.?\d*$/.test(inputValue)) {
      return;
    }

    // Convertir en nombre
    const numericValue = inputValue.includes('.') ? parseFloat(inputValue) : parseInt(inputValue, 10);

    // Vérifier les limites min/max
    if (min !== undefined && numericValue < min) {
      return;
    }

    if (max !== undefined && numericValue > max) {
      return;
    }

    // Appeler onChange avec la valeur numérique
    onChange({
      ...e,
      target: {
        ...e.target,
        name,
        value: numericValue,
      },
    });
  };

  const handleBlur = (e) => {
    const inputValue = e.target.value;
    
    // Si vide et requis, garder vide ou mettre la valeur min
    if (inputValue === '') {
      if (required && min !== undefined) {
        onChange({
          ...e,
          target: {
            ...e.target,
            name,
            value: min,
          },
        });
      }
      return;
    }

    // Arrondir selon le step
    if (step === 1) {
      const roundedValue = Math.round(parseFloat(inputValue));
      onChange({
        ...e,
        target: {
          ...e.target,
          name,
          value: roundedValue,
        },
      });
    }
  };

  const increment = () => {
    const currentValue = value || 0;
    const newValue = max !== undefined ? Math.min(currentValue + step, max) : currentValue + step;
    onChange({
      target: {
        name,
        value: newValue,
      },
    });
  };

  const decrement = () => {
    const currentValue = value || 0;
    const newValue = min !== undefined ? Math.max(currentValue - step, min) : currentValue - step;
    onChange({
      target: {
        name,
        value: newValue,
      },
    });
  };

  return (
    <div className={`mb-4 ${className}`}>
      {label && (
        <label htmlFor={name} className="block text-sm font-medium text-gray-700 mb-1">
          {label}
          {required && <span className="text-red-500 ml-1">*</span>}
        </label>
      )}

      <div className="relative rounded-md shadow-sm">
        <button
          type="button"
          onClick={decrement}
          disabled={disabled || (min !== undefined && value <= min)}
          className="absolute left-0 top-0 bottom-0 px-3 rounded-l-md border border-r-0 border-gray-300 bg-gray-50 text-gray-500 hover:bg-gray-100 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          -
        </button>

        <input
          type="text"
          id={name}
          name={name}
          value={value === undefined || value === null ? '' : value}
          onChange={handleChange}
          onBlur={handleBlur}
          placeholder={placeholder}
          required={required}
          disabled={disabled}
          min={min}
          max={max}
          step={step}
          className={`block w-full pl-12 pr-12 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 disabled:bg-gray-100 disabled:cursor-not-allowed ${
            error ? 'border-red-500 focus:ring-red-500 focus:border-red-500' : ''
          }`}
        />

        <button
          type="button"
          onClick={increment}
          disabled={disabled || (max !== undefined && value >= max)}
          className="absolute right-0 top-0 bottom-0 px-3 rounded-r-md border border-l-0 border-gray-300 bg-gray-50 text-gray-500 hover:bg-gray-100 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          +
        </button>
      </div>

      {error && (
        <p className="mt-1 text-sm text-red-600">{error}</p>
      )}

      {helperText && !error && (
        <p className="mt-1 text-sm text-gray-500">{helperText}</p>
      )}
    </div>
  );
};

export default InputNumber;