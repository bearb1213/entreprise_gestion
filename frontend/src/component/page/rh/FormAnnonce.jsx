import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import InputText from "../../form/InputText";
import InputNumber from "../../form/InputNumber";
import Select from "../../form/Select";
import Button from "../../form/Button";
import MultiCheckbox from "../../form/MultiCheckbox";
import MultiSelect from "../../form/MultiSelect";
import besoinService from "../../../services/besoinService";

const FormAnnonce = () => {
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(true);

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
    competenceIds: [],
    langueIds: [],
  });

  const statutOptions = [
    { value: 0, label: "Ouvert" },
    { value: 1, label: "Fermé" },
  ];

  const coefficientOptions = [
    { value: 1, label: "Faible" },
    { value: 2, label: "Moyen" },
    { value: 3, label: "Important" },
    { value: 4, label: "Très important" }
  ];

  useEffect(() => {
    const loadInitialData = async () => {
      try {
        setLoading(true);
        
        const [metiersData, departementsData, competencesData, languesData] = await Promise.all([
          besoinService.getMetiers(),
          besoinService.getDepartements(),
          besoinService.getCompetences(),
          besoinService.getLangues()
        ]);

        setMetiers(metiersData);
        setDepartements(departementsData);
        setCompetences(competencesData);
        setLangues(languesData);
      } catch (error) {
        console.error('Erreur lors du chargement des données:', error);
        setErrors({ general: 'Erreur lors du chargement des données' });
      } finally {
        setLoading(false);
      }
    };

    loadInitialData();
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

  const handleChangeCompetences = (selectedCompetences) => {
    const competenceIds = selectedCompetences.map(comp => comp.value);
    setFormData(prev => ({
      ...prev,
      competenceIds
    }));
  };

  const handleChangeLangues = (selectedLangues) => {
    const langueIds = selectedLangues.map(langue => langue.value);
    setFormData(prev => ({
      ...prev,
      langueIds
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
  const validateCurrentPage = () => {
    const newErrors = {};
    
    if (page === 0) {
      if (!formData.metierId) newErrors.metierId = "Le métier est requis";
      if (!formData.departementId) newErrors.departementId = "Le département est requis";
      if (!formData.nbPosteDispo || formData.nbPosteDispo < 1) {
        newErrors.nbPosteDispo = "Le nombre de postes doit être supérieur à 0";
      }
    } else if (page === 1) {
      if (formData.minAge && formData.maxAge && parseInt(formData.minAge) > parseInt(formData.maxAge)) {
        newErrors.maxAge = "L'âge maximum doit être supérieur à l'âge minimum";
      }
    }

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
      competenceIds: [],
      langueIds: [],
    });
    setErrors({});
    setPage(0);
  };

  // Soumission du formulaire
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
        metierId: parseInt(formData.metierId),
        departementId: parseInt(formData.departementId),
        competences: formData.competenceIds.map(id => ({ id: parseInt(id), coeff: 2 })),
        langues: formData.langueIds.map(id => ({ id: parseInt(id), coeff: 2 })),
        diplomeFilieres: [] // Pas de diplômes dans ce formulaire pour l'instant
      };

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
  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-2xl mx-auto">
          <div className="bg-white py-8 px-6 shadow rounded-lg sm:px-10 text-center">
            <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            <p className="mt-2 text-gray-600">Chargement des données...</p>
          </div>
        </div>
      </div>
    );
  }
  // Page 0 - Informations générales
  if (page === 0) {
    return (
      <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-2xl mx-auto">
          <div className="bg-white py-8 px-6 shadow rounded-lg sm:px-10">
            <div className="mb-8 text-center">
              <h2 className="text-3xl font-extrabold text-gray-900">Créer une Annonce</h2>
              <p className="mt-2 text-gray-600">Informations générales du poste</p>
            </div>

            {errors.general && (
              <div className="mb-4 bg-red-50 border border-red-200 rounded-lg p-4">
                <p className="text-red-600">{errors.general}</p>
              </div>
            )}

            <form className="space-y-6">
              <Select
                label="Statut de l'annonce"
                name="statut"
                value={formData.statut}
                onChange={handleChange}
                options={statutOptions}
                error={errors.statut}
              />

              <Select
                label="Métier"
                name="metierId"
                value={formData.metierId}
                onChange={handleChange}
                options={metiers}
                required
                error={errors.metierId}
                placeholder="Sélectionnez un métier"
              />

              <Select
                label="Département"
                name="departementId"
                value={formData.departementId}
                onChange={handleChange}
                options={departements}
                required
                error={errors.departementId}
                placeholder="Sélectionnez un département"
              />

              <InputNumber
                label="Nombre de postes disponibles"
                name="nbPosteDispo"
                value={formData.nbPosteDispo}
                onChange={handleChange}
                required
                error={errors.nbPosteDispo}
                min="1"
              />

              <div className="flex justify-between pt-4">
                <Button type="button" variant="secondary" onClick={handleReset}>
                  Réinitialiser
                </Button>
                <Button type="button" onClick={handleNextPage}>
                  Suivant
                </Button>
              </div>
            </form>
          </div>
        </div>
      </div>
    );
  }

  // Page 1 - Critères de sélection
  if (page === 1) {
    return (
      <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-2xl mx-auto">
          <div className="bg-white py-8 px-6 shadow rounded-lg sm:px-10">
            <div className="mb-8 text-center">
              <h2 className="text-3xl font-extrabold text-gray-900">Critères de Sélection</h2>
              <p className="mt-2 text-gray-600">Définissez les critères pour les candidats</p>
            </div>

            {errors.general && (
              <div className="mb-4 bg-red-50 border border-red-200 rounded-lg p-4">
                <p className="text-red-600">{errors.general}</p>
              </div>
            )}

            <form className="space-y-6">
              <div className="grid grid-cols-2 gap-4">
                <InputNumber
                  label="Âge minimum"
                  name="minAge"
                  value={formData.minAge}
                  onChange={handleChange}
                  error={errors.minAge}
                  min="18"
                  max="70"
                  helperText="Optionnel"
                />
                <InputNumber
                  label="Âge maximum"
                  name="maxAge"
                  value={formData.maxAge}
                  onChange={handleChange}
                  error={errors.maxAge}
                  required
                  
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
                helperText="Optionnel"
              />

              <div className="grid grid-cols-2 gap-4">
                <Select
                  label="Coefficient Âge"
                  name="coeffAge"
                  value={formData.coeffAge}
                  onChange={handleChange}
                  options={coefficientOptions}
                  helperText="Importance de ce critère"
                />
                <Select
                  label="Coefficient Expérience"
                  name="coeffExperience"
                  value={formData.coeffExperience}
                  onChange={handleChange}
                  options={coefficientOptions}
                  helperText="Importance de ce critère"
                />
              </div>

              <div className="flex justify-between pt-4">
                <Button type="button" variant="secondary" onClick={handlePrevPage}>
                  Précédent
                </Button>
                <Button type="button" onClick={handleNextPage}>
                  Suivant
                </Button>
              </div>
            </form>
          </div>
        </div>
      </div>
    );
  }

  // Page 2 - Compétences et langues
  if (page === 2) {
    return (
      <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-2xl mx-auto">
          <div className="bg-white py-8 px-6 shadow rounded-lg sm:px-10">
            <div className="mb-8 text-center">
              <h2 className="text-3xl font-extrabold text-gray-900">Compétences Requises</h2>
              <p className="mt-2 text-gray-600">Sélectionnez les compétences et langues nécessaires</p>
            </div>

            {errors.general && (
              <div className="mb-4 bg-red-50 border border-red-200 rounded-lg p-4">
                <p className="text-red-600">{errors.general}</p>
              </div>
            )}

            <form className="space-y-6" onSubmit={handleSubmit}>
              <MultiCheckbox
                label="Compétences techniques"
                name="competences"
                value={competences.filter(comp => formData.competenceIds.includes(comp.value))}
                onChange={handleChangeCompetences}
                options={competences}
                helperText="Sélectionnez les compétences requises"
              />

              <MultiCheckbox
                label="Langues requises"
                name="langues"
                value={langues.filter(langue => formData.langueIds.includes(langue.value))}
                onChange={handleChangeLangues}
                options={langues}
                helperText="Sélectionnez les langues requises"
              />

              <div className="flex justify-between pt-4">
                <Button type="button" variant="secondary" onClick={handlePrevPage}>
                  Précédent
                </Button>
                <div className="space-x-2">
                  <Button type="button" variant="secondary" onClick={handleReset}>
                    Réinitialiser
                  </Button>
                  <Button type="submit" disabled={isSubmitting}>
                    {isSubmitting ? "Création..." : "Créer l'annonce"}
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