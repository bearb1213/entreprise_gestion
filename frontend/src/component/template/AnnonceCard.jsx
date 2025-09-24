import React from 'react';
import { Link } from 'react-router-dom';

const AnnonceCard = ({ annonce }) => {
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

  // Couleur selon le statut
  const getStatusColor = (status) => {
    switch(status) {
      case 0: return 'bg-green-100 text-green-800'; // Ouvert
      case 1: return 'bg-yellow-100 text-yellow-800'; // En cours
      case 2: return 'bg-red-100 text-red-800'; // Fermé
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const getStatusText = (status) => {
    switch(status) {
      case 0: return '🟢 Ouvert';
      case 1: return '🔴 Fermé';
      default: return '⚪ Inconnu';
    }
  };

  return (
    <div className="bg-white rounded-xl shadow-md border border-gray-200 overflow-hidden hover:shadow-lg transition-shadow">
      {/* Header avec statut */}
      <div className="bg-gradient-to-r from-blue-600 to-blue-800 text-white p-4">
        <div className="flex justify-between items-center">
          <h3 className="text-xl font-bold">{metierNom}</h3>
          <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(statu)}`}>
            {getStatusText(statu)}
          </span>
        </div>
        <p className="text-blue-100 mt-1">{departementNom}</p>
      </div>

      {/* Informations principales */}
      <div className="p-4 space-y-3">
        {/* Postes disponibles */}
        <div className="flex items-center justify-between">
          <span className="text-gray-600">Postes disponibles</span>
          <span className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm font-medium">
            {nbPosteDispo} poste{nbPosteDispo > 1 ? 's' : ''}
          </span>
        </div>

        {/* Âge et expérience */}
        <div className="grid grid-cols-2 gap-4">
          <div className="flex items-center">
            <span className="text-gray-500 mr-2">🎂</span>
            <span className="text-sm">{minAge} - {maxAge} ans</span>
          </div>
          <div className="flex items-center">
            <span className="text-gray-500 mr-2">💼</span>
            <span className="text-sm">{minExperience} an{minExperience > 1 ? 's' : ''} min</span>
          </div>
        </div>

        {/* Compétences */}
        {competences.length > 0 && (
          <div>
            <h4 className="font-semibold text-gray-700 mb-2">Compétences requises</h4>
            <div className="flex flex-wrap gap-2">
              {competences.map((competence, index) => (
                <span
                  key={index}
                  className="bg-gray-100 text-gray-800 px-3 py-1 rounded-full text-xs font-medium"
                >
                  {competence}
                </span>
              ))}
            </div>
          </div>
        )}

        {/* Langues */}
        {langues.length > 0 && (
          <div>
            <h4 className="font-semibold text-gray-700 mb-2">Langues</h4>
            <div className="flex flex-wrap gap-2">
              {langues.map((langue, index) => (
                <span
                  key={index}
                  className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-xs font-medium"
                >
                  {langue}
                </span>
              ))}
            </div>
          </div>
        )}

        {/* Diplômes */}
        {diplomes.length > 0 && (
          <div>
            <h4 className="font-semibold text-gray-700 mb-2">Diplômes</h4>
            <div className="space-y-1">
              {diplomes.map((diplome, index) => (
                <div key={index} className="flex items-center">
                  <span className="text-green-500 mr-2">🎓</span>
                  <span className="text-sm text-gray-600">{diplome}</span>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Footer avec actions */}
      <div className="bg-gray-50 p-4 border-t border-gray-200">
        <div className="flex justify-between items-center">
          <span className="text-sm text-gray-500">ID: #{id}</span>
          <div className="flex space-x-2">
            <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors text-sm">
              Postuler
            </button>
            <Link to={`/CvList/${id}`} >
            <button className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-colors text-sm">
              Détails
            </button>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AnnonceCard;