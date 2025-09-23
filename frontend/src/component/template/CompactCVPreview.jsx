import React from "react";
import { useNavigate } from "react-router-dom";

const CompactCVPreview = ({ cvData, onClick }) => {
  if (!cvData) return null;

  // Extraction des données avec la nouvelle structure
  const { 
    personne = {}, 
    competences = [], 
    experiences = [] 
  } = cvData;

  const { 
    nom, 
    prenom, 
    image, 
    ville 
  } = personne;

  return (
    <div 
      className="bg-white rounded-lg shadow-md border border-gray-200 p-4 hover:shadow-lg transition-shadow cursor-pointer"
      onClick={onClick}
    >
      <div className="flex items-center space-x-3">
        {image && (
          <img
            src={image}
            alt={`${prenom} ${nom}`}
            className="w-12 h-12 rounded-full object-cover border-2 border-gray-300"
          />
        )}
        <div className="flex-1 min-w-0">
          <h3 className="font-semibold text-gray-800 truncate">
            {prenom} {nom}
          </h3>
          <p className="text-sm text-gray-600 truncate">
            {experiences?.[0]?.metier?.libelle || 'Sans emploi'}
          </p>
          <p className="text-xs text-gray-500">{ville}</p>
        </div>
      </div>

      {competences.length > 0 && (
        <div className="mt-3">
          <div className="flex flex-wrap gap-1">
            {competences.slice(0, 3).map((competence) => (
              <span
                key={competence.id}
                className="bg-blue-100 text-blue-800 px-2 py-1 rounded text-xs"
              >
                {competence.libelle}
              </span>
            ))}
            {competences.length > 3 && (
              <span className="text-xs text-gray-500">
                +{competences.length - 3}
              </span>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default CompactCVPreview;