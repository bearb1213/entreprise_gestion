import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import InputText from "../../form/InputText";
import InputNumber from "../../form/InputNumber";
import Select from "../../form/Select";
import Button from "../../form/Button";
import MultiCheckbox from "../../form/MultiCheckbox";
import MultiSelect from "../../form/MultiSelect";
import MultiSelectInputNumber from "../../form/MultiSelectInputNumber";
import MultiSelectFiliere from "../../form/MultiSelectFiliere"; // Import du composant
import besoinService from "../../../services/besoinService";

const FormAnnonce = () => {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(true);
  
  // NOUVEAU : État pour les informations de l'utilisateur
  const [userInfo, setUserInfo] = useState(null);
  const [userLoading, setUserLoading] = useState(true);

  // États pour les données chargées
  const [metiers, setMetiers] = useState([]);
  const [departements, setDepartements] = useState([]);
  const [competences, setCompetences] = useState([]);
  const [langues, setLangues] = useState([]);

  const [formData, setFormData] = useState({
    statut: 0,
    minAge: "",
    maxAge: "",
    nbPosteDispo: "",
    minExperience: "",
    coeffAge: 2,
    coeffExperience: 2,
    metierId: "",
    departementId: "",
    competences: [],
    langues: [],
    diplomeFilieres: []
  });

    // DÉPLACER CES CONSTANTES ICI (avant tout return)
  const statutOptions = [
    { value: 1, label: "Ouvert" },
    { value: 0, label: "Fermé" },
  ];

  const coefficientOptions = [
    { value: 1, label: "Faible" },
    { value: 2, label: "Moyen" },
    { value: 3, label: "Important" },
    { value: 4, label: "Très important" }
  ];

  // Indicateur de progression
  const progressSteps = [
    "Informations générales",
    "Critères de sélection", 
    "Compétences requises"
  ];


  // Chargement des informations de l'utilisateur
  useEffect(() => {
    const fetchUserInfo = async () => {
      console.log("ETO TSIKA ZAO");
      try {
        const response = await fetch("http://localhost:8080/api/utilisateur/me", {
          credentials: "include"
        });
        
        if (response.ok) {
          const userData = await response.json();
          setUserInfo(userData);
          
          // Si l'utilisateur est DEPARTEMENT, pré-remplir son département
          if (isDepartementUser) {
            setFormData(prev => ({
              ...prev,
              departementId: 1
            }));
          }
        }
      } catch (error) {
        console.error("Erreur lors du chargement des infos utilisateur:"+ error.message);
      } finally {
        setUserLoading(false);
      }
    };

    fetchUserInfo();
  }, []);

  // Chargement des données
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        
        // Chargement parallèle de toutes les données
        const [languesRes, departementsRes, competencesRes, metiersRes] = await Promise.all([
          fetch("http://localhost:8080/api/langues", { credentials: "include" }),
          fetch("http://localhost:8080/api/departements", { credentials: "include" }),
          fetch("http://localhost:8080/api/competences", { credentials: "include" }),
          fetch("http://localhost:8080/api/metiers", { credentials: "include" }),
        ]);

        if (!languesRes.ok || !departementsRes.ok || !competencesRes.ok || !metiersRes.ok) {
          throw new Error("Erreur lors du chargement des données");
        }

        const [languesData, departementsData, competencesData, metiersData] = await Promise.all([
          languesRes.json(),
          departementsRes.json(),
          competencesRes.json(),
          metiersRes.json(),
        ]);

        setLangues(languesData.map(item => ({
          value: item.id,
          label: item.libelle,
        })));

        setDepartements(departementsData.map(item => ({
          value: item.id,
          label: item.libelle,
        })));

        setCompetences(competencesData.map(item => ({
          value: item.id,
          label: item.libelle,
        })));

        setMetiers(metiersData.map(item => ({
          value: item.id,
          label: item.libelle
        })));

      } catch (error) {
        console.error("Erreur lors du chargement:", error);
        alert(`Erreur lors du chargement des données: ${error.message}`);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: null
      }));
    }
  };

  // Nouvelles fonctions de gestion avec coefficients
  const handleChangeCompetences = (newCompetences) => {
    setFormData(prev => ({
      ...prev,
      competences: newCompetences.map(item => ({
        id: parseInt(item.selectValue || item.value),
        coeff: parseInt(item.inputValue || 2)
      }))
    }));
  };

  // Fonction pour vérifier si l'utilisateur a le rôle DEPARTEMENT
  // Fonction pour vérifier si l'utilisateur a le rôle DEPARTEMENT
  const isDepartementUser = () => {
    if (!userInfo || !userInfo.roles) return false;
    
    // CORRECTION : Les rôles sont des objets avec une propriété "authority"
    const hasRole = userInfo.roles.some(role => 
      role.authority === "ROLE_DEPARTEMENT" || role.authority === "DEPARTEMENT"
    );
    
    console.log("User roles:", userInfo.roles);
    console.log("Has DEPARTEMENT role:", hasRole);
    return hasRole;
  };

  // Fonction pour obtenir le département de l'utilisateur
  const getUserDepartement = () => {
    return userInfo && userInfo.departement;
  };

  const handleChangeLangues = (newLangues) => {
    setFormData(prev => ({
      ...prev,
      langues: newLangues.map(item => ({
        id: parseInt(item.selectValue || item.value),
        coeff: parseInt(item.inputValue || 2)
      }))
    }));
  };

  // MODIFICATION PRINCIPALE : Utilisation de MultiSelectFiliere comme dans FormCandidat
  const handleChangeDiplomeFilieres = (newDiplomes) => {
    setFormData(prev => ({
      ...prev,
      diplomeFilieres: newDiplomes.map(id => parseInt(id)) // Format simple comme dans FormCandidat
    }));
  };

  const handleNextPage = () => {
    if (validateCurrentPage()) {
      setPage(prev => prev + 1);
    }
  };

  const handlePrevPage = () => {
    setPage(prev => prev - 1);
  };

  // Validation de la page courante
  // Validation de la page courante
  const validateCurrentPage = () => {
    const newErrors = {};
    
    console.log("=== VALIDATION PAGE " + page + " ===");
    console.log("formData:", formData);
    console.log("isDepartementUser:", isDepartementUser());
    
    if (page === 0) {
      if (!formData.metierId) {
        newErrors.metierId = "Le métier est requis";
        console.log("❌ Métier manquant");
      }
      
      if (!formData.departementId) {
        newErrors.departementId = "Le département est requis";
        console.log("❌ Département manquant");
      }
      
      if (!formData.nbPosteDispo || formData.nbPosteDispo < 1) {
        newErrors.nbPosteDispo = "Le nombre de postes doit être supérieur à 0";
        console.log("❌ Nombre de postes invalide:", formData.nbPosteDispo);
      }
    } else if (page === 1) {
      if (formData.minAge && formData.maxAge && parseInt(formData.minAge) > parseInt(formData.maxAge)) {
        newErrors.maxAge = "L'âge maximum doit être supérieur à l'âge minimum";
        console.log("❌ Âge min > max");
      }
    }

    console.log("Erreurs trouvées:", newErrors);
    console.log("Validation passée:", Object.keys(newErrors).length === 0);
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };
  // Réinitialisation du formulaire
  const handleReset = () => {
      setFormData({
        statut: 0,
        minAge: "",
        maxAge: "",
        nbPosteDispo: "",
        minExperience: "",
        coeffAge: 2,
        coeffExperience: 2,
        metierId: "",
        departementId: "",
        competences: [],
        langues: [],
        diplomeFilieres: []
      });
      setErrors({});
      setPage(0);
    };

    const handleSubmit = async (e) => {
      e.preventDefault();
      
      if (!validateCurrentPage()) return;

      try {
          setIsSubmitting(true);
          
          // Préparer les données selon le format attendu par l'API
          const besoinData = {
              statut: parseInt(formData.statut),
              minAge: formData.minAge ? parseInt(formData.minAge) : null,
              maxAge: formData.maxAge ? parseInt(formData.maxAge) : null,
              nbPosteDispo: parseInt(formData.nbPosteDispo),
              minExperience: formData.minExperience ? parseInt(formData.minExperience) : null,
              coeffAge: parseInt(formData.coeffAge),
              coeffExperience: parseInt(formData.coeffExperience),
              metier: formData.metierId ? { id: parseInt(formData.metierId) } : null,
      // CORRECTION : Format objet pour departement (ou null si géré par backend)
              departement: formData.departementId ? { id: parseInt(formData.departementId) } : null,
              competences: formData.competences,
              langues: formData.langues,
              diplomeFilieres: formData.diplomeFilieres
          };

          // Si vous avez l'information du rôle dans le frontend, vous pouvez faire :
          // const userRole = getUserRole(); // À implémenter selon votre auth
          // if (userRole !== 'DEPARTEMENT') {
          //     besoinData.departementId = parseInt(formData.departementId);
          // }

          console.log("Données envoyées:", besoinData);
          
          await besoinService.createBesoin(besoinData);
          
          // Rediriger vers la liste des annonces
          navigate('/annonces');
      } catch (error) {
          console.error('Erreur lors de la création:', error);
          setErrors({ general: 'Erreur lors de la création de l\'annonce' });
      } finally {
          setIsSubmitting(false);
      }
  };

  // Affichage du loading
  if (loading || userLoading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-4xl mx-auto">
          <div className="bg-white rounded-2xl shadow-xl p-8 text-center">
            <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4"></div>
            <h3 className="text-xl font-semibold text-gray-900 mb-2">
              {userLoading ? "Chargement de votre profil..." : "Chargement des données"}
            </h3>
            <p className="text-gray-600">Veuillez patienter...</p>
          </div>
        </div>
      </div>
    );
  }

  // Composant de progression
  const ProgressBar = () => (
    <div className="mb-8">
      <div className="flex justify-between items-center mb-4">
        {progressSteps.map((step, index) => (
          <div key={index} className="flex flex-col items-center flex-1">
            <div className={`w-10 h-10 rounded-full flex items-center justify-center border-2 ${
              index <= page 
                ? 'bg-blue-600 border-blue-600 text-white' 
                : 'bg-white border-gray-300 text-gray-500'
            } transition-all duration-300`}>
              {index + 1}
            </div>
            <span className={`text-sm mt-2 text-center ${
              index <= page ? 'text-blue-600 font-medium' : 'text-gray-500'
            }`}>
              {step}
            </span>
          </div>
        ))}
      </div>
      <div className="w-full bg-gray-200 rounded-full h-2">
        <div 
          className="bg-blue-600 h-2 rounded-full transition-all duration-500 ease-out"
          style={{ width: `${((page + 1) / progressSteps.length) * 100}%` }}
        ></div>
      </div>
    </div>
  );

  // Page 0 - Informations générales (identique)
  if (page === 0) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-4xl mx-auto">
          <div className="bg-white rounded-2xl shadow-xl p-8">
            <div className="mb-8 text-center">
              <h2 className="text-4xl font-bold text-gray-900 bg-gradient-to-r from-blue-600 to-indigo-700 bg-clip-text text-transparent">
                Créer une Annonce
              </h2>
              <p className="mt-3 text-lg text-gray-600">Informations générales du poste à pourvoir</p>
            </div>

            <ProgressBar />

            {errors.general && (
              <div className="mb-6 bg-red-50 border border-red-200 rounded-xl p-4">
                <p className="text-red-600 font-medium">{errors.general}</p>
              </div>
            )}

            <form className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <Select
                  label="Statut de l'annonce"
                  name="statut"
                  value={formData.statut}
                  onChange={handleChange}
                  options={statutOptions}
                  error={errors.statut}
                />

                <InputNumber
                  label="Nombre de postes disponibles"
                  name="nbPosteDispo"
                  value={formData.nbPosteDispo}
                  onChange={handleChange}
                  required
                  error={errors.nbPosteDispo}
                  min="1"
                  placeholder="Ex: 3"
                />
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <Select
                  label="Métier *"
                  name="metierId"
                  value={formData.metierId}
                  onChange={handleChange}
                  options={metiers}
                  required
                  error={errors.metierId}
                  placeholder="Sélectionnez un métier"
                />

                {/* MODIFICATION : Conditionner l'affichage du sélecteur de département */}
                {isDepartementUser() ? (
                  // Si utilisateur DEPARTEMENT, afficher son département en lecture seule
                  <div className="space-y-2">
                    <label className="block text-sm font-medium text-gray-700">
                      Département *
                    </label>
                    <div className="p-3 bg-gray-100 rounded-md border border-gray-300">
                      <p className="text-gray-900 font-medium">
                        {getUserDepartement()?.libelle || "Votre département"}
                      </p>
                      <p className="text-sm text-gray-500 mt-1">
                        Votre département est automatiquement sélectionné
                      </p>
                    </div>
                    <input
                      type="hidden"
                      name="departementId"
                      value={formData.departementId}
                    />
                  </div>
                ) : (
                  // Si ADMIN ou RH, afficher le sélecteur normal
                  <Select
                    label="Département *"
                    name="departementId"
                    value={formData.departementId}
                    onChange={handleChange}
                    options={departements}
                    required
                    error={errors.departementId}
                    placeholder="Sélectionnez un département"
                  />
                )}

              </div>

              <div className="flex justify-between pt-6 border-t border-gray-200">
                <Button 
                  type="button" 
                  variant="secondary" 
                  onClick={handleReset}
                  className="px-6 py-3"
                >
                  ↺ Réinitialiser
                </Button>
                <Button 
                  type="button" 
                  onClick={handleNextPage}
                  className="px-8 py-3 bg-gradient-to-r from-blue-600 to-indigo-700 hover:from-blue-700 hover:to-indigo-800"
                >
                  Suivant →
                </Button>
              </div>
            </form>
          </div>
        </div>
      </div>
    );
  }

  // Page 1 - Critères de sélection (identique)
  if (page === 1) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-4xl mx-auto">
          <div className="bg-white rounded-2xl shadow-xl p-8">
            <div className="mb-8 text-center">
              <h2 className="text-4xl font-bold text-gray-900 bg-gradient-to-r from-blue-600 to-indigo-700 bg-clip-text text-transparent">
                Critères de Sélection
              </h2>
              <p className="mt-3 text-lg text-gray-600">Définissez les critères pour les candidats</p>
            </div>

            <ProgressBar />

            {errors.general && (
              <div className="mb-6 bg-red-50 border border-red-200 rounded-xl p-4">
                <p className="text-red-600 font-medium">{errors.general}</p>
              </div>
            )}

            <form className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <InputNumber
                  label="Âge minimum"
                  name="minAge"
                  value={formData.minAge}
                  onChange={handleChange}
                  error={errors.minAge}
                  min="18"
                  max="70"
                  placeholder="Ex: 21"
                  helperText="Optionnel - 18 ans minimum"
                />
                <InputNumber
                  label="Âge maximum"
                  name="maxAge"
                  value={formData.maxAge}
                  onChange={handleChange}
                  error={errors.maxAge}
                  min="18"
                  max="70"
                  placeholder="Ex: 55"
                  helperText="Optionnel"
                />
              </div>

              <InputNumber
                label="Expérience minimale (années)"
                name="minExperience"
                value={formData.minExperience}
                onChange={handleChange}
                error={errors.minExperience}
                min="0"
                max="50"
                placeholder="Ex: 2"
                helperText="Nombre d'années d'expérience requise"
              />

              <div className="bg-gray-50 rounded-xl p-6">
                <h3 className="text-lg font-semibold text-gray-900 mb-4">Coefficients d'importance</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <Select
                    label="Importance de l'âge"
                    name="coeffAge"
                    value={formData.coeffAge}
                    onChange={handleChange}
                    options={coefficientOptions}
                    helperText="Poids de ce critère dans la sélection"
                  />
                  <Select
                    label="Importance de l'expérience"
                    name="coeffExperience"
                    value={formData.coeffExperience}
                    onChange={handleChange}
                    options={coefficientOptions}
                    helperText="Poids de ce critère dans la sélection"
                  />
                </div>
              </div>

              <div className="flex justify-between pt-6 border-t border-gray-200">
                <Button 
                  type="button" 
                  variant="secondary" 
                  onClick={handlePrevPage}
                  className="px-6 py-3"
                >
                  ← Précédent
                </Button>
                <Button 
                  type="button" 
                  onClick={handleNextPage}
                  className="px-8 py-3 bg-gradient-to-r from-blue-600 to-indigo-700 hover:from-blue-700 hover:to-indigo-800"
                >
                  Suivant →
                </Button>
              </div>
            </form>
          </div>
        </div>
      </div>
    );
  }

  // Page 2 - Compétences et langues (MODIFIÉE avec MultiSelectFiliere)
  if (page === 2) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-4xl mx-auto">
          <div className="bg-white rounded-2xl shadow-xl p-8">
            <div className="mb-8 text-center">
              <h2 className="text-4xl font-bold text-gray-900 bg-gradient-to-r from-blue-600 to-indigo-700 bg-clip-text text-transparent">
                Compétences Requises
              </h2>
              <p className="mt-3 text-lg text-gray-600">Sélectionnez les compétences et langues nécessaires avec leurs coefficients</p>
            </div>

            <ProgressBar />

            {errors.general && (
              <div className="mb-6 bg-red-50 border border-red-200 rounded-xl p-4">
                <p className="text-red-600 font-medium">{errors.general}</p>
              </div>
            )}

            <form className="space-y-8" onSubmit={handleSubmit}>
              <div className="space-y-6">
                <MultiSelectInputNumber
                  label="Compétences techniques requises"
                  name="competences"
                  value={formData.competences}
                  onChange={handleChangeCompetences}
                  options={competences}
                  error={errors.competences}
                  selectLabel="Compétence"
                  inputLabel="Coefficient"
                  inputMin="1"
                  inputMax="4"
                  inputPlaceholder="1-4"
                  helperText="Sélectionnez les compétences et leur importance (1=Faible, 4=Très important)"
                />

                <MultiSelectInputNumber
                  label="Langues requises"
                  name="langues"
                  value={formData.langues}
                  onChange={handleChangeLangues}
                  options={langues}
                  error={errors.langues}
                  selectLabel="Langue"
                  inputLabel="Coefficient"
                  inputMin="1"
                  inputMax="4"
                  inputPlaceholder="1-4"
                  helperText="Sélectionnez les langues et leur importance (1=Faible, 4=Très important)"
                />

                {/* MODIFICATION : Utilisation de MultiSelectFiliere comme dans FormCandidat */}
                <div className="space-y-4">
                  <label htmlFor="" className="block text-sm font-medium text-gray-700 mb-1">
                    Diplômes et filières requis <span className="text-red-500">*</span>
                  </label>
                  <MultiSelectFiliere
                    value={formData.diplomeFilieres}
                    onChange={handleChangeDiplomeFilieres}
                  />
                  {errors.diplomeFilieres && (
                    <p className="text-red-500 text-sm mt-1">{errors.diplomeFilieres}</p>
                  )}
                  <p className="text-gray-500 text-sm">
                    Sélectionnez les diplômes et filières requis pour ce poste
                  </p>
                </div>
              </div>

              <div className="bg-blue-50 border border-blue-200 rounded-xl p-6">
                <h3 className="text-lg font-semibold text-blue-900 mb-2">Récapitulatif</h3>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                  <div>
                    <span className="font-medium text-gray-700">Métier:</span>
                    <p className="text-gray-900">{metiers.find(m => m.value == formData.metierId)?.label || 'Non sélectionné'}</p>
                  </div>
                  <div>
                    <span className="font-medium text-gray-700">Postes:</span>
                    <p className="text-gray-900">{formData.nbPosteDispo || 0}</p>
                  </div>
                  <div>
                    <span className="font-medium text-gray-700">Diplômes:</span>
                    <p className="text-gray-900">{formData.diplomeFilieres.length} sélectionnés</p>
                  </div>
                </div>
              </div>

              <div className="flex justify-between pt-6 border-t border-gray-200">
                <Button 
                  type="button" 
                  variant="secondary" 
                  onClick={handlePrevPage}
                  className="px-6 py-3"
                >
                  ← Précédent
                </Button>
                <div className="space-x-4">
                  <Button 
                    type="button" 
                    variant="secondary" 
                    onClick={handleReset}
                    className="px-6 py-3"
                  >
                    ↺ Tout réinitialiser
                  </Button>
                  <Button 
                    type="submit" 
                    disabled={isSubmitting}
                    className="px-8 py-3 bg-gradient-to-r from-green-600 to-emerald-700 hover:from-green-700 hover:to-emerald-800"
                  >
                    {isSubmitting ? (
                      <div className="flex items-center gap-2">
                        <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                        Création...
                      </div>
                    ) : (
                      "✅ Créer l'annonce"
                    )}
                  </Button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    );
  }
};

export default FormAnnonce;