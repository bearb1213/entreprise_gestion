import React, { useState } from 'react';
import Select from './Select';

const MultiSelectInputNumber = ({ 
  label, 
  name, 
  value = [], 
  onChange, 
  options, 
  selectLabel = "Option",
  inputLabel = "Quantité",
  required = false, 
  error = '' 
}) => {
  const [items, setItems] = useState(value);

  const handleChange = (newItems) => {
    setItems(newItems);
    if (onChange) {
      onChange(newItems);
    }
  };

  const addItem = () => {
    handleChange([...items, { selectValue: '', inputValue: '' }]);
  };

  const removeItem = (index) => {
    handleChange(items.filter((_, i) => i !== index));
  };

  const updateItem = (index, field, value) => {
    const newItems = items.map((item, i) => 
      i === index ? { ...item, [field]: value } : item
    );
    handleChange(newItems);
  };

  return (
    <div className="mb-6">
      <div className="flex items-center justify-between mb-3">
        <label className="block text-sm font-medium text-gray-700">
          {label} {required && <span className="text-red-500">*</span>}
        </label>
        <button
          type="button"
          onClick={addItem}
          className="px-3 py-1 bg-blue-500 text-white text-sm rounded hover:bg-blue-600"
        >
          + Ajouter
        </button>
      </div>

      <div className="space-y-3">
        {items.map((item, index) => (
          <div key={index} className="grid grid-cols-1 md:grid-cols-12 gap-3 items-end p-3 border rounded-lg bg-gray-50">
            {/* Select - 8 colonnes sur desktop */}
            <div className="md:col-span-8">
              <Select
                label={`${selectLabel} ${index + 1}`}
                name={`${name}-select-${index}`}
                value={item.selectValue}
                onChange={(e) => updateItem(index, 'selectValue', e.target.value)}
                options={options}
                required={required}
              />
            </div>

            {/* Input Number - 3 colonnes sur desktop */}
            <div className="md:col-span-3">
              <label className="block text-sm font-medium text-gray-700 mb-1">
                {inputLabel}
              </label>
              <input
                type="number"
                min="0"
                step="1"
                value={item.inputValue}
                onChange={(e) => updateItem(index, 'inputValue', e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="0"
              />
            </div>

            {/* Bouton de suppression - 1 colonne sur desktop */}
            <div className="md:col-span-1">
              <button
                type="button"
                onClick={() => removeItem(index)}
                className="w-full px-3 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                title="Supprimer"
              >
                ×
              </button>
            </div>
          </div>
        ))}
      </div>

      {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
    </div>
  );
};

export default MultiSelectInputNumber;