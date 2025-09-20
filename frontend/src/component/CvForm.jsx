import React, { useState, useEffect } from 'react';
import axios from 'axios';

const CvForm = () => {
  const [formData, setFormData] = useState({
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
    competencesIds: [],
    languesIds: [],
    diplomesIds: [],
    experiences: []
  });
  
  const [competences, setCompetences] = useState([]);
  const [langues, setLangues] = useState([]);
  const [diplomes, setDiplomes] = useState([]);
  const [metiers, setMetiers] = useState([]);
  const [scores, setScores] = useState({});
  const [loading, setLoading] = useState(true);

  // Base URL pour les API
  const API_BASE_URL = 'http://localhost:8080/api';

  useEffect(() => {
    fetchInitialData();
  }, []);

  const fetchInitialData = async () => {
    try {
      const [compRes, langRes, dipRes, metRes] = await Promise.all([
        axios.get(`${API_BASE_URL}/competences`),
        axios.get(`${API_BASE_URL}/langues`),
        axios.get(`${API_BASE_URL}/diplomes`),
        axios.get(`${API_BASE_URL}/metiers`)
      ]);
      
      setCompetences(compRes.data);
      setLangues(langRes.data);
      setDiplomes(dipRes.data);
      setMetiers(metRes.data);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e, section, field) => {
    if (section) {
      setFormData(prev => ({
        ...prev,
        [section]: {
          ...prev[section],
          [field]: e.target.value
        }
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [field]: e.target.value
      }));
    }
  };

  const handleArrayChange = (arrayName, item) => {
    setFormData(prev => ({
      ...prev,
      [arrayName]: [...prev[arrayName], item]
    }));
  };

  const removeArrayItem = (arrayName, index) => {
    setFormData(prev => ({
      ...prev,
      [arrayName]: prev[arrayName].filter((_, i) => i !== index)
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Structure simplifiée pour l'évaluation
      const evaluationData = {
        personne: formData.personne,
        description: formData.description,
        competencesIds: formData.competencesIds,
        languesIds: formData.languesIds,
        diplomesIds: formData.diplomesIds,
        experiences: formData.experiences.map(exp => ({
          metierId: exp.metier.id,
          nbAnnee: exp.nbAnnee
        }))
      };

      console.log("Données envoyées:", evaluationData);
      
      const response = await axios.post(`${API_BASE_URL}/candidat/evaluate`, evaluationData);
      setScores(response.data.scores);
    } catch (error) {
      console.error('Error evaluating candidat:', error);
      alert('Erreur lors de l\'évaluation du CV: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleSave = async () => {
    try {
      const saveData = {
        personne: formData.personne,
        description: formData.description,
        competencesIds: formData.competencesIds,
        languesIds: formData.languesIds,
        diplomesIds: formData.diplomesIds,
        experiences: formData.experiences.map(exp => ({
          metierId: exp.metier.id,
          nbAnnee: exp.nbAnnee
        }))
      };

      await axios.post(`${API_BASE_URL}/candidat`, saveData);
      alert('Candidat enregistré avec succès!');
      
      // Réinitialiser le formulaire après sauvegarde
      setFormData({
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
        competencesIds: [],
        languesIds: [],
        diplomesIds: [],
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

  if (loading) {
    return <div className="container mx-auto p-6">Chargement...</div>;
  }

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
              value={formData.personne.nom}
              onChange={(e) => handleInputChange(e, 'personne', 'nom')}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              required
            />
          </div>
          <div>
            <label className="block text-gray-700 text-sm font-bold mb-2">Prénom *</label>
            <input
              type="text"
              value={formData.personne.prenom}
              onChange={(e) => handleInputChange(e, 'personne', 'prenom')}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              required
            />
          </div>
          <div>
            <label className="block text-gray-700 text-sm font-bold mb-2">Email *</label>
            <input
              type="email"
              value={formData.personne.email}
              onChange={(e) => handleInputChange(e, 'personne', 'email')}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              required
            />
          </div>
          <div>
            <label className="block text-gray-700 text-sm font-bold mb-2">Téléphone *</label>
            <input
              type="tel"
              value={formData.personne.telephone}
              onChange={(e) => handleInputChange(e, 'personne', 'telephone')}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              required
            />
          </div>
          <div>
            <label className="block text-gray-700 text-sm font-bold mb-2">Date de Naissance *</label>
            <input
              type="date"
              value={formData.personne.dateNaissance}
              onChange={(e) => handleInputChange(e, 'personne', 'dateNaissance')}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              required
            />
          </div>
          <div>
            <label className="block text-gray-700 text-sm font-bold mb-2">Genre *</label>
            <select
              value={formData.personne.genre}
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
              value={formData.personne.ville}
              onChange={(e) => handleInputChange(e, 'personne', 'ville')}
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              required
            />
          </div>
        </div>

        {/* Compétences */}
        <h2 className="text-xl font-semibold mb-4">Compétences</h2>
        <div className="mb-6">
          <select
            onChange={(e) => {
              if (e.target.value) {
                const selectedId = parseInt(e.target.value);
                handleArrayChange('competencesIds', selectedId);
                e.target.value = '';
              }
            }}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          >
            <option value="">Sélectionnez une compétence</option>
            {competences.map(comp => (
              <option key={comp.id} value={comp.id}>{comp.libelle}</option>
            ))}
          </select>
          <div className="mt-2">
            {formData.competencesIds.map((compId, index) => (
              <div key={index} className="flex items-center justify-between bg-gray-100 p-2 rounded mb-2">
                <span>{getLibelleById(compId, competences)}</span>
                <button
                  type="button"
                  onClick={() => removeArrayItem('competencesIds', index)}
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
                const selectedId = parseInt(e.target.value);
                handleArrayChange('languesIds', selectedId);
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
            {formData.languesIds.map((langueId, index) => (
              <div key={index} className="flex items-center justify-between bg-gray-100 p-2 rounded mb-2">
                <span>{getLibelleById(langueId, langues)}</span>
                <button
                  type="button"
                  onClick={() => removeArrayItem('languesIds', index)}
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
                const selectedId = parseInt(e.target.value);
                handleArrayChange('diplomesIds', selectedId);
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
            {formData.diplomesIds.map((diplomeId, index) => (
              <div key={index} className="flex items-center justify-between bg-gray-100 p-2 rounded mb-2">
                <span>{getLibelleById(diplomeId, diplomes)}</span>
                <button
                  type="button"
                  onClick={() => removeArrayItem('diplomesIds', index)}
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
                    const selectedMetier = metiers.find(m => m.id === parseInt(e.target.value));
                    if (selectedMetier) {
                      handleArrayChange('experiences', {
                        metier: selectedMetier,
                        nbAnnee: 1
                      });
                      e.target.value = '';
                    }
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
                  if (formData.experiences.length > 0) {
                    const updatedExperiences = [...formData.experiences];
                    const lastIndex = updatedExperiences.length - 1;
                    updatedExperiences[lastIndex] = {
                      ...updatedExperiences[lastIndex],
                      nbAnnee: parseInt(e.target.value) || 1
                    };
                    setFormData(prev => ({ ...prev, experiences: updatedExperiences }));
                  }
                }}
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                placeholder="Nombre d'années"
              />
            </div>
          </div>
          <div className="mt-2">
            {formData.experiences.map((exp, index) => (
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
            value={formData.description}
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
              setFormData({
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
                competencesIds: [],
                languesIds: [],
                diplomesIds: [],
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
                    style={{ width: `${Math.min(score, 100)}%` }}
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