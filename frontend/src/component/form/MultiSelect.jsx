import React, { useState } from 'react';
import Select from './Select';

const MultiSelectUnique = ({ label, name, value = [], onChange, options, required = false, error = '' }) => {
  const [selectedValues, setSelectedValues] = useState(value);

  // Options disponibles (exclut celles déjà sélectionnées)
  const availableOptions = options.filter(
    option => !selectedValues.includes(option.value)
  );

  const handleAddSelect = () => {
    if (availableOptions.length > 0) {
      const newValues = [...selectedValues, ''];
      setSelectedValues(newValues);
      if (onChange) onChange(newValues);
    }
  };

  const handleRemoveSelect = (index) => {
    const newValues = selectedValues.filter((_, i) => i !== index);
    setSelectedValues(newValues);
    if (onChange) onChange(newValues);
  };

  const handleSelectChange = (index, newValue) => {
    // Vérifier si la valeur n'est pas déjà sélectionnée
    if (selectedValues.includes(newValue) && newValue !== '') {
      return; // Ne pas autoriser les doublons
    }

    const newValues = [...selectedValues];
    newValues[index] = newValue;
    setSelectedValues(newValues);
    if (onChange) onChange(newValues);
  };

  const canAddMore = availableOptions.length > 0 && selectedValues.length < options.length;

  return (
    <div className="mb-6">
      <div className="flex items-center justify-between mb-2">
        <label className="block text-sm font-medium text-gray-700">
          {label} {required && <span className="text-red-500">*</span>}
        </label>
        <button
          type="button"
          onClick={handleAddSelect}
          disabled={!canAddMore}
          className="px-3 py-1 text-sm bg-blue-500 text-white rounded hover:bg-blue-600 disabled:bg-gray-300 disabled:cursor-not-allowed"
        >
          + Ajouter
        </button>
      </div>

      <div className="space-y-3">
        {selectedValues.map((selectedValue, index) => (
          <div key={index} className="flex items-center space-x-2 p-3 border rounded-lg bg-gray-50">
            <div className="flex-1">
              <Select
                label={`Sélection ${index + 1}`}
                name={`${name}-${index}`}
                value={selectedValue}
                onChange={(e) => handleSelectChange(index, e.target.value)}
                options={[
                  { value: '', label: 'Choisissez une option' },
                  ...availableOptions,
                  ...options.filter(opt => opt.value === selectedValue)
                ]}
                required={required}
              />
            </div>
            
            <button
              type="button"
              onClick={() => handleRemoveSelect(index)}
              className="px-2 py-1 text-red-500 hover:text-red-700 font-bold"
              title="Supprimer cette sélection"
            >
              ×
            </button>
          </div>
        ))}
      </div>

      {selectedValues.length === 0 && (
        <div className="text-center text-gray-500 py-4 italic">
          Aucune sélection. Cliquez sur "+ Ajouter" pour commencer.
        </div>
      )}

      {error && <p className="mt-1 text-sm text-red-500">{error}</p>}

      {/* Indicateur des options disponibles */}
      {canAddMore && (
        <p className="mt-2 text-sm text-gray-500">
          {availableOptions.length} option(s) disponible(s)
        </p>
      )}

      {!canAddMore && selectedValues.length > 0 && (
        <p className="mt-2 text-sm text-yellow-600">
          Toutes les options ont été sélectionnées
        </p>
      )}
    </div>
  );
};

export default MultiSelectUnique;