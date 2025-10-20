import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Card from '../../card/Card';
import CardGrid from '../../card/CardGrid';

const API_BASE_URL = "http://localhost:8080/api";

const CandidatureDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [candidature, setCandidature] = useState(null);
  const [error, setError] = useState(null);
  const [actionLoading, setActionLoading] = useState(false);
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
          
          // Déterminer le rôleupdat
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
      0: { text: 'En attente', color: 'bg-yellow-100 text-yellow-800 border-yellow-200' },
      1: { text: 'En étude', color: 'bg-blue-100 text-blue-800 border-blue-200' },
      2: { text: 'Acceptée', color: 'bg-green-100 text-green-800 border-green-200' },
      3: { text: 'Refusée', color: 'bg-red-100 text-red-800 border-red-200' }
    };
    return statuts[statut] || statuts[0];
  };

  // Fonction pour obtenir le badge de pourcentage
  const getPourcentageBadge = (pourcentage) => {
    if (pourcentage >= 75) {
      return { text: 'Excellent', color: 'bg-green-100 text-green-800 border-green-200' };
    } else if (pourcentage >= 50) {
      return { text: 'Bon', color: 'bg-blue-100 text-blue-800 border-blue-200' };
    } else {
      return { text: 'Insuffisant', color: 'bg-red-100 text-red-800 border-red-200' };
    }
  };

  // Fonction pour formater la date
  const formatDate = (dateString) => {
    if (!dateString) return 'Non spécifié';
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  // Fonction pour calculer l'âge
  const calculerAge = (dateNaissance) => {
    if (!dateNaissance) return 'Non spécifié';
    const today = new Date();
    const birthDate = new Date(dateNaissance);
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    
    return `${age} ans`;
  };

  // Fonction pour mettre à jour le statut
  const updateStatut = async (nouveauStatut) => {
    setActionLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/candidature/${id}/statut`, {
        method: "POST",
        credentials: "include",
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          statut: nouveauStatut
        })
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Erreur lors de la mise à jour du statut");
      }

      // Recharger les détails après la mise à jour
      await fetchCandidatureDetails();
      
      // Afficher un message de succès
      const message = nouveauStatut === 2 ? "Candidature acceptée avec succès" : "Candidature refusée avec succès";
      alert(message);
      
    } catch (error) {
      console.error("Erreur lors de la mise à jour du statut:", error);
      alert("Erreur lors de la mise à jour du statut: " + error.message);
    } finally {
      setActionLoading(false);
    }
  };

  // Fonction pour embaucher le candidat (pour admin)
  const handleEmbaucher = async () => {
    if (window.confirm("Êtes-vous sûr de vouloir embaucher ce candidat ? Cette action est définitive.")) {
      setActionLoading(true);
      try {
        const response = await fetch(`${API_BASE_URL}/embauche/${id}`, {
          method: "GET",
          credentials: "include"
        });

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText || "Erreur lors de l'embauche");
        }

        const result = await response.json();
        
        if (result.status === "success") {
          alert("Candidat embauché avec succès !");
          // Recharger les détails après l'embauche
          await fetchCandidatureDetails();
        } else {
          throw new Error(result.message || "Erreur lors de l'embauche");
        }
        
      } catch (error) {
        console.error("Erreur lors de l'embauche:", error);
        alert("Erreur lors de l'embauche: " + error.message);
      } finally {
        setActionLoading(false);
      }
    }
  };

  // Fonction pour accepter la candidature
  const handleAccepter = () => {
    if (window.confirm("Êtes-vous sûr de vouloir accepter cette candidature ?")) {
      updateStatut(2); // 2 = Acceptée
    }
  };

  // Fonction pour refuser la candidature
  const handleRefuser = () => {
    if (window.confirm("Êtes-vous sûr de vouloir refuser cette candidature ?")) {
      updateStatut(3); // 3 = Refusée
    }
  };

  const handleEntretienPage = () => {
    navigate("/entretien/"+id);
  };

  // Charger les détails de la candidature
  const fetchCandidatureDetails = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_BASE_URL}/candidature/details/${id}`, {
        method: "GET",
        credentials: "include"
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Erreur serveur : " + response.status);
      }

      const data = await response.json();
        console.log(data);

      setCandidature(data);
    } catch (error) {
      console.error("Erreur lors du chargement des détails:", error);
      setError(error.message || 'Erreur lors du chargement des détails de la candidature');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCandidatureDetails();
  }, [id]);

  // Vérifier si l'utilisateur est admin
  const isAdmin = userRole === 'ADMIN' || userRole?.includes('ADMIN');

  // Vérifier si on peut afficher les boutons d'action (seulement si statut n'est pas déjà Acceptée ou Refusée)
  const canShowActions = candidature?.statut === 0 || candidature?.statut === 1;

  // Vérifier si on peut afficher le bouton embaucher (admin seulement et si candidature est acceptée)
  const canShowEmbaucher = isAdmin && candidature?.derniereEvaluationId === 3 ;

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-8">
            <h1 className="text-4xl font-bold text-gray-900 mb-4">Chargement des détails...</h1>
            <p className="text-gray-600">Veuillez patienter pendant que nous récupérons les informations</p>
          </div>
          <div className="animate-pulse">
            <CardGrid>
              {[...Array(3)].map((_, index) => (
                <div key={index} className="bg-white rounded-2xl shadow-lg p-6 w-80 h-96"></div>
              ))}
            </CardGrid>
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="bg-red-50 border border-red-200 rounded-2xl p-8 text-center">
            <div className="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg className="w-8 h-8 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <h2 className="text-2xl font-bold text-red-800 mb-2">Erreur</h2>
            <p className="text-red-600 mb-6">{error}</p>
            <button
              onClick={() => navigate(-1)}
              className="px-6 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-all duration-200"
            >
              Retour
            </button>
          </div>
        </div>
      </div>
    );
  }

  if (!candidature) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h1 className="text-4xl font-bold text-gray-900 mb-4">Candidature non trouvée</h1>
            <button
              onClick={() => navigate(-1)}
              className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-all duration-200"
            >
              Retour
            </button>
          </div>
        </div>
      </div>
    );
  }

  const statutBadge = getStatutCandidatureBadge(candidature.statut);
  const besoinInfo = candidature.besoin;
  const pourcentageBadge = candidature.pourcentage ? getPourcentageBadge(candidature.pourcentage) : null;

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* En-tête */}
        <div className="flex items-center justify-between mb-8">
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
            Détail de la Candidature
            {isAdmin && <span className="block text-sm text-gray-600 mt-1">(Vue Administrateur)</span>}
          </h1>
          <div className="w-20"></div>
        </div>

        {/* Grille horizontale des cartes */}
        <CardGrid gap={6} cardClassName="w-80">
          
          {/* Carte informations personnelles */}
          <Card
            title="Informations Personnelles"
            variant="primary"
            border={true}
            shadow="large"
            width={320}
            height={500}
            footer={
              <div className="flex flex-col space-y-2">
                <div className="flex justify-between items-center">
                  <span className="text-sm text-gray-500">
                    {formatDate(candidature.dateCandidature)}
                  </span>
                  <span className={`px-2 py-1 text-xs font-medium rounded-full border ${statutBadge.color}`}>
                    {statutBadge.text}
                  </span>
                </div>
                {candidature.noteTotale !== undefined && candidature.noteMaximale !== undefined && (
                  <div className="flex justify-between items-center pt-2 border-t border-gray-200">
                    <span className="text-sm text-gray-500">Score:</span>
                    <div className="text-right">
                      <div className="text-sm font-bold text-gray-900">
                        {candidature.noteTotale.toFixed(2)}/{candidature.noteMaximale.toFixed(2)}
                      </div>
                      {candidature.pourcentage !== undefined && (
                        <div className="flex items-center gap-1 text-xs">
                          <span className="text-gray-600">{candidature.pourcentage}%</span>
                          <span className={`px-1 py-0.5 text-xs font-medium rounded-full border ${pourcentageBadge?.color || 'bg-gray-100 text-gray-800 border-gray-200'}`}>
                            {pourcentageBadge?.text || 'Non évalué'}
                          </span>
                        </div>
                      )}
                    </div>
                  </div>
                )}
              </div>
            }
          >
            <div className="space-y-4">
              <div className="flex flex-col items-center text-center">
                <img 
                  src={"https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80"} 
                  alt={candidature.candidat || "Candidat"}
                  className="w-16 h-16 rounded-full object-cover border-4 border-white shadow-lg mb-3"
                />
                <h3 className="text-xl font-bold text-gray-900">
                  {candidature.candidat || "Nom non disponible"}
                </h3>
                <p className="text-sm text-gray-600">{candidature.email || "Email non disponible"}</p>
                {candidature.telephone && (
                  <p className="text-xs text-gray-500 mt-1">{candidature.telephone}</p>
                )}
              </div>

              <div className="space-y-3 pt-3 border-t border-gray-200">
                <div className="flex justify-between">
                  <span className="text-xs font-medium text-gray-500">Date de naissance</span>
                  <span className="text-sm text-gray-900">
                    {candidature.date_naissance ? formatDate(candidature.date_naissance) : 'Non spécifié'}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className="text-xs font-medium text-gray-500">Âge</span>
                  <span className="text-sm text-gray-900">
                    {calculerAge(candidature.date_naissance)}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className="text-xs font-medium text-gray-500">Ville</span>
                  <span className="text-sm text-gray-900">
                    {candidature.ville || 'Non spécifié'}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className="text-xs font-medium text-gray-500">Genre</span>
                  <span className="text-sm text-gray-900">
                    {candidature.genre === 1 ? 'Femme' : candidature.genre === 0 ? 'Homme' : 'Non spécifié'}
                  </span>
                </div>
              </div>

              {candidature.description && (
                <div className="pt-3 border-t border-gray-200">
                  <span className="text-xs font-medium text-gray-500">Description</span>
                  <p className="text-gray-900 mt-1 text-xs leading-relaxed line-clamp-3">
                    {candidature.description}
                  </p>
                </div>
              )}
            </div>
          </Card>

          {/* Carte informations du besoin */}
          {besoinInfo && (
            <Card
              title="Poste Candidaté"
              variant="info"
              border={true}
              shadow="large"
              width={320}
              height={500}
            >
              <div className="space-y-4">
                <div className="text-center">
                  <h4 className="text-lg font-semibold text-gray-900 mb-2">
                    {besoinInfo.metier || "Poste non spécifié"}
                  </h4>
                  {besoinInfo.departement && (
                    <p className="text-sm text-gray-600">
                      {besoinInfo.departement}
                    </p>
                  )}
                </div>

                <div className="space-y-3">
                  <div className="flex justify-between">
                    <span className="text-xs font-medium text-gray-500">Postes disponibles</span>
                    <span className="text-sm font-bold text-gray-900">{besoinInfo.nbPosteDispo || 0}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-xs font-medium text-gray-500">Expérience requise</span>
                    <span className="text-sm text-gray-900">
                      {besoinInfo.minExperience ? `${besoinInfo.minExperience} an(s)` : 'Non requise'}
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-xs font-medium text-gray-500">Âge minimum</span>
                    <span className="text-sm text-gray-900">
                      {besoinInfo.minAge ? `${besoinInfo.minAge} ans` : 'Non spécifié'}
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-xs font-medium text-gray-500">Âge maximum</span>
                    <span className="text-sm text-gray-900">
                      {besoinInfo.maxAge ? `${besoinInfo.maxAge} ans` : 'Non spécifié'}
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-xs font-medium text-gray-500">Statut</span>
                    <span className="text-sm text-gray-900">
                      {besoinInfo.statut === 1 ? 'Actif' : 'Inactif'}
                    </span>
                  </div>
                </div>
              </div>
            </Card>
          )}

          {/* Carte notes d'évaluation */}
          {candidature.notes && candidature.notes.length > 0 && (
            <Card
              title="Évaluations"
              variant="success"
              border={true}
              shadow="large"
              width={320}
              height={500}
            >
              <div className="space-y-4">
                {candidature.notes.map((note, index) => (
                  <div key={index} className="border-b border-gray-200 pb-3 last:border-b-0 last:pb-0">
                    <div className="flex justify-between items-start mb-1">
                      <span className="text-sm font-medium text-gray-900">{note.evaluation}</span>
                      <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                        note.pourcentage >= 75 ? 'bg-green-100 text-green-800' :
                        note.pourcentage >= 50 ? 'bg-blue-100 text-blue-800' :
                        note.pourcentage >= 25 ? 'bg-yellow-100 text-yellow-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {note.pourcentage}%
                      </span>
                    </div>
                    {note.date && (
                      <p className="text-xs text-gray-500">
                        {formatDate(note.date)}
                      </p>
                    )}
                  </div>
                ))}
              </div>
            </Card>
          )}

          {/* Carte actions */}
          <Card
            title="Actions"
            variant="primary"
            border={true}
            shadow="large"
            width={320}
            height={500}
          >
            <div className="space-y-3">
              <button
                onClick={() => navigate(`/besoins/${candidature.besoinId || ''}`)}
                className="w-full px-4 py-3 bg-blue-100 text-blue-700 rounded-lg hover:bg-blue-200 transition-all duration-200 text-sm font-medium text-left flex items-center gap-2"
              >
                <span>📋</span>
                Voir l'offre d'emploi
              </button>
              <button
                onClick={() => window.print()}
                className="w-full px-4 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-all duration-200 text-sm font-medium text-left flex items-center gap-2"
              >
                <span>🖨️</span>
                Imprimer cette fiche
              </button>
              <button
                onClick={fetchCandidatureDetails}
                className="w-full px-4 py-3 bg-green-100 text-green-700 rounded-lg hover:bg-green-200 transition-all duration-200 text-sm font-medium text-left flex items-center gap-2"
              >
                <span>🔄</span>
                Actualiser
              </button>
            </div>
          </Card>

        </CardGrid>

        {/* Actions en bas */}
        <div className="mt-8 flex justify-center space-x-4">
          <button
            onClick={() => navigate(-1)}
            className="px-6 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-all duration-200 font-medium"
          >
            Retour à la liste
          </button>
          
          {/* Boutons Accepter/Refuser - seulement si statut n'est pas déjà Acceptée ou Refusée */}
          {canShowActions && !isAdmin && (
            <>
              <button
                onClick={handleAccepter}
                disabled={actionLoading}
                className="px-6 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-all duration-200 font-medium flex items-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {actionLoading ? (
                  <>
                    <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                    Traitement...
                  </>
                ) : (
                  <>
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                    </svg>
                    Accepter
                  </>
                )}
              </button>
              
              <button
                onClick={handleRefuser}
                disabled={actionLoading}
                className="px-6 py-3 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-all duration-200 font-medium flex items-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {actionLoading ? (
                  <>
                    <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                    Traitement...
                  </>
                ) : (
                  <>
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                    </svg>
                    Refuser
                  </>
                )}
              </button>
            </>
          )}

          {/* Bouton Embaucher pour les administrateurs */}
          {canShowEmbaucher && (
            <button
              onClick={handleEmbaucher}
              disabled={actionLoading}
              className="px-6 py-3 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-all duration-200 font-medium flex items-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {actionLoading ? (
                <>
                  <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                  Traitement...
                </>
              ) : (
                <>
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                  </svg>
                  Embaucher
                </>
              )}
            </button>
          )}

          {/* Bouton Organiser un entretien */}
          {!isAdmin  && candidature.derniereEvaluationId==2 &&(
            <button
              onClick={handleEntretienPage}
              disabled={actionLoading}
              className="px-6 py-3 bg-orange-600 text-white rounded-lg hover:bg-orange-700 transition-all duration-200 font-medium flex items-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {actionLoading ? (
                <>
                  <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                  Traitement...
                </>
              ) : (
                <>
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                  Organiser un entretien
                </>
              )}
            </button>
          )}
          
          {/* Si déjà acceptée ou refusée, afficher un message */}
          {!canShowActions && !canShowEmbaucher && (
            <div className="text-center">
              <p className="text-gray-600 italic">
                {candidature.statut === 2 && !candidature.estEmploye
                  ? isAdmin 
                    ? "Cette candidature est prête pour l'embauche" 
                    : "Cette candidature a déjà été acceptée" 
                  : candidature.statut === 3 
                  ? "Cette candidature a déjà été refusée" 
                  : candidature.statut === 1 
                  ? "Cette candidature est déjà en cours d'étude"
                  : ""}
              </p>
            </div>
          )}
        </div>

      </div>
    </div>
  );
};

export default CandidatureDetail;