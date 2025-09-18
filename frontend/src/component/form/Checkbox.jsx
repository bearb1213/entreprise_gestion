import React, { useState } from 'react';

// Composant Checkbox
const Checkbox = ({ label, name, checked, onChange, required = false, error = '' }) => {
  return (
    <div className="mb-4">
      <label className="flex items-center">
        <input
          type="checkbox"
          name={name}
          checked={checked}
          onChange={onChange}
          className={`h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded ${
            error ? 'border-red-500' : ''
          }`}
        />
        <span className="ml-2 text-sm text-gray-700">
          {label} {required && <span className="text-red-500">*</span>}
        </span>
      </label>
      {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
    </div>
  );
};

export default Checkbox;