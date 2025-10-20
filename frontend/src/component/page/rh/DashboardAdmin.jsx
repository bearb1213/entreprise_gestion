import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Card from '../../card/Card';
import CardGrid from '../../card/CardGrid';
import { CardActions, CardButton } from '../../card/CardActions';
import { CardGridSkeleton } from '../../card/CardSkeleton';

const API_BASE_URL = "http://localhost:8080/api";

const DashboardAdmin = () => {
  const [loading, setLoading] = useState(false);
  const [annonces, setAnnonces] = useState([]);
  const [entretiens, setEntretiens] = useState([]);
  const [statistiques, setStatistiques] = useState({});
  const [userRole, setUserRole] = useState(null);
  const navigate = useNavigate();

  const stats = [
    { 
      label: 'Candidatures Total', 
      value: statistiques.totalCandidatures || 0, 
      color: 'bg-blue-500',
      onClick: () => navigate('/candidatures')
    },
    { 
      label: 'Entretiens Aujourd\'hui', 
      value: entretiens.filter(entretien => {
        const entretienDate = new Date(entretien.dateHeureDebut);
        const today = new Date();
        return (
          entretienDate.getDate() === today.getDate() &&
          entretienDate.getMonth() === today.getMonth() &&
          entretienDate.getFullYear() === today.getFullYear()
        );
      }).length, 
      color: 'bg-green-500',
      onClick: () => navigate('/entretiens')
    },
    { 
      label: 'Postes Ouverts', 
      value: statistiques.postesOuverts || 0, 
      color: 'bg-purple-500',
      onClick: () => navigate('/besoins')
    },
    { 
      label: 'Candidats à Embaucher', 
      value: statistiques.candidatsAEmbaucher || 0, 
      color: 'bg-orange-500',
      onClick: () => navigate('/candidatures?statut=2')
    },
  ];

  // Récupérer le rôle de l'utilisateur connecté
  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await fetch(`${API_BASE_URL}/utilisateur/me`, {
          credentials: "include"
        });
        
        if (response.ok) {
          const userData = await response.json();
          const role = userData.roles?.[0]?.authority || userData.userType;
          setUserRole(role);
        }
      } catch (error) {
        console.error("Erreur récupération info utilisateur:", error);
      }
    };
    
    fetchUserInfo();
  }, []);

  // Récupérer les statistiques globales
  const fetchStatistiques = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/admin/statistiques`, {
        credentials: 'include'
      });
      if (response.ok) {
        const data = await response.json();
        setStatistiques(data);
      }
    } catch (error) {
      console.error('Erreur lors du chargement des statistiques:', error);
    }
  };

  // Récupérer tous les entretiens futurs
  const fetchEntretiensFuturs = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/entretiens/futurs`, {
        credentials: 'include'
      });
      if (response.ok) {
        const data = await response.json();
        setEntretiens(data.entretiens || []);
      }
    } catch (error) {
      console.error('Erreur lors du chargement des entretiens:', error);
    }
  };

  const fetchAnnonces = async () => {
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/besoins`, {
        method: "GET",
        credentials: "include"
      });

      if (!response.ok) {
        throw new Error("Erreur serveur : " + response.status);
      }

      const data = await response.json();
      setAnnonces(data);
    } catch (error) {
      console.error("Erreur lors du chargement des annonces:", error);
    } finally {
      setLoading(false);
    }
  };

  const getStatutBadge = (statut) => {
    const statuts = {
      0: { text: 'Brouillon', color: 'bg-gray-100 text-gray-800' },
      1: { text: 'Publié', color: 'bg-green-100 text-green-800' },
      2: { text: 'En cours', color: 'bg-blue-100 text-blue-800' },
      3: { text: 'Pourvu', color: 'bg-purple-100 text-purple-800' },
      4: { text: 'Archivé', color: 'bg-gray-100 text-gray-800' }
    };
    return statuts[statut] || statuts[0];
  };

  const getEntretienStatutBadge = (statut) => {
    const statuts = {
      0: { text: 'Planifié', color: 'bg-blue-100 text-blue-800' },
      1: { text: 'En cours', color: 'bg-yellow-100 text-yellow-800' },
      2: { text: 'Terminé', color: 'bg-green-100 text-green-800' },
      3: { text: 'Annulé', color: 'bg-red-100 text-red-800' }
    };
    return statuts[statut] || statuts[0];
  };

  const handleVoirCandidatures = (annonceId) => {
    navigate(`/candidatures/${annonceId}`);
  };

  const handleEditAnnonce = (annonceId) => {
    navigate(`/besoins/edit/${annonceId}`);
  };

  const handleCreateAnnonce = () => {
    navigate('/besoins/nouveau');
  };

  const handleVoirEntretien = (entretien) => {
    navigate(`/candidature-detail/${entretien.candidatureId}`);
  };

  const handleGestionUtilisateurs = () => {
    navigate('/utilisateurs');
  };

  const handleRapports = () => {
    navigate('/rapports');
  };

  const handleParametres = () => {
    navigate('/parametres');
  };

  useEffect(() => {
    fetchAnnonces();
    fetchStatistiques();
    fetchEntretiensFuturs();
  }, []);

  const entretiensDuJour = entretiens.filter(entretien => {
    const entretienDate = new Date(entretien.dateHeureDebut);
    const today = new Date();
    return (
      entretienDate.getDate() === today.getDate() &&
      entretienDate.getMonth() === today.getMonth() &&
      entretienDate.getFullYear() === today.getFullYear()
    );
  });

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-purple-50 to-indigo-100 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-8">
            <h1 className="text-4xl font-bold text-gray-900 mb-4">Chargement du dashboard...</h1>
            <p className="text-gray-600">Veuillez patienter pendant que nous récupérons les données</p>
          </div>
          <CardGridSkeleton count={6} columns={3} />
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-50 to-indigo-100 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* En-tête */}
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-gray-900 mb-4 bg-gradient-to-r from-purple-600 to-indigo-700 bg-clip-text text-transparent">
            Dashboard Administrateur
          </h1>
          <p className="text-xl text-gray-600 max-w-3xl mx-auto">
            Vue d'ensemble et gestion complète du système
          </p>
        </div>

        {/* Statistiques */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-12">
          {stats.map((stat, index) => (
            <div 
              key={index} 
              onClick={stat.onClick}
              className="bg-white rounded-2xl shadow-lg p-6 text-center transform hover:scale-105 transition-transform duration-200 cursor-pointer"
            >
              <div className={`w-12 h-12 ${stat.color} rounded-full flex items-center justify-center mx-auto mb-4`}>
                <span className="text-white font-bold text-lg">{stat.value}</span>
              </div>
              <h3 className="text-gray-600 font-medium">{stat.label}</h3>
            </div>
          ))}
        </div>

        {/* Contrôles */}
        <div className="flex justify-between items-center mb-8">
          <div>
            <h2 className="text-3xl font-bold text-gray-800">Annonces Récentes</h2>
            <p className="text-gray-600 mt-2">{annonces.length} annonce(s) au total</p>
          </div>
          <div className="flex gap-4">
            <button
              onClick={handleCreateAnnonce}
              className="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-all duration-200 shadow-lg hover:shadow-xl flex items-center gap-2"
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
              </svg>
              Nouvelle Annonce
            </button>
            <button
              onClick={() => { fetchAnnonces(); fetchStatistiques(); fetchEntretiensFuturs(); }}
              className="px-6 py-2 bg-gradient-to-r from-purple-600 to-indigo-700 text-white rounded-lg hover:from-purple-700 hover:to-indigo-800 transition-all duration-200 shadow-lg hover:shadow-xl flex items-center gap-2"
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
              Actualiser
            </button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          {/* Annonces récentes */}
          <div className="lg:col-span-2">
            <CardGrid columns={2}>
              {annonces.slice(0, 4).map((annonce) => {
                const statutBadge = getStatutBadge(annonce.statut);
                
                return (
                  <Card
                    key={annonce.id}
                    title={annonce.metier?.libelle || "Sans titre"}
                    description={annonce.description || `Postes disponibles: ${annonce.nbPosteDispo || 0}`}
                    imageUrl={annonce.image || "https://images.unsplash.com/photo-1497366754035-f200968a6e72?ixlib=rb-4.0.3&auto=format&fit=crop&w=450&q=80"}
                    width={400}
                    height={450}
                    border={true}
                    variant={annonce.statut === 1 ? "primary" : "default"}
                    shadow="large"
                    hoverEffect={true}
                    footer={
                      <div className="w-full">
                        <div className="flex justify-between items-center mb-3">
                          <span className={`px-2 py-1 text-xs font-medium rounded-full ${statutBadge.color}`}>
                            {statutBadge.text}
                          </span>
                          <div className="text-right">
                            <div className={`text-sm ${annonce.statut === 1 ? 'text-green-600 font-medium' : 'text-gray-600'}`}>
                              {annonce.nbPosteDispo || 0} poste(s)
                            </div>
                          </div>
                        </div>
                        <CardActions>
                          <CardButton 
                            variant="primary"
                            onClick={() => handleVoirCandidatures(annonce.id)}
                            className="flex-1"
                          >
                            Voir Candidatures
                          </CardButton>
                          <CardButton 
                            variant="secondary"
                            onClick={() => handleEditAnnonce(annonce.id)}
                            className="flex-1"
                          >
                            Modifier
                          </CardButton>
                        </CardActions>
                      </div>
                    }
                  >
                    <div className="space-y-2 text-sm text-gray-600">
                      <div className="flex justify-between">
                        <span>Expérience:</span>
                        <span className="font-medium">
                          {annonce.minExperience || 0} an(s) min
                        </span>
                      </div>
                      <div className="flex justify-between">
                        <span>Département:</span>
                        <span className="font-medium text-right max-w-[150px] truncate">
                          {annonce.departement?.libelle || 'Non spécifié'}
                        </span>
                      </div>
                      <div className="flex justify-between">
                        <span>Date création:</span>
                        <span className="font-medium">
                          {new Date(annonce.dateCreation).toLocaleDateString('fr-FR')}
                        </span>
                      </div>
                    </div>
                  </Card>
                );
              })}
            </CardGrid>

            {/* Actions rapides */}
            <div className="mt-8">
              <h2 className="text-2xl font-bold text-gray-800 mb-6">Actions Rapides</h2>
              <CardGrid columns={2}>
                <Card
                  title="Gérer les Utilisateurs"
                  description="Gestion des comptes et permissions"
                  variant="info"
                  border={true}
                  shadow="medium"
                  footer={
                    <CardActions>
                      <CardButton 
                        variant="primary"
                        onClick={handleGestionUtilisateurs}
                        className="w-full"
                      >
                        Accéder
                      </CardButton>
                    </CardActions>
                  }
                >
                  <div className="text-4xl text-center mb-4">👥</div>
                </Card>

                <Card
                  title="Rapports et Statistiques"
                  description="Analyses et données du système"
                  variant="success"
                  border={true}
                  shadow="medium"
                  footer={
                    <CardActions>
                      <CardButton 
                        variant="primary"
                        onClick={handleRapports}
                        className="w-full"
                      >
                        Voir les rapports
                      </CardButton>
                    </CardActions>
                  }
                >
                  <div className="text-4xl text-center mb-4">📈</div>
                </Card>

                <Card
                  title="Paramètres"
                  description="Configuration du système"
                  variant="warning"
                  border={true}
                  shadow="medium"
                  footer={
                    <CardActions>
                      <CardButton 
                        variant="primary"
                        onClick={handleParametres}
                        className="w-full"
                      >
                        Configurer
                      </CardButton>
                    </CardActions>
                  }
                >
                  <div className="text-4xl text-center mb-4">⚙️</div>
                </Card>

                <Card
                  title="Tous les Entretiens"
                  description="Calendrier complet des entretiens"
                  variant="primary"
                  border={true}
                  shadow="medium"
                  footer={
                    <CardActions>
                      <CardButton 
                        variant="primary"
                        onClick={() => navigate('/entretiens')}
                        className="w-full"
                      >
                        Voir le calendrier
                      </CardButton>
                    </CardActions>
                  }
                >
                  <div className="text-4xl text-center mb-4">📅</div>
                </Card>
              </CardGrid>
            </div>
          </div>

          {/* Colonne de droite */}
          <div className="space-y-6">
            
            {/* Entretiens du jour */}
            <Card
              title="Entretiens du Jour"
              description={new Date().toLocaleDateString('fr-FR', {
                weekday: 'long',
                year: 'numeric',
                month: 'long',
                day: 'numeric'
              })}
              variant="primary"
              border={true}
              shadow="large"
              footer={
                <CardActions>
                  <CardButton 
                    variant="secondary"
                    onClick={() => navigate('/entretiens')}
                    className="w-full"
                  >
                    Voir tous les entretiens
                  </CardButton>
                </CardActions>
              }
            >
              <div className="space-y-4 max-h-80 overflow-y-auto">
                {entretiensDuJour.length > 0 ? (
                  entretiensDuJour.map((entretien) => {
                    const statutBadge = getEntretienStatutBadge(entretien.statut);
                    const formatTime = (dateTime) => {
                      return new Date(dateTime).toLocaleTimeString('fr-FR', {
                        hour: '2-digit',
                        minute: '2-digit'
                      });
                    };

                    return (
                      <div 
                        key={entretien.id}
                        onClick={() => handleVoirEntretien(entretien)}
                        className="border border-gray-200 rounded-lg p-3 hover:shadow-md transition-all duration-200 cursor-pointer bg-white hover:bg-purple-50"
                      >
                        <div className="flex justify-between items-start mb-2">
                          <div className="font-semibold text-gray-900 text-sm">
                            {entretien.candidat?.prenom} {entretien.candidat?.nom}
                          </div>
                          <div className="flex flex-col items-end gap-1">
                            <span className="text-xs bg-purple-100 text-purple-800 px-2 py-1 rounded-full">
                              🕐 {formatTime(entretien.dateHeureDebut)}
                            </span>
                            <span className={`px-2 py-1 text-xs font-medium rounded-full ${statutBadge.color}`}>
                              {statutBadge.text}
                            </span>
                          </div>
                        </div>
                        
                        {entretien.poste && (
                          <div className="text-xs text-gray-600 mb-1">
                            📋 {entretien.poste}
                          </div>
                        )}
                        
                        {entretien.departement && (
                          <div className="text-xs text-gray-500">
                            🏢 {entretien.departement}
                          </div>
                        )}
                      </div>
                    );
                  })
                ) : (
                  <div className="text-center py-8 text-gray-500">
                    <div className="text-4xl mb-4">📅</div>
                    <p className="text-sm">Aucun entretien prévu pour aujourd'hui</p>
                  </div>
                )}
              </div>
            </Card>

            {/* Dernières candidatures */}
            <Card
              title="Dernières Candidatures"
              description="Les candidatures les plus récentes"
              variant="success"
              border={true}
              shadow="large"
              footer={
                <CardActions>
                  <CardButton 
                    variant="secondary"
                    onClick={() => navigate('/candidatures')}
                    className="w-full"
                  >
                    Voir toutes les candidatures
                  </CardButton>
                </CardActions>
              }
            >
              <CandidaturesRecentes />
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};

// Composant Candidatures récentes
const CandidaturesRecentes = () => {
  const [candidatures, setCandidatures] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCandidaturesRecentes = async () => {
      try {
        const response = await fetch(`${API_BASE_URL}/candidatures/recentes?limit=5`, {
          credentials: 'include'
        });
        if (response.ok) {
          const data = await response.json();
          setCandidatures(data.candidatures || []);
        }
      } catch (error) {
        console.error('Erreur lors du chargement des candidatures récentes:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchCandidaturesRecentes();
  }, []);

  const getStatutBadge = (statut) => {
    const statuts = {
      0: { text: 'En attente', color: 'bg-yellow-100 text-yellow-800' },
      1: { text: 'En étude', color: 'bg-blue-100 text-blue-800' },
      2: { text: 'Acceptée', color: 'bg-green-100 text-green-800' },
      3: { text: 'Refusée', color: 'bg-red-100 text-red-800' }
    };
    return statuts[statut] || statuts[0];
  };

  if (loading) {
    return (
      <div className="text-center py-4">
        <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-purple-500 mx-auto"></div>
      </div>
    );
  }

  return (
    <div className="space-y-3">
      {candidatures.map((candidature) => {
        const statutBadge = getStatutBadge(candidature.statut);
        return (
          <div 
            key={candidature.id}
            onClick={() => navigate(`/candidature-detail/${candidature.id}`)}
            className="flex items-center justify-between p-3 border border-gray-200 rounded-lg hover:bg-gray-50 cursor-pointer transition-all duration-200"
          >
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-gray-900 truncate">
                {candidature.candidatNom || 'Candidat'}
              </p>
              <p className="text-xs text-gray-500 truncate">
                {candidature.poste || 'Poste non spécifié'}
              </p>
            </div>
            <span className={`px-2 py-1 text-xs font-medium rounded-full ${statutBadge.color}`}>
              {statutBadge.text}
            </span>
          </div>
        );
      })}
      {candidatures.length === 0 && (
        <div className="text-center py-4 text-gray-500">
          <p className="text-sm">Aucune candidature récente</p>
        </div>
      )}
    </div>
  );
};

export default DashboardAdmin;