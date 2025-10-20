import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Card from '../../card/Card';
import CardGrid from '../../card/CardGrid';
import { CardActions, CardButton } from '../../card/CardActions';
import { CardGridSkeleton } from '../../card/CardSkeleton';

const API_BASE_URL = "http://localhost:8080/api";

const UniteDashboard = () => {
  const [loading, setLoading] = useState(false);
  const [annonces, setAnnonces] = useState([]);
  const navigate = useNavigate();

  const stats = [
    { label: 'Annonces actives', value: annonces.length, color: 'bg-blue-500' },
    { label: 'Postes disponibles', value: annonces.reduce((acc, annonce) => acc + (annonce.nbPosteDispo || 0), 0), color: 'bg-green-500' },
    { label: 'Métiers', value: new Set(annonces.map(a => a.metier?.id)).size, color: 'bg-purple-500' },
  ];

  

  const getStatutBadge = (statut) => {
    const statuts = {
      0: { text: 'Fermé', color: 'bg-gray-100 text-gray-800' },
      1: { text: 'Ouvert', color: 'bg-green-100 text-green-800' }
    };
    return statuts[statut] || statuts[0];
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
      alert('Erreur lors du chargement des annonces');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAnnonces();
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-8">
            <h1 className="text-4xl font-bold text-gray-900 mb-4">Chargement des annonces...</h1>
            <p className="text-gray-600">Veuillez patienter pendant que nous récupérons les dernières offres</p>
          </div>
          <CardGridSkeleton count={6} columns={3} />
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* En-tête amélioré */}
        <div className="text-center mb-12">
          <p className="text-xl text-gray-600 max-w-3xl mx-auto">
            Listes des annonces
          </p>
        </div>

        {/* Statistiques */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
          {stats.map((stat, index) => (
            <div key={index} className="bg-white rounded-2xl shadow-lg p-6 text-center transform hover:scale-105 transition-transform duration-200">
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
            <h2 className="text-3xl font-bold text-gray-800">Annonces</h2>
            <p className="text-gray-600 mt-2">{annonces.length} offre(s) disponible(s)</p>
          </div>
          <button
            onClick={fetchAnnonces}
            className="px-6 py-2 bg-gradient-to-r from-blue-600 to-indigo-700 text-white rounded-lg hover:from-blue-700 hover:to-indigo-800 transition-all duration-200 shadow-lg hover:shadow-xl flex items-center gap-2"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            Actualiser
          </button>
        </div>

        {/* Grid des annonces */}
        <section className="mb-12">
          <CardGrid columns={3}>
            {annonces.map((annonce) => {
              const statutBadge = getStatutBadge(annonce.statut);
              
              return (
                <Card
                  key={annonce.id}
                  title={annonce.metier?.libelle || "Sans titre"}
                  description={annonce.description || `Postes disponibles: ${annonce.nbPosteDispo || 0}`}
                  imageUrl={annonce.image || "https://images.unsplash.com/photo-1497366754035-f200968a6e72?ixlib=rb-4.0.3&auto=format&fit=crop&w=450&q=80"}
                  width={400}
                  height={500}
                  border={true}
                  shadow="large"
                  hoverEffect={true}
                  footer={
                    <div className="w-full">
                      <div className="flex justify-between items-center mb-3">
                        <span className={`px-2 py-1 text-xs font-medium rounded-full ${statutBadge.color}`}>
                          {statutBadge.text}
                        </span>
                        <span className={`text-sm 'text-green-600 font-medium' : 'text-gray-600'}`}>
                          {annonce.nbPosteDispo || 0} poste(s)
                        </span>
                      </div>
                      <CardActions>
                        <CardButton 
                          variant="secondary"
                          onClick={() => navigate(`/candidatures/${annonce.id}`)}
                          className="flex-1"
                        >
                          Voir les candidatures
                        </CardButton>
                      </CardActions>
                    </div>
                  }
                >
                  <div className="space-y-2 text-sm text-gray-600">
                    <div className="flex justify-between">
                      <span>Âge:</span>
                      <span className="font-medium">{annonce.minAge}-{annonce.maxAge} ans</span>
                    </div>
                    <div className="flex justify-between">
                      <span>Expérience:</span>
                      <span className="font-medium">{annonce.minExperience} an(s) min</span>
                    </div>
                    <div className="flex justify-between">
                      <span>Département:</span>
                      <span className="font-medium">{annonce.departement?.libelle || 'Non spécifié'}</span>
                    </div>
                  </div>
                </Card>
              );
            })}
          </CardGrid>
        </section>

        {annonces.length === 0 && !loading && (
          <div className="text-center py-12">
            <div className="bg-white rounded-2xl shadow-lg p-12 max-w-md mx-auto">
              <div className="w-24 h-24 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-6">
                <svg className="w-12 h-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <h3 className="text-xl font-semibold text-gray-900 mb-2">Aucune annonce disponible</h3>
              <p className="text-gray-600">Il n'y a actuellement aucune offre d'emploi disponible.</p>
            </div>
          </div>
        )}

      </div>
    </div>
  );
};

export default UniteDashboard;