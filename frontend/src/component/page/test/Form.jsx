import React, { useState } from 'react';
import InputText from '../../form/InputText';
import Button from '../../form/Button';
import Checkbox from '../../form/Checkbox';
import RadioGroup from '../../form/RadioGroup';
import Select from '../../form/Select';
import TextArea from '../../form/TextArea';
import InputDate from '../../form/InputDate';


// Composant principal Form
const Form = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    message: '',
    country: '',
    newsletter: false,
    gender: '',
    terms: false,
    date:''
  });

  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value,
    });

    // Clear error when user starts typing
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: '',
      });
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.firstName.trim()) newErrors.firstName = 'Le prénom est requis';
    if (!formData.lastName.trim()) newErrors.lastName = 'Le nom est requis';
    if (!formData.email.trim()) {
      newErrors.email = 'L\'email est requis';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'L\'email est invalide';
    }
    if (!formData.country) newErrors.country = 'Le pays est requis';
    if (!formData.gender) newErrors.gender = 'Le genre est requis';
    if (!formData.terms) newErrors.terms = 'Vous devez accepter les conditions';

    return newErrors;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const newErrors = validateForm();

    if (Object.keys(newErrors).length === 0) {
      setIsSubmitting(true);
      // Simuler une requête API
      setTimeout(() => {
        console.log('Formulaire soumis:', formData);
        alert('Formulaire soumis avec succès!');
        setIsSubmitting(false);
      }, 1500);
    } else {
      setErrors(newErrors);
    }
  };

  const countryOptions = [
    { value: 'fr', label: 'France' },
    { value: 'be', label: 'Belgique' },
    { value: 'ch', label: 'Suisse' },
    { value: 'ca', label: 'Canada' },
    { value: 'other', label: 'Autre' },
  ];

  const genderOptions = [
    { value: 'male', label: 'Homme' },
    { value: 'female', label: 'Femme' },
    { value: 'other', label: 'Autre' },
  ];

  return (
    <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-2xl mx-auto">
        <div className="bg-white py-8 px-6 shadow rounded-lg sm:px-10">
          <div className="mb-8 text-center">
            <h2 className="text-3xl font-extrabold text-gray-900">Formulaire de Contact</h2>
            <p className="mt-2 text-gray-600">Remplissez le formulaire ci-dessous</p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <InputText
                label="Prénom"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                required
                error={errors.firstName}
              />
              <InputText
                label="Nom"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                required
                error={errors.lastName}
              />
            </div>

            <InputText
              label="Email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              required
              error={errors.email}
            />

            <InputText
              label="Téléphone (optionnel)"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
            />

            <Select
              label="Pays"
              name="country"
              value={formData.country}
              onChange={handleChange}
              options={countryOptions}
              required
              error={errors.country}
            />

            <RadioGroup
              label="Genre"
              name="gender"
              value={formData.gender}
              onChange={handleChange}
              options={genderOptions}
              required
              error={errors.gender}
            />

            <TextArea
              label="Message"
              name="message"
              value={formData.message}
              onChange={handleChange}
              rows={5}
              placeholder="Votre message ici..."
            />

            <Checkbox
              label="Je souhaite m'abonner à la newsletter"
              name="newsletter"
              checked={formData.newsletter}
              onChange={handleChange}
            />

            <Checkbox
              label="J'accepte les conditions d'utilisation"
              name="terms"
              checked={formData.terms}
              onChange={handleChange}
              required
              error={errors.terms}
            />

            <InputDate
              label="date naissance"
              name="naissance"
              value={formData.date}
              onChange={handleChange}
              
            />

            <div className="flex justify-between pt-4">
              <Button type="button" variant="secondary">
                Annuler
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Envoi en cours...' : 'Soumettre'}
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Form;