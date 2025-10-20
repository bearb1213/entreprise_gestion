import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import CalendrierEntretiens from './CalendrierEntretiens';

const FormulaireEntretien = () => {
  const { candidatureId } = useParams();
  const navigate = useNavigate();
  const [rhList, setRhList] = useState([]);
  const [selectedRh, setSelectedRh] = useState('');
  const [selectedDateTime, setSelectedDateTime] = useState(null);
  const [duree, setDuree] = useState(60); // 60 minutes par défaut
  const [loading, setLoading] = useState(false);

  // Récupérer la liste des RH
  const fetchRHList = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/utilisateur/rh' , {credentials: 'include'});
      if (response.ok) {
        const data = await response.json();
        setRhList(data.utilisateurs || []);
        if (data.utilisateurs && data.utilisateurs.length > 0) {
          setSelectedRh(data.utilisateurs[0].id.toString());
        }
      }
    } catch (error) {
      console.error('Erreur lors du chargement des RH:', error);
    }
  };

  useEffect(() => {
    fetchRHList();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!selectedRh || !selectedDateTime) {
      alert('Veuillez sélectionner un RH, une date et une heure');
      return;
    }

    setLoading(true);
    try {
      const dateTimeDebut = new Date(selectedDateTime);
      const dateTimeFin = new Date(dateTimeDebut.getTime() + duree * 60000);

      // Formater les dates selon le format attendu par le backend
      const formatDateForBackend = (date) => {
        return date.toISOString()
          .replace(/\.\d{3}Z$/, '') // Supprimer les millisecondes et le Z
          .replace('Z', ''); // Au cas où
      };

      const entretienData = {
        dateHeureDebut: formatDateForBackend(dateTimeDebut),
        dateHeureFin: formatDateForBackend(dateTimeFin),
        rhId: parseInt(selectedRh),
        candidatureId: parseInt(candidatureId)
      };

      console.log("Data envoyée:", entretienData);

      const response = await fetch('http://localhost:8080/api/entretiens', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(entretienData)
      });

      console.log("Statut de la réponse:", response.status);

      if (!response.ok) {
        const errorData = await response.json();
        console.error("Erreur du backend:", errorData);
        throw new Error(errorData.error || `Erreur ${response.status}`);
      }

      const result = await response.json();
      console.log("Réponse du serveur:", result);

      alert('Entretien planifié avec succès !');
      navigate(-1);

    } catch (error) {
      console.error('Erreur détaillée:', error);
      alert('Erreur lors de la planification de l\'entretien: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8">
      <div className="max-w-6xl mx-auto px-4">
        
        {/* En-tête */}
        <div className="flex items-center justify-between mb-8">
          <button
            onClick={() => navigate(-1)}
            className="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-all duration-200 flex items-center gap-2"
          >
            ← Retour
          </button>
          <h1 className="text-3xl font-bold text-gray-900 bg-gradient-to-r from-blue-600 to-indigo-700 bg-clip-text text-transparent">
            Planifier un Entretien
          </h1>
          <div className="w-20"></div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          
          {/* Formulaire */}
          <div className="bg-white rounded-2xl shadow-lg p-6">
            <h2 className="text-xl font-semibold text-gray-800 mb-6">
              Configuration
            </h2>

            <form onSubmit={handleSubmit} className="space-y-6">
              
              {/* Sélection du RH */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  📋 Responsable RH
                </label>
                <select
                  value={selectedRh}
                  onChange={(e) => setSelectedRh(e.target.value)}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-200"
                  required
                >
                  <option value="">Sélectionnez un RH</option>
                  {rhList.map((rh) => (
                    <option key={rh.id} value={rh.id}>
                      {rh.login} {rh.departement ? `- ${rh.departement.nom}` : ''}
                    </option>
                  ))}
                </select>
              </div>

              {/* Date et heure sélectionnées */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  📅 Date et heure sélectionnées
                </label>
                <div className="px-4 py-3 bg-gray-50 border border-gray-300 rounded-lg">
                  {selectedDateTime ? (
                    <div className="text-gray-800">
                      <div className="font-medium">
                        {new Date(selectedDateTime).toLocaleDateString('fr-FR', {
                          weekday: 'long',
                          year: 'numeric',
                          month: 'long',
                          day: 'numeric'
                        })}
                      </div>
                      <div className="text-sm text-blue-600">
                        🕐 {new Date(selectedDateTime).toLocaleTimeString('fr-FR', {
                          hour: '2-digit',
                          minute: '2-digit'
                        })}
                      </div>
                    </div>
                  ) : (
                    <span className="text-gray-500">Aucune date/heure sélectionnée</span>
                  )}
                </div>
              </div>

              {/* Durée de l'entretien */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  ⏱️ Durée de l'entretien
                </label>
                <select
                  value={duree}
                  onChange={(e) => setDuree(parseInt(e.target.value))}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-200"
                >
                  <option value={30}>30 minutes</option>
                  <option value={45}>45 minutes</option>
                  <option value={60}>1 heure</option>
                  <option value={90}>1 heure 30</option>
                  <option value={120}>2 heures</option>
                </select>
              </div>

              {/* Heure de fin calculée */}
              {selectedDateTime && (
                <div className="p-4 bg-blue-50 rounded-lg border border-blue-200">
                  <div className="text-sm text-blue-800">
                    <div className="font-medium">Résumé de l'entretien :</div>
                    <div className="mt-1">
                      Début : {new Date(selectedDateTime).toLocaleTimeString('fr-FR', { 
                        hour: '2-digit', 
                        minute: '2-digit' 
                      })}
                    </div>
                    <div>
                      Fin : {new Date(new Date(selectedDateTime).getTime() + duree * 60000).toLocaleTimeString('fr-FR', { 
                        hour: '2-digit', 
                        minute: '2-digit' 
                      })}
                    </div>
                    <div className="text-xs text-blue-600 mt-1">
                      (Durée : {duree} minutes)
                    </div>
                  </div>
                </div>
              )}

              {/* Bouton de soumission */}
              <button
                type="submit"
                disabled={loading || !selectedDateTime || !selectedRh}
                className="w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 text-white font-semibold rounded-lg transition-all duration-200 flex items-center justify-center gap-2"
              >
                {loading ? (
                  <>
                    <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                    Planification en cours...
                  </>
                ) : (
                  <>
                    📅 Planifier l'entretien
                  </>
                )}
              </button>
            </form>
          </div>

          {/* Calendrier avec sélection d'heure */}
          <div className="bg-white rounded-2xl shadow-lg p-6">
            <h2 className="text-xl font-semibold text-gray-800 mb-6">
              Sélectionnez la date et l'heure
            </h2>
            
            {selectedRh ? (
              <CalendrierEntretiens 
                rhId={parseInt(selectedRh)} 
                onDateTimeSelect={setSelectedDateTime}
                selectedDateTime={selectedDateTime}
              />            ) : (
              <div className="text-center py-12 text-gray-500">
                <div className="text-4xl mb-4">📋</div>
                <p>Veuillez d'abord sélectionner un RH</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default FormulaireEntretien;