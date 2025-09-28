import React from 'react';
import Checkbox from './Checkbox';

const MultiCheckbox = ({ 
  label, 
  name, 
  value = [], 
  onChange, 
  options, 
  required = false, 
  error = '' 
}) => {
  
  const handleCheckboxChange = (optionValue, isChecked) => {
    let newValues;
    
    if (isChecked) {
      // Ajouter la valeur si elle est cochée
      newValues = [...value, optionValue];
    } else {
      // Retirer la valeur si elle est décochée
      newValues = value.filter(val => val !== optionValue);
    }
    
    // Appeler onChange avec le nouveau tableau
    if (onChange) {
      onChange( newValues );
    }
  };

  // Vérifier si une option est cochée
  const isChecked = (optionValue) => {
    return value.includes(optionValue);
  };

  return (
    <div className="mb-6">
      {/* Label principal */}
      <div className="mb-3">
        <label className="block text-sm font-medium text-gray-700">
          {label} {required && <span className="text-red-500">*</span>}
        </label>
        {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
      </div>

      {/* Groupe de checkboxes */}
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        {options.map((option) => (
          <Checkbox
            key={option.value}
            label={option.label}
            name={`${name}-${option.value}`}
            checked={isChecked(option.value)}
            onChange={(e) => handleCheckboxChange(option.value, e.target.checked)}
          />
        ))}
      </div>

      {/* Affichage des valeurs sélectionnées (optionnel) */}
      {value.length > 0 && (
        <div className="mt-3 p-3 bg-gray-50 rounded-md">
          <p className="text-sm text-gray-600">
            <strong>{value.length}</strong> option(s) sélectionnée(s) :
          </p>
          <div className="mt-1 flex flex-wrap gap-2">
            {value.map((selectedValue, index) => {
              const selectedOption = options.find(opt => opt.value === selectedValue);
              return (
                <span
                  key={index}
                  className="inline-flex items-center px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full"
                >
                  {selectedOption?.label || selectedValue}
                </span>
              );
            })}
          </div>
        </div>
      )}
    </div>
  );
};

export default MultiCheckbox;