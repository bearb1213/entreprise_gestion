import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import CalendrierEntretiens from './CalendrierEntretiens';

const DashboardRH = () => {
  const navigate = useNavigate();
  const [rhId, setRhId] = useState(null);
  const [entretiens, setEntretiens] = useState([]);
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [loading, setLoading] = useState(false);

  // Récupérer l'ID du RH connecté (à adapter selon votre auth)
  useEffect(() => {
    const fetchRHInfo = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/utilisateur/me', {
          credentials: 'include'
        });
        if (response.ok) {
          const data = await response.json();
          // Supposons que l'ID du RH est dans la réponse
          setRhId(data.id || 1); // À adapter selon votre structure
        }
      } catch (error) {
        console.error('Erreur lors de la récupération des infos RH:', error);
      }
    };
    fetchRHInfo();
  }, []);

  // Récupérer les entretiens futurs du RH
  const fetchEntretiensFuturs = async () => {
    if (!rhId) return;
    
    setLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/api/entretiens/rh/${rhId}/futurs`, {
        credentials: 'include'
      });
      if (response.ok) {
        const data = await response.json();
        setEntretiens(data.entretiens || []);
      }
    } catch (error) {
      console.error('Erreur lors du chargement des entretiens:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (rhId) {
      fetchEntretiensFuturs();
    }
  }, [rhId]);

  // Obtenir les entretiens pour la date sélectionnée
  const getEntretiensForSelectedDate = () => {
    return entretiens.filter(entretien => {
      const entretienDate = new Date(entretien.dateHeureDebut);
      return (
        entretienDate.getDate() === selectedDate.getDate() &&
        entretienDate.getMonth() === selectedDate.getMonth() &&
        entretienDate.getFullYear() === selectedDate.getFullYear()
      );
    });
  };

  const entretiensDuJour = getEntretiensForSelectedDate();

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8">
      <div className="max-w-7xl mx-auto px-4">
        
        {/* En-tête */}
        <div className="mb-8 text-center">
          <h1 className="text-4xl font-bold text-gray-900 bg-gradient-to-r from-blue-600 to-indigo-700 bg-clip-text text-transparent">
            Dashboard RH
          </h1>
          <p className="text-gray-600 mt-2">Gestion des entretiens et évaluations</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          {/* Calendrier */}
          <div className="lg:col-span-2">
            <div className="bg-white rounded-2xl shadow-lg p-6">
              <h2 className="text-xl font-semibold text-gray-800 mb-6">
                Calendrier des Entretiens
              </h2>
              <CalendrierEntretiens 
                rhId={rhId}
                onDateSelect={setSelectedDate}
                selectedDate={selectedDate}
                entretiens={entretiens}
              />
            </div>
          </div>

          {/* Liste des entretiens du jour */}
          <div className="bg-white rounded-2xl shadow-lg p-6">
            <h2 className="text-xl font-semibold text-gray-800 mb-6">
              Entretiens du {selectedDate.toLocaleDateString('fr-FR', {
                weekday: 'long',
                year: 'numeric',
                month: 'long',
                day: 'numeric'
              })}
            </h2>

            {loading ? (
              <div className="text-center py-8">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500 mx-auto"></div>
                <p className="text-gray-500 mt-2">Chargement...</p>
              </div>
            ) : entretiensDuJour.length > 0 ? (
              <div className="space-y-4 max-h-96 overflow-y-auto">
                {entretiensDuJour.map((entretien) => (
                  <EntretienCard 
                    key={entretien.id} 
                    entretien={entretien}
                    onClick={() => navigate(`/entretien/${entretien.candidatureId}/evaluation`)}
                  />
                ))}
              </div>
            ) : (
              <div className="text-center py-8 text-gray-500">
                <div className="text-4xl mb-4">📅</div>
                <p>Aucun entretien prévu pour cette date</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

// Composant Carte d'entretien
const EntretienCard = ({ entretien, onClick }) => {
  const formatTime = (dateTime) => {
    return new Date(dateTime).toLocaleTimeString('fr-FR', {
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  return (
    <div 
      onClick={onClick}
      className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-all duration-200 cursor-pointer bg-white hover:bg-blue-50"
    >
      <div className="flex justify-between items-start mb-2">
        <div className="font-semibold text-gray-900">
          {entretien.candidat?.prenom} {entretien.candidat?.nom}
        </div>
        <div className="text-sm bg-blue-100 text-blue-800 px-2 py-1 rounded-full">
          🕐 {formatTime(entretien.dateHeureDebut)}
        </div>
      </div>
      
      {entretien.poste && (
        <div className="text-sm text-gray-600 mb-2">
          📋 {entretien.poste}
        </div>
      )}
      
      {entretien.candidat?.email && (
        <div className="text-xs text-gray-500">
          📧 {entretien.candidat.email}
        </div>
      )}
      
      <div className="mt-2 text-xs text-blue-600 font-medium">
        Cliquer pour évaluer →
      </div>
    </div>
  );
};

export default DashboardRH;