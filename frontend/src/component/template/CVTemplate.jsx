import React from 'react';

const CVTemplate = ({ cvData }) => {
  const {
    nom,
    prenom,
    date_naissance,
    image,
    genre,
    email,
    telephone,
    ville,
    description,
    langues = [],
    filiere = [],
    competences = [],
    metiers = []
  } = cvData;

  // Calcul de l'âge
  const calculateAge = (birthDate) => {
    const today = new Date();
    const birth = new Date(birthDate);
    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
      age--;
    }
    
    return age;
  };

  // Formater la date
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  return (
    <div className="max-w-4xl mx-auto bg-white shadow-2xl rounded-lg overflow-hidden">
      {/* En-tête du CV */}
      <div className="bg-gradient-to-r from-blue-600 to-blue-800 text-white p-8">
        <div className="flex items-center justify-between">
          <div className="flex-1">
            <h1 className="text-4xl font-bold mb-2">{prenom} {nom}</h1>
            <p className="text-xl opacity-90">{filiere[0]?.filiere?.libelle || 'Professionnel'}</p>
          </div>
          
          {image && (
            <div className="w-32 h-32 rounded-full border-4 border-white overflow-hidden shadow-lg">
              <img 
                src={image} 
                alt={`${prenom} ${nom}`}
                className="w-full h-full object-cover"
              />
            </div>
          )}
        </div>
      </div>

      {/* Corps du CV */}
      <div className="p-8">
        {/* Section Informations personnelles */}
        <div className="mb-8">
          <h2 className="text-2xl font-bold text-blue-800 mb-4 border-b-2 border-blue-200 pb-2">
            Informations Personnelles
          </h2>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="flex items-center">
              <span className="w-6 text-blue-600 mr-2">🎂</span>
              <span>{formatDate(date_naissance)} ({calculateAge(date_naissance)} ans)</span>
            </div>
            
            <div className="flex items-center">
              <span className="w-6 text-blue-600 mr-2">👤</span>
              <span>{genre === 'M' ? 'Homme' : genre === 'F' ? 'Femme' : genre}</span>
            </div>
            
            <div className="flex items-center">
              <span className="w-6 text-blue-600 mr-2">📧</span>
              <a href={`mailto:${email}`} className="text-blue-600 hover:underline">
                {email}
              </a>
            </div>
            
            <div className="flex items-center">
              <span className="w-6 text-blue-600 mr-2">📞</span>
              <a href={`tel:${telephone}`} className="text-blue-600 hover:underline">
                {telephone}
              </a>
            </div>
            
            <div className="flex items-center">
              <span className="w-6 text-blue-600 mr-2">📍</span>
              <span>{ville}</span>
            </div>
          </div>
        </div>

        {/* Section Description */}
        {description && (
          <div className="mb-8">
            <h2 className="text-2xl font-bold text-blue-800 mb-4 border-b-2 border-blue-200 pb-2">
              Profil
            </h2>
            <p className="text-gray-700 leading-relaxed">{description}</p>
          </div>
        )}

        {/* Section Formation */}
        {filiere.length > 0 && (
          <div className="mb-8">
            <h2 className="text-2xl font-bold text-blue-800 mb-4 border-b-2 border-blue-200 pb-2">
              Formation
            </h2>
            
            <div className="space-y-4">
              {filiere.map((item, index) => (
                <div key={index} className="bg-gray-50 p-4 rounded-lg border-l-4 border-blue-500">
                  <h3 className="font-semibold text-lg text-gray-800">
                    {item.filiere?.libelle}
                  </h3>
                  <p className="text-blue-600 font-medium">
                    {item.diplome?.libelle}
                  </p>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Section Expérience professionnelle */}
        {metiers.length > 0 && (
          <div className="mb-8">
            <h2 className="text-2xl font-bold text-blue-800 mb-4 border-b-2 border-blue-200 pb-2">
              Expérience Professionnelle
            </h2>
            
            <div className="space-y-6">
              {metiers.map((metier, index) => (
                <div key={index} className="bg-white p-4 rounded-lg border-l-4 border-green-500 shadow-sm">
                  <div className="flex justify-between items-start mb-2">
                    <h3 className="font-semibold text-lg text-gray-800">
                      {metier.metier?.libelle}
                    </h3>
                    <span className="bg-green-100 text-green-800 px-3 py-1 rounded-full text-sm font-medium">
                      {metier.nb_annee} an{metier.nb_annee > 1 ? 's' : ''} d'expérience
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Section Compétences */}
        {competences.length > 0 && (
          <div className="mb-8">
            <h2 className="text-2xl font-bold text-blue-800 mb-4 border-b-2 border-blue-200 pb-2">
              Compétences
            </h2>
            
            <div className="flex flex-wrap gap-2">
              {competences.map((competence, index) => (
                <span
                  key={index}
                  className="bg-blue-100 text-blue-800 px-4 py-2 rounded-full text-sm font-medium"
                >
                  {competence.libelle}
                </span>
              ))}
            </div>
          </div>
        )}

        {/* Section Langues */}
        {langues.length > 0 && (
          <div className="mb-8">
            <h2 className="text-2xl font-bold text-blue-800 mb-4 border-b-2 border-blue-200 pb-2">
              Langues
            </h2>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {langues.map((langue, index) => (
                <div key={index} className="flex items-center">
                  <span className="w-6 text-blue-600 mr-2">🌐</span>
                  <span className="font-medium">{langue.libelle}</span>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Pied de page */}
      <div className="bg-gray-100 p-4 text-center text-gray-600 text-sm">
        <p>CV généré le {new Date().toLocaleDateString('fr-FR')}</p>
      </div>
    </div>
  );
};

export default CVTemplate;