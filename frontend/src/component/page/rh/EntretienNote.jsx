import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";

const EntretienNote = () => {
    const { id } = useParams(); // Récupère l'ID de la candidature depuis l'URL
    const [candidature, setCandidature] = useState(null);
    const [formData, setFormData] = useState({
        note: "",
    });
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);

    const API_BASE_URL = "http://localhost:8080/api";
    const EVALUATION_ENTRETIEN_ID = 3; // ID fixe pour l'évaluation "Entretien"

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

    // Charger les détails de la candidature
    const fetchCandidatureDetails = async () => {
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
            alert("Erreur lors du chargement des détails: " + error.message);
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            await fetchCandidatureDetails();
            setLoading(false);
        };
        fetchData();
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        
        try {
            const noteData = {
                note: parseFloat(formData.note),
                candidatureId: parseInt(id),
                evaluationId: EVALUATION_ENTRETIEN_ID // ID fixe pour "Entretien"
            };

            console.log("Données envoyées:", noteData);

            const response = await fetch(`${API_BASE_URL}/notes`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify(noteData)
            });

            console.log("Statut de la réponse:", response.status);

            // Gérer la réponse même si elle n'est pas OK
            let result;
            try {
                result = await response.json();
            } catch (jsonError) {
                // Si la réponse n'est pas du JSON valide
                console.error("Erreur parsing JSON:", jsonError);
                if (response.ok) {
                    // Si le statut est OK mais réponse vide, considérer comme succès
                    result = { status: "success", message: "Note ajoutée avec succès" };
                } else {
                    throw new Error(`Erreur ${response.status}: ${response.statusText}`);
                }
            }

            if (!response.ok) {
                console.error("Erreur détaillée:", result);
                throw new Error(result.message || result.error || `Erreur ${response.status}`);
            }

            console.log("Réponse du serveur:", result);
            
            alert(result.message || "Note ajoutée avec succès !");
            setFormData({ note: "" });
            
            // Recharger les détails pour voir la nouvelle note
            await fetchCandidatureDetails();
        } catch (error) {
            console.error("Error submitting note:", error);
            alert(`Erreur: ${error.message}`);
        } finally {
            setSubmitting(false);
        }
    };
    // Vérifier si une note d'entretien existe déjà
    const hasExistingEntretienNote = () => {
        candidature.derniereEvaluationId==3;
    };

    if (loading) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8">
                <div className="max-w-7xl mx-auto px-4">
                    <div className="text-center mb-8">
                        <h1 className="text-4xl font-bold text-gray-900 mb-4">Chargement des détails...</h1>
                        <p className="text-gray-600">Veuillez patienter pendant que nous récupérons les informations</p>
                    </div>
                </div>
            </div>
        );
    }

    if (!candidature) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8">
                <div className="max-w-4xl mx-auto px-4">
                    <div className="text-center">
                        <h1 className="text-4xl font-bold text-gray-900 mb-4">Candidature non trouvée</h1>
                    </div>
                </div>
            </div>
        );
    }

    const besoinInfo = candidature.besoin;
    const existingEntretienNote = hasExistingEntretienNote();

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-8">
            <div className="max-w-6xl mx-auto px-4">
                
                {/* En-tête */}
                <div className="text-center mb-8">
                    <h1 className="text-4xl font-bold text-gray-900 bg-gradient-to-r from-blue-600 to-indigo-700 bg-clip-text text-transparent">
                        Évaluation d'Entretien
                    </h1>
                    <p className="text-gray-600 mt-2">Ajouter une note d'entretien pour {candidature.candidat || "le candidat"}</p>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    
                    {/* Colonne de gauche : Informations de la candidature */}
                    <div className="space-y-6">
                        
                        {/* Carte informations personnelles */}
                        <div className="bg-white rounded-2xl shadow-lg p-6">
                            <h2 className="text-xl font-semibold text-gray-800 mb-4">Informations du Candidat</h2>
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
                                        <span className="text-sm font-medium text-gray-500">Date de naissance</span>
                                        <span className="text-sm text-gray-900">
                                            {candidature.date_naissance ? formatDate(candidature.date_naissance) : 'Non spécifié'}
                                        </span>
                                    </div>
                                    <div className="flex justify-between">
                                        <span className="text-sm font-medium text-gray-500">Âge</span>
                                        <span className="text-sm text-gray-900">
                                            {calculerAge(candidature.date_naissance)}
                                        </span>
                                    </div>
                                    <div className="flex justify-between">
                                        <span className="text-sm font-medium text-gray-500">Ville</span>
                                        <span className="text-sm text-gray-900">
                                            {candidature.ville || 'Non spécifié'}
                                        </span>
                                    </div>
                                    <div className="flex justify-between">
                                        <span className="text-sm font-medium text-gray-500">Genre</span>
                                        <span className="text-sm text-gray-900">
                                            {candidature.genre === 1 ? 'Femme' : candidature.genre === 0 ? 'Homme' : 'Non spécifié'}
                                        </span>
                                    </div>
                                </div>

                                {candidature.description && (
                                    <div className="pt-3 border-t border-gray-200">
                                        <span className="text-sm font-medium text-gray-500">Description</span>
                                        <p className="text-gray-900 mt-1 text-sm leading-relaxed">
                                            {candidature.description}
                                        </p>
                                    </div>
                                )}
                            </div>
                        </div>

                        {/* Carte informations du poste */}
                        {besoinInfo && (
                            <div className="bg-white rounded-2xl shadow-lg p-6">
                                <h2 className="text-xl font-semibold text-gray-800 mb-4">Poste Candidaté</h2>
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
                                            <span className="text-sm font-medium text-gray-500">Postes disponibles</span>
                                            <span className="text-sm font-bold text-gray-900">{besoinInfo.nbPosteDispo || 0}</span>
                                        </div>
                                        <div className="flex justify-between">
                                            <span className="text-sm font-medium text-gray-500">Expérience requise</span>
                                            <span className="text-sm text-gray-900">
                                                {besoinInfo.minExperience ? `${besoinInfo.minExperience} an(s)` : 'Non requise'}
                                            </span>
                                        </div>
                                        <div className="flex justify-between">
                                            <span className="text-sm font-medium text-gray-500">Âge minimum</span>
                                            <span className="text-sm text-gray-900">
                                                {besoinInfo.minAge ? `${besoinInfo.minAge} ans` : 'Non spécifié'}
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>

                    {/* Colonne de droite : Formulaire d'évaluation */}
                    <div className="bg-white rounded-2xl shadow-lg p-6">
                        <h2 className="text-xl font-semibold text-gray-800 mb-6">
                            Note d'Entretien
                        </h2>
                        
                        {existingEntretienNote && (
                            <div className="mb-6 p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
                                <div className="flex items-center">
                                    <svg className="w-5 h-5 text-yellow-600 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                        <path fillRule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
                                    </svg>
                                    <p className="text-sm text-yellow-700">
                                        Une note d'entretien existe déjà pour cette candidature.
                                    </p>
                                </div>
                            </div>
                        )}
                        
                        <form onSubmit={handleSubmit} className="space-y-6">
                            {/* Type d'évaluation fixe */}
                            <div className="p-4 bg-blue-50 rounded-lg border border-blue-200">
                                <div className="flex items-center justify-between">
                                    <div>
                                        <p className="font-medium text-blue-800">Évaluation : Entretien</p>
                                        <p className="text-sm text-blue-600">Coefficient: 5</p>
                                    </div>
                                    <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center">
                                        <span className="text-blue-600 font-bold">3</span>
                                    </div>
                                </div>
                            </div>

                            {/* Input de note */}
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Note d'entretien (sur 20)
                                </label>
                                <input
                                    type="number"
                                    name="note"
                                    value={formData.note}
                                    onChange={handleChange}
                                    step="0.1"
                                    min="0"
                                    max="20"
                                    placeholder="Ex: 15.5"
                                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-200"
                                    required
                                />
                                <p className="text-xs text-gray-500 mt-1">
                                    Note comprise entre 0 et 20, peut inclure des décimales
                                </p>
                            </div>

                            {/* Bouton de soumission */}
                            <button
                                type="submit"
                                disabled={submitting || existingEntretienNote}
                                className={`w-full py-3 px-4 text-white font-semibold rounded-lg transition-all duration-200 flex items-center justify-center gap-2 ${
                                    submitting || existingEntretienNote || !formData.note
                                        ? 'bg-gray-400 cursor-not-allowed'
                                        : 'bg-blue-600 hover:bg-blue-700'
                                }`}
                            >
                                {submitting ? (
                                    <>
                                        <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                                        Ajout en cours...
                                    </>
                                ) : existingEntretienNote ? (
                                    <>
                                        ✅ Note d'entretien déjà existante
                                    </>
                                ) : (
                                    <>
                                        📝 Ajouter la note d'entretien
                                    </>
                                )}
                            </button>                        </form>

                        {/* Notes existantes */}
                        {candidature.notes && candidature.notes.length > 0 && (
                            <div className="mt-8 pt-6 border-t border-gray-200">
                                <h3 className="text-lg font-semibold text-gray-800 mb-4">Historique des Notes</h3>
                                <div className="space-y-3">
                                    {candidature.notes.map((note, index) => (
                                        <div key={index} className={`border rounded-lg p-3 ${
                                            note.evaluationId === EVALUATION_ENTRETIEN_ID || 
                                            (note.evaluation && note.evaluation.toLowerCase().includes("entretien"))
                                                ? 'border-blue-300 bg-blue-50'
                                                : 'border-gray-200'
                                        }`}>
                                            <div className="flex justify-between items-start mb-1">
                                                <div>
                                                    <span className="text-sm font-medium text-gray-900">
                                                        {note.evaluation}
                                                        {(note.evaluationId === EVALUATION_ENTRETIEN_ID || 
                                                          (note.evaluation && note.evaluation.toLowerCase().includes("entretien"))) && 
                                                          <span className="ml-2 text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded-full">Entretien</span>
                                                        }
                                                    </span>
                                                </div>
                                                <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                                                    note.note >= 16 ? 'bg-green-100 text-green-800' :
                                                    note.note >= 12 ? 'bg-blue-100 text-blue-800' :
                                                    note.note >= 8 ? 'bg-yellow-100 text-yellow-800' :
                                                    'bg-red-100 text-red-800'
                                                }`}>
                                                    {note.note}/{note.noteMaximal}
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
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default EntretienNote;