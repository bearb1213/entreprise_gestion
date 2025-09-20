import React, { useState, useEffect } from 'react';
import axios from 'axios';

const CvForm = () => {
  const [formData, setFormData] = useState({
    nom: '',
    prenom: '',
    email: '',
    telephone: '',
    dateNaissance: '',
    genre: '',
    ville: '',
    description: '',
    competences: [],
    langues: [],
    diplomes: [],
    experiences: []
  });
  
  const [competences, setCompetences] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [scores, setScores] = useState({});

  const API_BASE_URL = 'http://localhost:8080/api';

  useEffect(() => {
    fetchCompetences();
  }, []);

  const fetchCompetences = async () => {
    try {
      setError(null);
      const response = await axios.get(`${API_BASE_URL}/competences`);
      
      if (response.data && Array.isArray(response.data)) {
        setCompetences(response.data);
      } else {
        setError('Format de données invalide');
      }
    } catch (error) {
      console.error('Error fetching competences:', error);
      setError('Erreur lors du chargement des compétences');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleCompetenceChange = (e) => {
    if (e.target.value) {
      const selectedId = parseInt(e.target.value);
      const selectedCompetence = competences.find(c => c.id === selectedId);
      
      if (selectedCompetence) {
        setFormData(prev => ({
          ...prev,
          competences: [...prev.competences, selectedCompetence]
        }));
        e.target.value = '';
      }
    }
  };

  const removeCompetence = (index) => {
    setFormData(prev => ({
      ...prev,
      competences: prev.competences.filter((_, i) => i !== index)
    }));
  };

  // ... rest of your code

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">Formulaire de Candidature</h1>
      
      {/* Compétences */}
      <h2 className="text-xl font-semibold mb-4">Compétences</h2>
      <div className="mb-6">
        <select
          onChange={handleCompetenceChange}
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          disabled={loading}
        >
          <option value="">Sélectionnez une compétence</option>
          {loading ? (
            <option>Chargement...</option>
          ) : (
            competences.map(comp => (
              <option key={comp.id} value={comp.id}>
                {comp.libelle}
              </option>
            ))
          )}
        </select>
        
        {error && (
          <div className="text-red-500 mt-2">{error}</div>
        )}
        
        <div className="mt-2">
          {formData.competences.map((comp, index) => (
            <div key={index} className="flex items-center justify-between bg-gray-100 p-2 rounded mb-2">
              <span>{comp.libelle}</span>
              <button
                type="button"
                onClick={() => removeCompetence(index)}
                className="text-red-500 hover:text-red-700 font-bold"
              >
                ×
              </button>
            </div>
          ))}
        </div>
      </div>

      {/* ... rest of your form */}
    </div>
  );
};

export default CvForm;