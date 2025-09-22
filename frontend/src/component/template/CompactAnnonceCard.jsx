import React, { useState } from 'react';
import AnnonceCard from './AnnonceCard';

const CompactAnnonceCard = ({ annonce}) => {
  const {
    id,
    statu,
    minAge,
    maxAge,
    nbPosteDispo,
    minExperience,
    metierNom,
    departementNom,
    competences = [],
    langues = [],
    diplomes = []
  } = annonce;
  const [affiche , setAffiche] = useState(false);

  const onClick =() => {
    setAffiche(!affiche);
  }


  return (
    <div 
      className="bg-white rounded-lg shadow-md border border-gray-200 p-4 hover:shadow-lg transition-shadow cursor-pointer"
      onClick={onClick}
    >
      <div className="flex justify-between items-start mb-3">
        <div>
          <h3 className="font-semibold text-gray-800">{metierNom}</h3>
          <p className="text-sm text-gray-600">{departementNom}</p>
        </div>
        <span className={`px-2 py-1 rounded text-xs ${
          statu === 0 ? 'bg-green-100 text-green-800' : 
          statu === 1 ? 'bg-yellow-100 text-yellow-800' : 
          'bg-red-100 text-red-800'
        }`}>
          {nbPosteDispo} poste(s)
        </span>
      </div>

      <div className="flex items-center text-sm text-gray-600 mb-3">
        <span className="mr-3">💼 {minExperience} an{minExperience > 1 ? 's' : ''}+</span>
      </div>

      {competences.length > 0 && (
        <div className="flex flex-wrap gap-1">
          {competences.slice(0, 3).map((competence, index) => (
            <span
              key={index}
              className="bg-gray-100 text-gray-800 px-2 py-1 rounded text-xs"
            >
              {competence}
            </span>
          ))}
          {competences.length > 3 && (
            <span className="text-xs text-gray-500">
              +{competences.length - 3}
            </span>
          )}
        </div>
      )}
      {affiche && <AnnonceCard annonce={annonce} />}

    </div>
  );
};

export default CompactAnnonceCard;