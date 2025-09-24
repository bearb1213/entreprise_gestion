import React, { useState, useEffect } from "react";
import InputText from "../../form/InputText";
import InputNumber from "../../form/InputNumber";
import Select from "../../form/Select";
import Button from "../../form/Button";
import MultiCheckbox from "../../form/MultiCheckbox";
import MultiSelect from "../../form/MultiSelect";

const FormAnnonce = () => {
  const [page, setPage] = useState(0);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errors, setErrors] = useState({});

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



  // Options pour les coefficients
  const coefficientOptions = [
    { value: 1, label: "Faible" },
    { value: 2, label: "Moyen" },
    { value: 3, label: "Important" },
    { value: 4, label: "Très important" }
  ];

  // Chargement des données initiales
  useEffect(() => {
    const loadInitialData = async () => {
      // Métiers
      setMetiers([
        { value: 1, label: "Développeur Frontend" },
        { value: 2, label: "Développeur Backend" },
        { value: 3, label: "Designer UI/UX" },
        { value: 4, label: "Chef de Projet" },
        { value: 5, label: "Data Scientist" },
      ]);

      // Départements
      setDepartements([
        { value: 1, label: "Informatique" },
        { value: 2, label: "Ressources Humaines" },
        { value: 3, label: "Marketing" },
        { value: 4, label: "Finance" },
      ]);

      // Compétences
      setCompetences([
        { value: 1, label: "React" },
        { value: 2, label: "Node.js" },
        { value: 3, label: "MongoDB" },
        { value: 4, label: "TypeScript" },
        { value: 5, label: "Python" },
        { value: 6, label: "Figma" },
      ]);

      // Langues
      setLangues([
        { value: 1, label: "Français" },
        { value: 2, label: "Anglais" },
        { value: 3, label: "Espagnol" },
        { value: 4, label: "Allemand" },
      ]);
    };

    loadInitialData();
  }, []);
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