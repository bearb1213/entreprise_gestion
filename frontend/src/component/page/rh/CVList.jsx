import React, { useState, useEffect } from 'react';
import CompactCVPreview from '../../template/CompactCVPreview';
import { Link, useNavigate } from 'react-router-dom';

const CVList = () => {
  const [cvs, setCvs] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCity, setSelectedCity] = useState('');
  const [selectedJob, setSelectedJob] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8080/api/candidats", { credentials: "include" })
      .then((response) => response.json())
      .then((data) => {
        if (data.success && data.candidats) {
          setCvs(data.candidats);
        } else {
          console.error("Erreur dans la réponse de l'API:", data);
        }
      })
      .catch((error) => console.error("Erreur fetch CVs :", error));
  }, []);

  console.log("Données reçues:", cvs);

  // Filtrer les CVs
  const filteredCVs = cvs.filter(cv => {
    const personne = cv.personne || {};
    const experiences = cv.experiences || [];
    
    const matchesSearch = 
      `${personne.prenom || ''} ${personne.nom || ''}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
      cv.competences?.some(comp => 
        comp.libelle?.toLowerCase().includes(searchTerm.toLowerCase())
      ) ||
      experiences.some(exp => 
        exp.metier?.libelle?.toLowerCase().includes(searchTerm.toLowerCase())
      );

    const matchesCity = selectedCity ? personne.ville === selectedCity : true;
    const matchesJob = selectedJob ? 
      experiences.some(exp => exp.metier?.libelle === selectedJob) : true;

    return matchesSearch && matchesCity && matchesJob;
  });

  // Options pour les filtres
  const cities = [...new Set(cvs.map(cv => cv.personne?.ville).filter(Boolean))];
  const jobs = [...new Set(cvs.flatMap(cv => 
    (cv.experiences || []).map(exp => exp.metier?.libelle).filter(Boolean)
  ))];

  const handleCVClick = (cvId) => {
    navigate(`/cv/${cvId}`);
  };

  const handleAddCV = () => {
    navigate("/profile");
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8 ">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Liste des CVs</h1>
          <p className="text-gray-600">Découvrez les profils talentueux de notre communauté</p>
        </div>

        {/* Filtres et recherche */}
        <div className="bg-white rounded-lg shadow p-6 mb-8">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            {/* Barre de recherche */}
            <div className="md:col-span-2">
              <label htmlFor="search" className="block text-sm font-medium text-gray-700 mb-1">
                Rechercher
              </label>
              <input
                type="text"
                id="search"
                placeholder="Nom, compétence, métier..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            {/* Filtre par ville */}
            <div>
              <label htmlFor="city" className="block text-sm font-medium text-gray-700 mb-1">
                Ville
              </label>
              <select
                id="city"
                value={selectedCity}
                onChange={(e) => setSelectedCity(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Toutes les villes</option>
                {cities.map(city => (
                  <option key={city} value={city}>{city}</option>
                ))}
              </select>
            </div>

            {/* Filtre par métier */}
            <div>
              <label htmlFor="job" className="block text-sm font-medium text-gray-700 mb-1">
                Métier
              </label>
              <select
                id="job"
                value={selectedJob}
                onChange={(e) => setSelectedJob(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Tous les métiers</option>
                {jobs.map(job => (
                  <option key={job} value={job}>{job}</option>
                ))}
              </select>
            </div>
          </div>
        </div>

        {/* Statistiques */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
          <div className="bg-white rounded-lg shadow p-4 text-center">
            <div className="text-2xl font-bold text-blue-600">{cvs.length}</div>
            <div className="text-gray-600">CVs au total</div>
          </div>
          <div className="bg-white rounded-lg shadow p-4 text-center">
            <div className="text-2xl font-bold text-green-600">{filteredCVs.length}</div>
            <div className="text-gray-600">Résultats filtrés</div>
          </div>
          <div className="bg-white rounded-lg shadow p-4 text-center">
            <div className="text-2xl font-bold text-purple-600">{cities.length}</div>
            <div className="text-gray-600">Villes différentes</div>
          </div>
        </div>

        {/* Liste des CVs */}
        {filteredCVs.length === 0 ? (
          <div className="bg-white rounded-lg shadow p-8 text-center">
            <div className="text-gray-400 text-6xl mb-4">🔍</div>
            <h3 className="text-lg font-medium text-gray-900 mb-2">Aucun résultat</h3>
            <p className="text-gray-600">Aucun CV ne correspond à vos critères de recherche.</p>
          </div>
        ) : (
          <>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mb-8">
              {filteredCVs.map(cv => (
                <div 
                  key={cv.id} 
                  onClick={() => handleCVClick(cv.id)}
                  className="cursor-pointer hover:scale-105 transition-transform duration-200"
                >
                  <CompactCVPreview
                    cvData={cv}
                  />
                </div>
              ))}
            </div>

            {/* Pagination (exemple) */}
            <div className="flex justify-center items-center space-x-2">
              <button className="px-3 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50">
                Précédent
              </button>
              <span className="px-3 py-2 text-gray-700">Page 1 sur 2</span>
              <button className="px-3 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50">
                Suivant
              </button>
            </div>
          </>
        )}

        {/* Bouton d'action */}
        <div className="fixed bottom-8 right-8">
          <button
            onClick={handleAddCV}
            className="bg-blue-600 text-white p-4 rounded-full shadow-lg hover:bg-blue-700 transition-colors flex items-center justify-center"
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4v16m8-8H4" />
            </svg>
          </button>
        </div>
      </div>
    </div>
  );
};

export default CVList;