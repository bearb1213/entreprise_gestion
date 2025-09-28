import React from 'react';

const CVPreview = ({ cvData, onViewFull, onEdit, onDownload }) => {
  if (!cvData) {
    return (
      <div className="bg-gray-100 rounded-lg p-6 text-center">
        <p className="text-gray-500">Aucun CV à afficher</p>
      </div>
    );
  }

  const { nom, prenom, image, ville, email, telephone, description, metiers, competences } = cvData;

  return (
    <div className="bg-white rounded-xl shadow-lg border border-gray-200 overflow-hidden max-w-2xl mx-auto">
      {/* Header avec photo */}
      <div className="bg-gradient-to-r from-blue-600 to-blue-800 text-white p-6">
        <div className="flex items-center space-x-4">
          {image && (
            <img
              src={image}
              alt={`${prenom} ${nom}`}
              className="w-16 h-16 rounded-full border-4 border-white object-cover"
            />
          )}
          <div>
            <h2 className="text-2xl font-bold">{prenom} {nom}</h2>
            <p className="text-blue-100">{metiers?.[0]?.metier?.libelle || 'Professionnel'}</p>
            <p className="text-blue-100 text-sm">{ville}</p>
          </div>
        </div>
      </div>

      {/* Informations de contact */}
      <div className="p-6 border-b border-gray-200">
        <h3 className="text-lg font-semibold text-gray-800 mb-3">Contact</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-2 text-sm">
          {email && (
            <div className="flex items-center">
              <span className="text-gray-500 mr-2">📧</span>
              <a href={`mailto:${email}`} className="text-blue-600 hover:underline">{email}</a>
            </div>
          )}
          {telephone && (
            <div className="flex items-center">
              <span className="text-gray-500 mr-2">📞</span>
              <a href={`tel:${telephone}`} className="text-blue-600 hover:underline">{telephone}</a>
            </div>
          )}
        </div>
      </div>

      {/* Description */}
      {description && (
        <div className="p-6 border-b border-gray-200">
          <h3 className="text-lg font-semibold text-gray-800 mb-3">Profil</h3>
          <p className="text-gray-600 text-sm leading-relaxed line-clamp-3">
            {description}
          </p>
        </div>
      )}

      {/* Compétences */}
      {competences && competences.length > 0 && (
        <div className="p-6 border-b border-gray-200">
          <h3 className="text-lg font-semibold text-gray-800 mb-3">Compétences</h3>
          <div className="flex flex-wrap gap-2">
            {competences.slice(0, 4).map((competence, index) => (
              <span
                key={index}
                className="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-xs font-medium"
              >
                {competence.libelle}
              </span>
            ))}
            {competences.length > 4 && (
              <span className="bg-gray-100 text-gray-600 px-3 py-1 rounded-full text-xs">
                +{competences.length - 4} autres
              </span>
            )}
          </div>
        </div>
      )}

      {/* Expérience */}
      {metiers && metiers.length > 0 && (
        <div className="p-6">
          <h3 className="text-lg font-semibold text-gray-800 mb-3">Expérience</h3>
          <div className="space-y-2">
            {metiers.slice(0, 2).map((metier, index) => (
              <div key={index} className="text-sm">
                <p className="font-medium text-gray-700">{metier.metier?.libelle}</p>
                <p className="text-gray-500">{metier.nb_annee} an{metier.nb_annee > 1 ? 's' : ''} d'expérience</p>
              </div>
            ))}
            {metiers.length > 2 && (
              <p className="text-sm text-gray-500">+{metiers.length - 2} expérience(s) supplémentaire(s)</p>
            )}
          </div>
        </div>
      )}

      {/* Actions */}
      <div className="bg-gray-50 p-4 flex justify-end space-x-3">
        {onViewFull && (
          <button
            onClick={onViewFull}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors text-sm"
          >
            Voir le CV complet
          </button>
        )}
        {onEdit && (
          <button
            onClick={onEdit}
            className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-colors text-sm"
          >
            Modifier
          </button>
        )}
        {onDownload && (
          <button
            onClick={onDownload}
            className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors text-sm"
          >
            Télécharger
          </button>
        )}
      </div>
    </div>
  );
};


export default CVPreview ;