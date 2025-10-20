import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { CardSkeleton } from "../../card/CardSkeleton";

const API_BASE_URL = "http://localhost:8080/api";

const AnnonceDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [besoin, setBesoin] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isPostulating, setIsPostulating] = useState(false);
  const [userRole, setUserRole] = useState(null);
  const [isCandidat, setIsCandidat] = useState(false);

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
          
          // Déterminer le rôle et le type d'utilisateur
          const role = userData.roles?.[0]?.authority || userData.userType;
          setUserRole(role);
          
          // Vérifier si c'est un candidat
          setIsCandidat(role === 'ROLE_CANDIDAT' || userData.userType === 'ROLE_CANDIDAT');
        }
      } catch (error) {
        console.error("Erreur récupération info utilisateur:", error);
      }
    };
    
    fetchUserInfo();
  }, []);

  const handlePostuler = async (annonce) => {
    setIsPostulating(true);
    
    try {
      const response = await fetch(
        `${API_BASE_URL}/candidature/candidater?idBesoin=${annonce.id}`, 
        {
          method: 'POST',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json',
          }
        }
      );

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.error || 'Erreur lors de la candidature');
      }

      alert(`✅ ${data.message}`);

    } catch (error) {
      console.error('Erreur candidature:', error);
      
      if (error.message.includes('session') || error.message.includes('email')) {
        alert(`❌ ${error.message}`);
      } else {
        alert(`❌ ${error.message}`);
      }
    } finally {
      setIsPostulating(false);
    }
  };

  const handleVoirCandidatures = () => {
    // Naviguer vers la page des candidatures pour ce besoin
    navigate(`/candidatures/${id}`);
  };

  useEffect(() => {
    const fetchBesoin = async () => {
      setLoading(true);
      try {
        const response = await fetch(`${API_BASE_URL}/besoins/${id}`, {
          credentials: "include",
        });
        if (!response.ok) throw new Error(`Erreur HTTP : ${response.status}`);
        const data = await response.json();
        console.log("Données reçues:", data);
        setBesoin(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchBesoin();
  }, [id]);

  // Fonction pour obtenir le statut en texte
  const getStatutText = (statut) => {
    switch(statut) {
      case 0: return "Brouillon";
      case 1: return "Publié";
      case 2: return "En cours de recrutement";
      case 3: return "Pourvu";
      case 4: return "Archivé";
      default: return "Inconnu";
    }
  };

  // Fonction pour obtenir la couleur du statut
  const getStatutColor = (statut) => {
    switch(statut) {
      case 0: return "bg-gray-100 text-gray-800";
      case 1: return "bg-green-100 text-green-800";
      case 2: return "bg-blue-100 text-blue-800";
      case 3: return "bg-purple-100 text-purple-800";
      case 4: return "bg-gray-100 text-gray-800";
      default: return "bg-gray-100 text-gray-800";
    }
  };

  // Déterminer quel bouton afficher
  const renderActionButton = () => {
    // Si l'utilisateur est un département
    if (userRole === 'ROLE_DEPARTEMENT' || userRole?.includes('ROLE_DEPARTEMENT')) {
      return (
        <button 
          onClick={handleVoirCandidatures}
          className="flex-1 px-4 py-3 bg-gradient-to-r from-green-600 to-emerald-700 text-white rounded-lg hover:from-green-700 hover:to-emerald-800 font-medium transition-colors flex items-center justify-center"
        >
          <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
            <path d="M9 6a3 3 0 11-6 0 3 3 0 016 0zM17 6a3 3 0 11-6 0 3 3 0 016 0zM12.93 17c.046-.327.07-.66.07-1a6.97 6.97 0 00-1.5-4.33A5 5 0 0119 16v1h-6.07zM6 11a5 5 0 015 5v1H1v-1a5 5 0 015-5z" />
          </svg>
          Voir les candidatures
        </button>
      );
    }
    
    // Si l'utilisateur n'est pas un candidat
    if (!isCandidat) {
      return (
        <button 
          disabled
          className="flex-1 px-4 py-3 bg-gray-400 text-white rounded-lg cursor-not-allowed font-medium flex items-center justify-center"
        >
          <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
            <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
          </svg>
          Réservé aux candidats
        </button>
      );
    }
    
    // Si c'est un candidat, afficher le bouton Postuler
    return (
      <button 
        onClick={() => handlePostuler(besoin)}      
        disabled={isPostulating}
        className={`flex-1 px-4 py-3 bg-gradient-to-r from-blue-600 to-indigo-700 text-white rounded-lg font-medium transition-colors flex items-center justify-center ${
          isPostulating ? 'opacity-50 cursor-not-allowed' : 'hover:from-blue-700 hover:to-indigo-800'
        }`}
      >
        {isPostulating ? (
          <>
            <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
            Postulation...
          </>
        ) : (
          <>
            <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-11a1 1 0 10-2 0v2H7a1 1 0 100 2h2v2a1 1 0 102 0v-2h2a1 1 0 100-2h-2V7z" clipRule="evenodd" />
            </svg>
            Postuler maintenant
          </>
        )}
      </button>
    );
  };

  if (loading) return <CardSkeleton />;
  if (error) return <p className="text-red-500 text-center mt-10">Erreur : {error}</p>;
  if (!besoin) return <p className="text-center mt-10">Aucune annonce trouvée</p>;

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8 px-4">
      <div className="max-w-4xl mx-auto">
        <div className="bg-white rounded-2xl shadow-xl overflow-hidden">
          {/* En-tête avec image de fond */}
          <div className="bg-gradient-to-r from-blue-600 to-indigo-700 text-white p-6">
            <div className="flex justify-between items-start">
              <div>
                <h1 className="text-3xl font-bold mb-2">{besoin.metier?.libelle || "Poste à pourvoir"}</h1>
                <div className="flex items-center space-x-4">
                  <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatutColor(besoin.statut)}`}>
                    {getStatutText(besoin.statut)}
                  </span>
                  <span className="flex items-center">
                    <svg className="w-5 h-5 mr-1" fill="currentColor" viewBox="0 0 20 20">
                      <path fillRule="evenodd" d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd" />
                    </svg>
                    {besoin.departement?.libelle || "Non spécifié"}
                  </span>
                </div>
              </div>
              <div className="text-right">
                <p className="text-2xl font-bold">{besoin.nbPosteDispo} poste(s)</p>
                <p className="text-blue-200">disponible(s)</p>
              </div>
            </div>
          </div>

          {/* Contenu principal */}
          <div className="p-6">
            {/* Informations principales */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
              <div className="bg-blue-50 p-4 rounded-lg">
                <h2 className="text-lg font-semibold text-blue-800 mb-3 flex items-center">
                  <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                    <path fillRule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clipRule="evenodd" />
                  </svg>
                  Profil recherché
                </h2>
                <div className="space-y-2">
                  <div className="flex justify-between">
                    <span className="text-gray-600">Âge requis :</span>
                    <span className="font-medium">{besoin.minAge} - {besoin.maxAge} ans</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Expérience minimale :</span>
                    <span className="font-medium">{besoin.minExperience} an(s)</span>
                  </div>
                </div>
              </div>

              <div className="bg-indigo-50 p-4 rounded-lg">
                <h2 className="text-lg font-semibold text-indigo-800 mb-3 flex items-center">
                  <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M10.394 2.08a1 1 0 00-.788 0l-7 3a1 1 0 000 1.84L5.25 8.051a.999.999 0 01.356-.257l4-1.714a1 1 0 11.788 1.838L7.667 9.088l1.94.831a1 1 0 00.787 0l7-3a1 1 0 000-1.838l-7-3zM3.31 9.397L5 10.12v4.102a8.969 8.969 0 00-1.05-.174 1 1 0 01-.89-.89 11.115 11.115 0 01.25-3.762zM9.3 16.573A9.026 9.026 0 007 14.935v-3.957l1.818.78a3 3 0 002.364 0l5.508-2.361a11.026 11.026 0 01.25 3.762 1 1 0 01-.89.89 8.968 8.968 0 00-5.35 2.524 1 1 0 01-1.4 0zM6 18a1 1 0 001-1v-2.065a8.935 8.935 0 00-2-.712V17a1 1 0 001 1z" />
                  </svg>
                  Métier & Département
                </h2>
                <div className="space-y-2">
                  <div className="flex justify-between">
                    <span className="text-gray-600">Métier :</span>
                    <span className="font-medium">{besoin.metier?.libelle || "Non spécifié"}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Département :</span>
                    <span className="font-medium">{besoin.departement?.libelle || "Non spécifié"}</span>
                  </div>
                </div>
              </div>
            </div>

            {/* Compétences requises */}
            <div className="mb-8">
              <h2 className="text-xl font-bold text-gray-800 mb-4 pb-2 border-b border-gray-200">
                Compétences requises
              </h2>
              {besoin.besoinCompetences && besoin.besoinCompetences.length > 0 ? (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                  {besoin.besoinCompetences.map(bc => (
                    <div key={bc.id} className="bg-white border border-gray-200 rounded-lg p-3 flex justify-between items-center shadow-sm">
                      <span className="font-medium">{bc.competence?.libelle || "Compétence non spécifiée"}</span>
                      <span className="bg-blue-100 text-blue-800 text-sm font-medium px-2.5 py-0.5 rounded-full">
                        Coefficient {bc.coeff}
                      </span>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-gray-500 italic">Aucune compétence spécifiée</p>
              )}
            </div>

            {/* Langues requises */}
            <div className="mb-8">
              <h2 className="text-xl font-bold text-gray-800 mb-4 pb-2 border-b border-gray-200">
                Langues requises
              </h2>
              {besoin.besoinLangues && besoin.besoinLangues.length > 0 ? (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                  {besoin.besoinLangues.map(bl => (
                    <div key={bl.id} className="bg-white border border-gray-200 rounded-lg p-3 flex justify-between items-center shadow-sm">
                      <span className="font-medium">{bl.langue?.libelle || "Langue non spécifiée"}</span>
                      <span className="bg-green-100 text-green-800 text-sm font-medium px-2.5 py-0.5 rounded-full">
                        Coefficient {bl.coeff}
                      </span>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-gray-500 italic">Aucune langue spécifiée</p>
              )}
            </div>

            {/* Diplômes et Filières */}
            <div className="mb-8">
              <h2 className="text-xl font-bold text-gray-800 mb-4 pb-2 border-b border-gray-200">
                Diplômes et Filières
              </h2>
              {besoin.besoinDiplomeFilieres && besoin.besoinDiplomeFilieres.length > 0 ? (
                <div className="space-y-3">
                  {besoin.besoinDiplomeFilieres.map(bd => (
                    <div key={bd.id} className="bg-white border border-gray-200 rounded-lg p-4 shadow-sm">
                      <div className="flex justify-between items-center">
                        <div>
                          <p className="font-medium">
                            {bd.diplomeFiliere?.diplome?.libelle || "Diplôme non spécifié"} - 
                            {bd.diplomeFiliere?.filiere?.libelle || "Filière non spécifiée"}
                          </p>
                        </div>
                        <span className="bg-purple-100 text-purple-800 text-sm font-medium px-2.5 py-0.5 rounded-full">
                          Coefficient {bd.coeff}
                        </span>
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-gray-500 italic">Aucun diplôme ou filière spécifié</p>
              )}
            </div>

            {/* Boutons d'action */}
            <div className="flex flex-col sm:flex-row gap-3 pt-4 border-t border-gray-200">
              <button
                onClick={() => navigate(-1)}
                className="flex-1 px-4 py-3 bg-gray-100 text-gray-800 rounded-lg hover:bg-gray-200 font-medium transition-colors flex items-center justify-center"
              >
                <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M9.707 16.707a1 1 0 01-1.414 0l-6-6a1 1 0 010-1.414l6-6a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l4.293 4.293a1 1 0 010 1.414z" clipRule="evenodd" />
                </svg>
                Retour
              </button>
              
              {/* Bouton conditionnel */}
              {renderActionButton()}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AnnonceDetail;