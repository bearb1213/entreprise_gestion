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

    // Base URL pour les API
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
        try {
            await axios.post(`${API_BASE_URL}/notes`, formData);
            alert("Note ajoutée avec succès !");
            setFormData({ candidatureId: "", evaluationId: "", note: "" });
        } catch (error) {
            console.error("Error submitting note:", error);
            alert("Erreur lors de l'ajout de la note: " + (error.response?.data?.message || error.message));
        }
    };

    if (loading) {
        return <div>Chargement...</div>;
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
                                {`Candidature ${candidature.id}`}
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
                                {evaluation.libelle}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="mb-6">
                    <label htmlFor="note" className="block text-gray-700 text-sm font-bold mb-2">
                        Note:
                    </label>
                    <input
                        type="number"
                        id="note"
                        name="note"
                        value={formData.note}
                        onChange={handleChange}
                        step="0.01"
                        min="0"
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        required
                    />
                </div>
                <button
                    type="submit"
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                >
                    Ajouter
                </button>
            </form>
        </div>
    );
};

export default EntretienNote;