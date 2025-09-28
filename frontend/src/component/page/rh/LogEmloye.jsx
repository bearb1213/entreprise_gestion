import React, { useState } from 'react';
import InputText from '../../form/InputText';

const LogEmploye = ({navigate}) => {
    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });
    const [errors, setErrors] = useState({});
    const [isLoading, setIsLoading] = useState(false);
    const [typeLog, setTypeLog] = useState("EMP"); // "EMP" ou "INV"

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
        // Clear error when user starts typing
        if (errors[name]) {
            setErrors(prev => ({
                ...prev,
                [name]: ''
            }));
        }
    };

    const handleChangeType = () => {
        setTypeLog(prev => prev === "EMP" ? "INV" : "EMP");
        setFormData({ username: '', password: '' }); // Réinitialiser le formulaire
        setErrors({}); // Effacer les erreurs
    };

    const validateForm = () => {
        const newErrors = {};

        if (!formData.username.trim()) {
            newErrors.username = 'L\'identifiant est requis';
        }

        // Validation du mot de passe seulement pour les employés
        if (typeLog === "EMP") {
            if (!formData.password) {
                newErrors.password = 'Le mot de passe est requis';
            } else if (formData.password.length < 6) {
                newErrors.password = 'Le mot de passe doit contenir au moins 6 caractères';
            }
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!validateForm()) return;

        setIsLoading(true);
        
        try {
            // Simuler une requête API
            await new Promise(resolve => setTimeout(resolve, 1000));
            
            if (typeLog === "EMP") {
                console.log('Connexion employé réussie:', formData);
                alert('Connexion employé réussie !');
            } else {
                console.log('Connexion invité réussie:', formData.username);
                alert('Connexion invité réussie !');
            }
            
        } catch (error) {
            console.error('Erreur de connexion:', error);
            setErrors({ general: typeLog === "EMP" 
                ? 'Identifiant ou mot de passe incorrect' 
                : 'Identifiant incorrect' 
            });
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100 py-12 px-4 sm:px-6 lg:px-8">
            <div className="max-w-md w-full space-y-8">
                {/* Header amélioré */}
                <div className="text-center">
                    <div className="mx-auto w-16 h-16 bg-blue-600 rounded-full flex items-center justify-center mb-4">
                        <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                        </svg>
                    </div>
                    <h2 className="text-3xl font-bold text-gray-900 bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
                        {typeLog === "EMP" ? "Connexion Employé" : "Connexion Invité"}
                    </h2>
                    <p className="mt-2 text-sm text-gray-600">
                        {typeLog === "EMP" 
                            ? "Entrez vos identifiants pour accéder à votre compte" 
                            : "Entrez votre identifiant pour continuer en tant qu'invité"
                        }
                    </p>
                </div>

                {/* Carte du formulaire */}
                <div className="bg-white rounded-2xl shadow-xl p-8 border border-gray-100">
                    <form className="space-y-6" onSubmit={handleSubmit}>
                        {/* Champs de formulaire */}
                        <div className="space-y-4">
                            <InputText
                                label={typeLog === "EMP" ? "Identifiant" : "Code d'accès"}
                                name="username"
                                value={formData.username}
                                onChange={handleChange}
                                placeholder={typeLog === "EMP" ? "Votre identifiant" : "Votre code d'accès invité"}
                                required
                                error={errors.username}
                                className="focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />

                            {/* Champ mot de passe seulement pour les employés */}
                            {typeLog === "EMP" && (
                                <InputText
                                    label="Mot de passe"
                                    name="password"
                                    value={formData.password}
                                    onChange={handleChange}
                                    placeholder="Votre mot de passe"
                                    type="password"
                                    required
                                    error={errors.password}
                                    className="focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                />
                            )}
                        </div>

                        {/* Message d'erreur général */}
                        {errors.general && (
                            <div className="rounded-lg bg-red-50 p-4 border border-red-200 animate-pulse">
                                <div className="flex items-center">
                                    <svg className="w-5 h-5 text-red-500 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                        <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                                    </svg>
                                    <p className="text-sm font-medium text-red-800">{errors.general}</p>
                                </div>
                            </div>
                        )}

                        {/* Bouton de connexion */}
                        <button
                            type="submit"
                            disabled={isLoading}
                            className="w-full bg-gradient-to-r from-blue-600 to-indigo-600 text-white py-3 px-4 rounded-xl font-semibold shadow-lg hover:from-blue-700 hover:to-indigo-700 transform hover:scale-105 transition-all duration-200 focus:outline-none focus:ring-4 focus:ring-blue-500 focus:ring-opacity-50 disabled:opacity-70 disabled:cursor-not-allowed disabled:transform-none"
                        >
                            {isLoading ? (
                                <div className="flex items-center justify-center">
                                    <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                    </svg>
                                    Connexion...
                                </div>
                            ) : (
                                typeLog === "EMP" ? 'Se connecter' : 'Continuer en invité'
                            )}
                        </button>

                        {/* Séparateur */}
                        <div className="relative">
                            <div className="absolute inset-0 flex items-center">
                                <div className="w-full border-t border-gray-300" />
                            </div>
                            <div className="relative flex justify-center text-sm">
                                <span className="px-2 bg-white text-gray-500">Ou</span>
                            </div>
                        </div>

                        {/* Bouton pour changer le type de connexion */}
                        <div className="text-center">
                            <p className="text-sm text-gray-600">
                                {typeLog === "EMP" 
                                    ? "Vous n'êtes pas employé ? " 
                                    : "Vous êtes employé ? "
                                }
                                <button
                                    type="button"
                                    onClick={handleChangeType}
                                    className="font-semibold text-blue-600 hover:text-blue-500 transition-colors duration-200 inline-flex items-center focus:outline-none"
                                >
                                    {typeLog === "EMP" 
                                        ? "Se connecter en invité" 
                                        : "Se connecter en employé"
                                    }
                                    <svg className="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 5l7 7-7 7" />
                                    </svg>
                                </button>
                            </p>
                        </div>
                    </form>
                </div>

                {/* Footer */}
                <div className="text-center">
                    <p className="text-xs text-gray-500">
                        © 2025 GG l'équipe. 
                    </p>
                </div>
            </div>
        </div>
    );
};

export default LogEmploye;