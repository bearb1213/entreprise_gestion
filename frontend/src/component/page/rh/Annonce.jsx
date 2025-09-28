import React, { useState, useEffect } from "react";
import AnnonceCard from "../../template/AnnonceCard";
import CompactAnnonceCard from "../../template/CompactAnnonceCard";
import { Link } from "react-router-dom";
import besoinService from "../../../services/besoinService";

const Annonce = () => {
  const [annonces, setAnnonces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAnnonces = async () => {
      try {
        setLoading(true);
        const data = await besoinService.getAllBesoins();
        setAnnonces(data);
        setError(null);
      } catch (err) {
        setError("Erreur lors du chargement des annonces");
        console.error("Erreur:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchAnnonces();
  }, []);

  if (loading) {
    return (
      <div className="mx-auto p-4 space-y-4">
        <div className="text-center py-8">
          <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
          <p className="mt-2 text-gray-600">Chargement des annonces...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="mx-auto p-4 space-y-4">
        <div className="bg-red-50 border border-red-200 rounded-lg p-4 text-center">
          <p className="text-red-600">{error}</p>
          <button 
            onClick={() => window.location.reload()} 
            className="mt-2 px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
          >
            Réessayer
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="mx-auto p-4 space-y-4">
      {annonces.length === 0 ? (
        <div className="text-center py-8">
          <p className="text-gray-600">Aucune annonce disponible</p>
        </div>
      ) : (
        annonces.map((annonce) => (
          <CompactAnnonceCard key={annonce.id} annonce={annonce} />
        ))
      )}
      <div className="fixed bottom-8 right-8">
        <Link to="/formAnnonce">
          <button className="bg-blue-600 text-white p-4 rounded-full shadow-lg hover:bg-blue-700 transition-colors flex items-center justify-center">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4v16m8-8H4" />
            </svg>
          </button>
        </Link>
      </div>
    </div>
  );
};

export default Annonce;
