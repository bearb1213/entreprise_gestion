import React, { useState, useEffect } from "react";
import axios from "axios";

const EntretienNote = () => {
    const [candidatures, setCandidatures] = useState([]);
    const [evaluations, setEvaluations] = useState([]);
    const [formData, setFormData] = useState({
        candidatureId: "",
        evaluationId: "",
        note: "",
    });
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);

    const API_BASE_URL = "http://localhost:8080/api";

    useEffect(() => {
        fetchInitialData();
    }, []);

    const fetchInitialData = async () => {
        try {
            const [candidaturesRes, evaluationsRes] = await Promise.all([
                axios.get(`${API_BASE_URL}/candidatures`),
                axios.get(`${API_BASE_URL}/evaluations`)
            ]);
            
            setCandidatures(candidaturesRes.data);
            setEvaluations(evaluationsRes.data);
        } catch (error) {
            console.error("Error fetching data:", error);
            alert("Erreur lors du chargement des données: " + (error.response?.data?.message || error.message));
        } finally {
            setLoading(false);
        }
    };

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
                candidatureId: parseInt(formData.candidatureId),
                evaluationId: parseInt(formData.evaluationId)
            };

            console.log("Données envoyées:", noteData);

            const response = await axios.post(`${API_BASE_URL}/notes`, noteData);
            console.log("Réponse du serveur:", response.data);
            
            alert("Note ajoutée avec succès !");
            setFormData({ candidatureId: "", evaluationId: "", note: "" });
        } catch (error) {
            console.error("Error submitting note:", error);
            console.error("Response data:", error.response?.data);
            console.error("Response status:", error.response?.status);
            
            const errorMessage = error.response?.data 
                || error.response?.data?.message 
                || "Erreur lors de l'ajout de la note";
            alert(`Erreur: ${errorMessage}`);
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) {
        return <div className="flex justify-center items-center h-64">
            <div className="text-lg">Chargement...</div>
        </div>;
    }

    return (
        <div className="container mx-auto p-6">
            <h2 className="text-2xl font-bold mb-6">Ajouter une Note</h2>
            <form onSubmit={handleSubmit} className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
                <div className="mb-4">
                    <label htmlFor="candidatureId" className="block text-gray-700 text-sm font-bold mb-2">
                        Candidature:
                    </label>
                    <select
                        id="candidatureId"
                        name="candidatureId"
                        value={formData.candidatureId}
                        onChange={handleChange}
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        required
                    >
                        <option value="">-- Sélectionner une candidature --</option>
                        {candidatures.map((candidature) => (
                            <option key={candidature.id} value={candidature.id}>
                                {`${candidature.candidatNom}`}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="mb-4">
                    <label htmlFor="evaluationId" className="block text-gray-700 text-sm font-bold mb-2">
                        Évaluation:
                    </label>
                    <select
                        id="evaluationId"
                        name="evaluationId"
                        value={formData.evaluationId}
                        onChange={handleChange}
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        required
                    >
                        <option value="">-- Sélectionner une évaluation --</option>
                        {evaluations.map((evaluation) => (
                            <option key={evaluation.id} value={evaluation.id}>
                                {evaluation.libelle} (Coeff: {evaluation.coeff})
                            </option>
                        ))}
                    </select>
                </div>
                <div className="mb-6">
                    <label htmlFor="note" className="block text-gray-700 text-sm font-bold mb-2">
                        Note (0-20):
                    </label>
                    <input
                        type="number"
                        id="note"
                        name="note"
                        value={formData.note}
                        onChange={handleChange}
                        step="0.01"
                        min="0"
                        max="20"
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        required
                    />
                </div>
                <button
                    type="submit"
                    disabled={submitting}
                    className={`bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline transition duration-300 ${
                        submitting ? 'opacity-50 cursor-not-allowed' : ''
                    }`}
                >
                    {submitting ? 'Ajout en cours...' : 'Ajouter la note'}
                </button>
            </form>
        </div>
    );
};

export default EntretienNote;