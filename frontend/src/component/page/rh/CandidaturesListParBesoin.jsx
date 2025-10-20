import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Card from '../../card/Card';
import CardGrid from '../../card/CardGrid';
import { CardActions, CardButton } from '../../card/CardActions';

const API_BASE_URL = "http://localhost:8080/api";

const CandidaturesListParBesoin = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [candidatures, setCandidatures] = useState([]);
  const [besoinInfo, setBesoinInfo] = useState(null);
  const [error, setError] = useState(null);
  const [userRole, setUserRole] = useState(null);

  // Récupérer le rôle de l'utilisateur connecté
  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await fetch(`${API_BASE_URL}/utilisateur/me`, {
          credentials: "include"
        });
        
        if (response.ok) {
          const userData = await response.json();
          console.log("User data:", userData);
          
          // Déterminer le rôle
          const role = userData.roles?.[0]?.authority || userData.userType;
          setUserRole(role);
        }
      } catch (error) {
        console.error("Erreur récupération info utilisateur:", error);
      }
    };
    
    fetchUserInfo();
  }, []);

  // Fonction pour obtenir le badge de statut de candidature
  const getStatutCandidatureBadge = (statut) => {
    const statuts = {
      0: { text: 'En attente', color: 'bg-yellow-100 text-yellow-800' },
      1: { text: 'En étude', color: 'bg-blue-100 text-blue-800' },
      2: { text: 'Acceptée', color: 'bg-green-100 text-green-800' },
      3: { text: 'Refusée', color: 'bg-red-100 text-red-800' }
    };
    return statuts[statut] || statuts[0];
  };

  // Charger les informations du besoin
  const fetchBesoinInfo = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/besoins/${id}`, {
        credentials: "include"
      });
      if (response.ok) {
        const data = await response.json();
        setBesoinInfo(data);
      }
    } catch (error) {
      console.error("Erreur chargement info besoin:", error);
    }
  };

  // Charger les candidatures pour ce besoin
  const fetchCandidatures = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_BASE_URL}/candidature/besoin/${id}`, {
        method: "GET",
        credentials: "include"
      });

      if (!response.ok) {
        throw new Error("Erreur serveur : " + response.status);
      }

      const data = await response.json();
      
      // Filtrer les candidatures selon le rôle
      let filteredCandidatures = data;
      if (userRole === 'RH' || userRole?.includes('RH')) {
        // RH : seulement les candidatures acceptées (statut 2)
        filteredCandidatures = data.filter(c => c.statut === 2);
      }
      // Département : toutes les candidatures (pas de filtre)
      
      setCandidatures(filteredCandidatures);
      console.log("Candidatures reçues:", filteredCandidatures);
    } catch (error) {
      console.error("Erreur lors du chargement des candidatures:", error);
      setError('Erreur lors du chargement des candidatures');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBesoinInfo();
    if (userRole) {
      fetchCandidatures();
    }
  }, [id, userRole]);

  // Fonction pour formater la date
  const formatDate = (dateString) => {
    if (!dateString) return 'Non spécifié';
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  };

  // Fonction pour extraire le prénom et nom du candidat
  const getCandidatInfo = (candidatNom) => {
    if (!candidatNom) return { prenom: 'Candidat', nom: 'Inconnu' };
    
    const parts = candidatNom.split(' ');
    if (parts.length >= 2) {
      return {
        prenom: parts[0],
        nom: parts.slice(1).join(' ')
      };
    }
    
    return {
      prenom: candidatNom,
      nom: ''
    };
  };

  // Fonction pour déterminer les actions selon le rôle
  const getCardActions = (candidature) => {
    if (userRole === 'RH' || userRole?.includes('RH')) {
      return (
        <CardActions align="between">
          <CardButton 
            variant="secondary"
            onClick={() => navigate(`/candidature-detail/${candidature.id}`)}
          >
            Détails
          </CardButton>
          <CardButton 
            variant="primary"
            onClick={() => navigate(`/entretien-note/${candidature.id}`)}
          >
            Évaluer
          </CardButton>
        </CardActions>
      );
    }
    
    if (userRole === 'DEPARTEMENT' || userRole?.includes('DEPARTEMENT')) {
      return (
        <CardActions align="center">
          <CardButton 
            variant="secondary"
            onClick={() => navigate(`/candidature-detail/${candidature.id}`)}
          >
            Voir détails
          </CardButton>
        </CardActions>
      );
    }
    
    // Par défaut, seulement le bouton détails
    return (
      <CardActions align="center">
        <CardButton 
          variant="secondary"
          onClick={() => navigate(`/candidature-detail/${candidature.id}`)}
        >
          Détails
        </CardButton>
      </CardActions>
    );
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-8">
            <h1 className="text-4xl font-bold text-gray-900 mb-4">Chargement des candidatures...</h1>
            <p className="text-gray-600">Veuillez patienter pendant que nous récupérons les candidatures</p>
          </div>
          <div className="animate-pulse">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {[...Array(6)].map((_, index) => (
                <div key={index} className="bg-white rounded-2xl shadow-lg p-6 h-64">
                  <div className="h-4 bg-gray-200 rounded w-3/4 mb-4"></div>
                  <div className="h-3 bg-gray-200 rounded w-1/2 mb-2"></div>
                  <div className="h-3 bg-gray-200 rounded w-2/3 mb-2"></div>
                  <div className="h-3 bg-gray-200 rounded w-1/3"></div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* En-tête avec informations du besoin */}
        <div className="text-center mb-12">
          <div className="flex items-center justify-between mb-6">
            <button
              onClick={() => navigate(-1)}
              className="px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-all duration-200 flex items-center gap-2"
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
              Retour
            </button>
            <h1 className="text-4xl font-bold text-gray-900 bg-gradient-to-r from-blue-600 to-indigo-700 bg-clip-text text-transparent">
              Candidatures
              {userRole === 'RH' && <span className="block text-sm text-gray-600 mt-1">(Candidatures acceptées uniquement)</span>}
            </h1>
            <div className="w-20"></div>
          </div>

          {besoinInfo && (
            <div className="bg-white rounded-2xl shadow-lg p-6 mb-6">
              <h2 className="text-2xl font-bold text-gray-800 mb-2">
                {besoinInfo.metier?.libelle || "Annonce sans titre"}
              </h2>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm text-gray-600">
                <div>
                  <span className="font-medium">Postes disponibles:</span>
                  <p className="text-gray-900">{besoinInfo.nbPosteDispo || 0}</p>
                </div>
                <div>
                  <span className="font-medium">Département:</span>
                  <p className="text-gray-900">{besoinInfo.departement?.libelle || 'Non spécifié'}</p>
                </div>
                <div>
                  <span className="font-medium">Expérience requise:</span>
                  <p className="text-gray-900">
                    {besoinInfo.minExperience ? `${besoinInfo.minExperience} an(s) minimum` : 'Non requise'}
                  </p>
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Statistiques */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-12">
          <div className="bg-white rounded-2xl shadow-lg p-6 text-center transform hover:scale-105 transition-transform duration-200">
            <div className="w-12 h-12 bg-blue-500 rounded-full flex items-center justify-center mx-auto mb-4">
              <span className="text-white font-bold text-lg">{candidatures.length}</span>
            </div>
            <h3 className="text-gray-600 font-medium">Total candidatures</h3>
          </div>
          <div className="bg-white rounded-2xl shadow-lg p-6 text-center transform hover:scale-105 transition-transform duration-200">
            <div className="w-12 h-12 bg-yellow-500 rounded-full flex items-center justify-center mx-auto mb-4">
              <span className="text-white font-bold text-lg">
                {candidatures.filter(c => c.statut === 0).length}
              </span>
            </div>
            <h3 className="text-gray-600 font-medium">En attente</h3>
          </div>
          <div className="bg-white rounded-2xl shadow-lg p-6 text-center transform hover:scale-105 transition-transform duration-200">
            <div className="w-12 h-12 bg-green-500 rounded-full flex items-center justify-center mx-auto mb-4">
              <span className="text-white font-bold text-lg">
                {candidatures.filter(c => c.statut === 2).length}
              </span>
            </div>
            <h3 className="text-gray-600 font-medium">Acceptées</h3>
          </div>
          <div className="bg-white rounded-2xl shadow-lg p-6 text-center transform hover:scale-105 transition-transform duration-200">
            <div className="w-12 h-12 bg-red-500 rounded-full flex items-center justify-center mx-auto mb-4">
              <span className="text-white font-bold text-lg">
                {candidatures.filter(c => c.statut === 3).length}
              </span>
            </div>
            <h3 className="text-gray-600 font-medium">Refusées</h3>
          </div>
        </div>

        {/* Liste des candidatures */}
        <section className="mb-12">
          <div className="flex justify-between items-center mb-8">
            <h2 className="text-3xl font-bold text-gray-800">
              Liste des Candidatures
              {userRole === 'RH' && <span className="text-sm text-gray-600 ml-2">(Acceptées uniquement)</span>}
            </h2>
            <button
              onClick={fetchCandidatures}
              className="px-6 py-2 bg-gradient-to-r from-blue-600 to-indigo-700 text-white rounded-lg hover:from-blue-700 hover:to-indigo-800 transition-all duration-200 shadow-lg hover:shadow-xl flex items-center gap-2"
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
              Actualiser
            </button>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 rounded-2xl p-6 mb-6">
              <p className="text-red-600 font-medium">{error}</p>
            </div>
          )}

          {candidatures.length === 0 ? (
            <div className="text-center py-12">
              <div className="bg-white rounded-2xl shadow-lg p-12 max-w-md mx-auto">
                <div className="w-24 h-24 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-6">
                  <svg className="w-12 h-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </div>
                <h3 className="text-xl font-semibold text-gray-900 mb-2">
                  {userRole === 'RH' ? 'Aucune candidature acceptée' : 'Aucune candidature'}
                </h3>
                <p className="text-gray-600">
                  {userRole === 'RH' 
                    ? "Aucune candidature n'a été acceptée pour cette annonce." 
                    : "Aucune candidature n'a été reçue pour cette annonce."}
                </p>
              </div>
            </div>
          ) : (
            <CardGrid>
              {candidatures.map((candidature) => {
                const statutBadge = getStatutCandidatureBadge(candidature.statut);
                const candidatInfo = getCandidatInfo(candidature.candidatNom);
                
                return (
                  <Card
                    key={candidature.id}
                    title={`${candidatInfo.prenom} ${candidatInfo.nom}`.trim()}
                    description={candidature.poste || "Candidature sans titre"}
                    imageUrl={"https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80"}
                    width={350}
                    height={450}
                    border={true}
                    variant="default"
                    shadow="large"
                    hoverEffect={true}
                    footer={
                      <div className="w-full">
                        <div className="flex justify-between items-center mb-3">
                          <span className={`px-2 py-1 text-xs font-medium rounded-full ${statutBadge.color}`}>
                            {statutBadge.text}
                          </span>
                          <span className="text-xs text-gray-500">
                            {formatDate(candidature.dateCandidature)}
                          </span>
                        </div>
                        {getCardActions(candidature)}
                      </div>
                    }
                  >
                    <div className="space-y-3 text-sm text-gray-600">
                      <div className="flex justify-between">
                        <span>Poste:</span>
                        <span className="font-medium text-right max-w-[150px] truncate">
                          {candidature.poste || 'Non spécifié'}
                        </span>
                      </div>
                      <div className="flex justify-between">
                        <span>Candidat:</span>
                        <span className="font-medium">
                          {candidature.candidatNom || 'Non spécifié'}
                        </span>
                      </div>
                      <div className="pt-2 border-t border-gray-100">
                        <p className="text-xs text-gray-500">
                          ID: {candidature.id}
                        </p>
                      </div>
                    </div>
                  </Card>
                );
              })}
            </CardGrid>
          )}
        </section>

      </div>
    </div>
  );
};

export default CandidaturesListParBesoin;