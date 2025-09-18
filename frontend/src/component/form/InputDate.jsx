import React, { useState, useRef, useEffect } from 'react';

const InputDate = ({ 
  label, 
  name, 
  value, 
  onChange, 
  placeholder = '', 
  required = false, 
  error = '' 
}) => {
  const [showPicker, setShowPicker] = useState(false);
  const [internalValue, setInternalValue] = useState(value || '');
  const pickerRef = useRef(null);

  // Mettre à jour la valeur interne quand la prop value change
  useEffect(() => {
    setInternalValue(value || '');
  }, [value]);

  // Fermer le datepicker quand on clique en dehors
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (pickerRef.current && !pickerRef.current.contains(event.target)) {
        setShowPicker(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  // Gérer le changement de date via l'input
  const handleInputChange = (e) => {
    const newValue = e.target.value;
    setInternalValue(newValue);
    
    // Si c'est une date valide, appeler onChange
    if (isValidDate(newValue)) {
      onChange({
        target: {
          name: name,
          value: newValue
        }
      });
    }
  };

  // Gérer la sélection de date via le datepicker
  const handleDateSelect = (dateString) => {
    setInternalValue(dateString);
    onChange({
      target: {
        name: name,
        value: dateString
      }
    });
    setShowPicker(false);
  };

  // Vérifier si une date est valide
  const isValidDate = (dateString) => {
    const regex = /^\d{4}-\d{2}-\d{2}$/;
    if (!dateString.match(regex)) return false;
    
    const date = new Date(dateString);
    const timestamp = date.getTime();
    if (isNaN(timestamp)) return false;
    
    return date.toISOString().slice(0, 10) === dateString;
  };

  // Formater la date pour l'affichage
  const formatDisplayDate = (dateString) => {
    if (!dateString) return placeholder;
    
    const date = new Date(dateString);
    if (isNaN(date.getTime())) return placeholder;
    
    return date.toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  };

  // Obtenir la date d'aujourd'hui
  const getTodayDate = () => {
    return new Date().toISOString().split('T')[0];
  };

  return (
    <div className="mb-4 relative" ref={pickerRef}>
      <label htmlFor={name} className="block text-sm font-medium text-gray-700 mb-1">
        {label} {required && <span className="text-red-500">*</span>}
      </label>
      
      <div className="relative">
        <input
          type="text"
          id={name}
          name={name}
          value={formatDisplayDate(internalValue)}
          onChange={handleInputChange}
          placeholder={placeholder}
          className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${
            error ? 'border-red-500' : 'border-gray-300'
          }`}
          onFocus={() => setShowPicker(true)}
          readOnly
        />
        
        {/* Icône de calendrier */}
        <div 
          className="absolute inset-y-0 right-0 flex items-center pr-3 cursor-pointer"
          onClick={() => setShowPicker(!showPicker)}
        >
          <svg 
            className={`h-5 w-5 ${error ? 'text-red-500' : 'text-gray-400'}`} 
            fill="none" 
            stroke="currentColor" 
            viewBox="0 0 24 24"
          >
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
          </svg>
        </div>
        
        {/* Date picker */}
        {showPicker && (
          <div className="absolute z-10 mt-1 left-0 bg-white border border-gray-200 rounded-md shadow-lg p-3 w-64">
            <input
              type="date"
              value={internalValue}
              onChange={(e) => handleDateSelect(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded-md mb-2"
              max={getTodayDate()}
            />
            <div className="text-xs text-gray-500 text-center">
              Utilisez le sélecteur de date ou saisissez au format AAAA-MM-JJ
            </div>
          </div>
        )}
      </div>
      
      {error && <p className="mt-1 text-sm text-red-500">{error}</p>}
    </div>
  );
};

export default InputDate;