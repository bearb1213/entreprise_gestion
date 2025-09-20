import React, { useState, useEffect } from 'react';
import axios from 'axios';

const CvForm = () => {
  const [candidat, setCandidat] = useState({
    personne: {
      nom: '',
      prenom: '',
      email: '',
      dateNaissance: '',
      genre: '',
      ville: '',
      telephone: ''
    },
    description: '',
    competences: [],
    langues: [],
    diplomes: [],
    experiences: []
  });
  
  const [competences, setCompetences] = useState([]);
  const [langues, setLangues] = useState([]);
  const [diplomes, setDiplomes] = useState([]);
  const [metiers, setMetiers] = useState([]);
  const [scores, setScores] = useState({});
  const [besoins, setBesoins] = useState([]);
  const [loadingCompetences, setLoadingCompetences] = useState(true); // État de chargement

  // Base URL pour les API
  const API_BASE_URL = 'http://localhost:8080/api';

  useEffect(() => {
    fetchInitialData();
  }, []);

  const fetchInitialData = async () => {
    try {
      const [compRes, langRes, dipRes, metRes, besRes] = await Promise.all([
        axios.get(`${API_BASE_URL}/competences`),
        axios.get(`${API_BASE_URL}/langues`),
        axios.get(`${API_BASE_URL}/diplomes`),
        axios.get(`${API_BASE_URL}/metiers`),
        axios.get(`${API_BASE_URL}/besoins/1`)
      ]);
      
      setCompetences(compRes.data);
      setLangues(langRes.data);
      setDiplomes(dipRes.data);
      setMetiers(metRes.data);
      setBesoins(besRes.data);
      setLoadingCompetences(false); // Fin du chargement
    } catch (error) {
      console.error('Error fetching data:', error);
      setLoadingCompetences(false); // Fin du chargement même en cas d'erreur
    }
  };

  // Removed duplicate declaration of getLibelleById


  const handleInputChange = (e, section, field) => {
    if (section) {
      setCandidat(prev => ({
        ...prev,
        [section]: {
          ...prev[section],
          [field]: e.target.value
        }
      }));
    } else {
      setCandidat(prev => ({
        ...prev,
        [field]: e.target.value
      }));
    }
  };

  const handleArrayChange = (arrayName, item) => {
    setCandidat(prev => ({
      ...prev,
      [arrayName]: [...prev[arrayName], item]
    }));
  };

  const removeArrayItem = (arrayName, index) => {
    setCandidat(prev => ({
      ...prev,
      [arrayName]: prev[arrayName].filter((_, i) => i !== index)
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(`${API_BASE_URL}/candidat/evaluate`, {
        personne: {
          nom: candidat.personne.nom,
          prenom: candidat.personne.prenom,
          email: candidat.personne.email,
          dateNaissance: candidat.personne.dateNaissance,
          genre: candidat.personne.genre ? parseInt(candidat.personne.genre) : null,
          ville: candidat.personne.ville,
          telephone: candidat.personne.telephone
        },
        description: candidat.description,
        competences: candidat.competences.map(comp => ({ 
          id: comp.competence.id,
          libelle: competences.find(c => c.id === comp.competence.id)?.libelle || ''
        })),
        langues: candidat.langues.map(langue => ({ 
          id: langue.langue.id,
          libelle: langues.find(l => l.id === langue.langue.id)?.libelle || ''
        })),
        diplomes: candidat.diplomes.map(diplome => ({ 
          id: diplome.diplomeFiliere.id,
          libelle: diplomes.find(d => d.id === diplome.diplomeFiliere.id)?.libelle || ''
        })),
        experiences: candidat.experiences.map(exp => ({
          nbAnnee: exp.nbAnnee,
          metier: { 
            id: exp.metier.id,
            libelle: metiers.find(m => m.id === exp.metier.id)?.libelle || ''
          }
        }))
      });
      setScores(response.data.scores);
    } catch (error) {
      console.error('Error evaluating candidat:', error);
      alert('Erreur lors de l\'évaluation du CV: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleSave = async () => {
    try {
      await axios.post(`${API_BASE_URL}/candidat`, {
        personne: {
          nom: candidat.personne.nom,
          prenom: candidat.personne.prenom,
          email: candidat.personne.email,
          dateNaissance: candidat.personne.dateNaissance,
          genre: candidat.personne.genre ? parseInt(candidat.personne.genre) : null,
          ville: candidat.personne.ville,
          telephone: candidat.personne.telephone
        },
        description: candidat.description,
        competences: candidat.competences.map(comp => ({ id: comp.competence.id })),
        langues: candidat.langues.map(langue => ({ id: langue.langue.id })),
        diplomes: candidat.diplomes.map(diplome => ({ id: diplome.diplomeFiliere.id })),
        experiences: candidat.experiences.map(exp => ({
          nbAnnee: exp.nbAnnee,
          metier: { id: exp.metier.id }
        }))
      });
      alert('Candidat enregistré avec succès!');
      
      // Réinitialiser le formulaire après sauvegarde
      setCandidat({
        personne: {
          nom: '',
          prenom: '',
          email: '',
          dateNaissance: '',
          genre: '',
          ville: '',
          telephone: ''
        },
        description: '',
        competences: [],
        langues: [],
        diplomes: [],
        experiences: []
      });
      setScores({});
    } catch (error) {
      console.error('Error saving candidat:', error);
      alert('Erreur lors de la sauvegarde: ' + (error.response?.data?.message || error.message));
    }
  };

  // Fonction pour obtenir le libellé d'un élément par son ID
  const getLibelleById = (id, array) => {
    const item = array.find(item => item.id === id);
    return item ? item.libelle : 'Inconnu';
  };


  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">Formulaire de Candidature</h1>
      
      <form onSubmit={handleSubmit} className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
        {/* Informations personnelles */}
        <h2 className="text-xl font-semibold mb-4">Informations Personnelles</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
          <div>
            <label className="block text-gray-700 text-sm font-bold mb-2">Nom *</label>
            <input
              type="text"
              value={candidat.personne.nom}
              onChange={(e) => handleInputChange(e, 'personne', 'nom')}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              required
            />
          </div>
          <div>
            <label className="block text-gray-700 text-sm font-bold mb-2">Prénom *</label>
            <input
              type="text"
              value={candidat.personne.prenom}
              onChange={(e) => handleInputChange(e, 'personne', 'prenom')}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              required
            />
          </div>
          <div>
            <label className="block text-gray-700 text-sm font-bold mb-2">Email *</label>
            <input
              type="email"
              value={candidat.personne.email}
              onChange={(e) => handleInputChange(e, 'personne', 'email')}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              required
            />
          </div>
          <div>
            <label className="block text-gray-700 text-sm font-bold mb-2">Téléphone *</label>
            <input
              type="tel"
              value={candidat.personne.telephone}
              onChange={(e) => handleInputChange(e, 'personne', 'telephone')}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              required
            />
          </div>
          <div>
            <label className="block text-gray-700 text-sm font-bold mb-2">Date de Naissance *</label>
            <input
              type="date"
              value={candidat.personne.dateNaissance}
              onChange={(e) => handleInputChange(e, 'personne', 'dateNaissance')}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              required
            />
          </div>
          <div>
            <label className="block text-gray-700 text-sm font-bold mb-2">Genre *</label>
            <select
              value={candidat.personne.genre}
              onChange={(e) => handleInputChange(e, 'personne', 'genre')}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              required
            >
              <option value="">Sélectionnez</option>
              <option value="1">Homme</option>
              <option value="2">Femme</option>
              <option value="3">Autre</option>
            </select>
          </div>
          <div>
            <label className="block text-gray-700 text-sm font-bold mb-2">Ville *</label>
            <input
              type="text"
              value={candidat.personne.ville}
              onChange={(e) => handleInputChange(e, 'personne', 'ville')}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              required
            />
          </div>
        </div>

        <h2 className="text-xl font-semibold mb-4">Compétences</h2>
        <div className="mb-6">
          <select
            onChange={(e) => {
              if (e.target.value) {
                const selectedId = parseInt(e.target.value);
                const selectedCompetence = competences.find(comp => comp.id === selectedId);
                
                if (selectedCompetence) {
                  handleArrayChange('competences', { 
                    competence: { 
                      id: selectedCompetence.id,
                      libelle: selectedCompetence.libelle
                    } 
                  });
                  e.target.value = '';
                }
              }
            }}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            disabled={loadingCompetences}
          >
            <option value="">{loadingCompetences ? 'Chargement...' : 'Sélectionnez une compétence'}</option>
            {competences.map(comp => (
              <option key={comp.id} value={comp.id}>{comp.libelle}</option>
            ))}
          </select>
          <div className="mt-2">
            {candidat.competences.map((comp, index) => (
              <div key={index} className="flex items-center justify-between bg-gray-100 p-2 rounded mb-2">
                <span>{comp.competence.libelle}</span>
                <button
                  type="button"
                  onClick={() => removeArrayItem('competences', index)}
                  className="text-red-500 hover:text-red-700 font-bold"
                >
                  ×
                </button>
              </div>
            ))}
          </div>
        </div>


        {/* Langues */}
        <h2 className="text-xl font-semibold mb-4">Langues</h2>
        <div className="mb-6">
          <select
            onChange={(e) => {
              if (e.target.value) {
                handleArrayChange('langues', { 
                  langue: { 
                    id: parseInt(e.target.value),
                    libelle: getLibelleById(parseInt(e.target.value), langues)
                  } 
                });
                e.target.value = '';
              }
            }}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          >
            <option value="">Sélectionnez une langue</option>
            {langues.map(langue => (
              <option key={langue.id} value={langue.id}>{langue.libelle}</option>
            ))}
          </select>
          <div className="mt-2">
            {candidat.langues.map((langue, index) => (
              <div key={index} className="flex items-center justify-between bg-gray-100 p-2 rounded mb-2">
                <span>{langue.langue.libelle}</span>
                <button
                  type="button"
                  onClick={() => removeArrayItem('langues', index)}
                  className="text-red-500 hover:text-red-700 font-bold"
                >
                  ×
                </button>
              </div>
            ))}
          </div>
        </div>

        {/* Diplômes */}
        <h2 className="text-xl font-semibold mb-4">Diplômes</h2>
        <div className="mb-6">
          <select
            onChange={(e) => {
              if (e.target.value) {
                handleArrayChange('diplomes', { 
                  diplomeFiliere: { 
                    id: parseInt(e.target.value),
                    libelle: getLibelleById(parseInt(e.target.value), diplomes)
                  } 
                });
                e.target.value = '';
              }
            }}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          >
            <option value="">Sélectionnez un diplôme</option>
            {diplomes.map(diplome => (
              <option key={diplome.id} value={diplome.id}>{diplome.libelle}</option>
            ))}
          </select>
          <div className="mt-2">
            {candidat.diplomes.map((diplome, index) => (
              <div key={index} className="flex items-center justify-between bg-gray-100 p-2 rounded mb-2">
                <span>{diplome.diplomeFiliere.libelle}</span>
                <button
                  type="button"
                  onClick={() => removeArrayItem('diplomes', index)}
                  className="text-red-500 hover:text-red-700 font-bold"
                >
                  ×
                </button>
              </div>
            ))}
          </div>
        </div>

        {/* Expériences */}
        <h2 className="text-xl font-semibold mb-4">Expériences Professionnelles</h2>
        <div className="mb-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
            <div>
              <label className="block text-gray-700 text-sm font-bold mb-2">Métier</label>
              <select
                onChange={(e) => {
                  if (e.target.value) {
                    handleArrayChange('experiences', {
                      metier: { 
                        id: parseInt(e.target.value),
                        libelle: getLibelleById(parseInt(e.target.value), metiers)
                      },
                      nbAnnee: 1
                    });
                    e.target.value = '';
                  }
                }}
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              >
                <option value="">Sélectionnez un métier</option>
                {metiers.map(metier => (
                  <option key={metier.id} value={metier.id}>{metier.libelle}</option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-gray-700 text-sm font-bold mb-2">Années d'expérience</label>
              <input
                type="number"
                min="1"
                max="50"
                onChange={(e) => {
                  if (candidat.experiences.length > 0) {
                    const updatedExperiences = [...candidat.experiences];
                    const lastIndex = updatedExperiences.length - 1;
                    updatedExperiences[lastIndex] = {
                      ...updatedExperiences[lastIndex],
                      nbAnnee: parseInt(e.target.value) || 1
                    };
                    setCandidat(prev => ({ ...prev, experiences: updatedExperiences }));
                  }
                }}
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                placeholder="Nombre d'années"
              />
            </div>
          </div>
          <div className="mt-2">
            {candidat.experiences.map((exp, index) => (
              <div key={index} className="flex items-center justify-between bg-gray-100 p-2 rounded mb-2">
                <span>{exp.metier.libelle} - {exp.nbAnnee} an(s)</span>
                <button
                  type="button"
                  onClick={() => removeArrayItem('experiences', index)}
                  className="text-red-500 hover:text-red-700 font-bold"
                >
                  ×
                </button>
              </div>
            ))}
          </div>
        </div>

        {/* Description */}
        <div className="mb-6">
          <label className="block text-gray-700 text-sm font-bold mb-2">Description / Présentation</label>
          <textarea
            value={candidat.description}
            onChange={(e) => handleInputChange(e, null, 'description')}
            rows="4"
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            placeholder="Décrivez votre parcours, vos motivations..."
          />
        </div>

        {/* Buttons */}
        <div className="flex items-center justify-between gap-4">
          <button
            type="submit"
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-6 rounded focus:outline-none focus:shadow-outline transition duration-300"
          >
            Évaluer le CV
          </button>
          <button
            type="button"
            onClick={handleSave}
            className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-6 rounded focus:outline-none focus:shadow-outline transition duration-300"
          >
            Sauvegarder
          </button>
          <button
            type="button"
            onClick={() => {
              setCandidat({
                personne: {
                  nom: '',
                  prenom: '',
                  email: '',
                  dateNaissance: '',
                  genre: '',
                  ville: '',
                  telephone: ''
                },
                description: '',
                competences: [],
                langues: [],
                diplomes: [],
                experiences: []
              });
              setScores({});
            }}
            className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-6 rounded focus:outline-none focus:shadow-outline transition duration-300"
          >
            Réinitialiser
          </button>
        </div>
      </form>

      {/* Résultats d'évaluation */}
      {Object.keys(scores).length > 0 && (
        <div className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
          <h2 className="text-xl font-semibold mb-4">Résultats de l'Évaluation</h2>
          <div className="space-y-4">
            {Object.entries(scores).map(([poste, score]) => (
              <div key={poste} className="border-l-4 border-blue-500 pl-4 py-2">
                <div className="flex justify-between items-center mb-2">
                  <span className="font-medium text-gray-800">{poste}</span>
                  <span className="font-bold text-blue-600">{score} points</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2.5">
                  <div
                    className="bg-blue-600 h-2.5 rounded-full transition-all duration-500"
                    style={{ width: `${Math.min((score / 100) * 100, 100)}%` }}
                  ></div>
                </div>
                <div className="text-sm text-gray-600 mt-1">
                  {score >= 80 ? 'Excellent match!' : 
                   score >= 60 ? 'Bon profil' : 
                   score >= 40 ? 'Profil acceptable' : 
                   'Profil non conforme'}
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default CvForm;