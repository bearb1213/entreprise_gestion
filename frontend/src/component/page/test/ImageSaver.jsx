import React, { useState, useRef } from 'react';

const ImageSaver = () => {
  const [selectedImage, setSelectedImage] = useState(null);
  const [previewUrl, setPreviewUrl] = useState('');
  const [isSaving, setIsSaving] = useState(false);
  const [saveStatus, setSaveStatus] = useState('');
  const fileInputRef = useRef(null);

  // Gérer la sélection d'image
  const handleImageSelect = (event) => {
    const file = event.target.files[0];
    if (!file) return;

    // Vérifier le type de fichier
    if (!file.type.match('image.*')) {
      setSaveStatus('error');
      setTimeout(() => setSaveStatus(''), 3000);
      return;
    }

    setSelectedImage(file);
    
    // Créer une prévisualisation
    const reader = new FileReader();
    reader.onload = (e) => {
      setPreviewUrl(e.target.result);
    };
    reader.readAsDataURL(file);
  };

  // Sauvegarder l'image
  const saveImage = async () => {
    if (!selectedImage) return;
    
    setIsSaving(true);
    setSaveStatus('saving');
    
    try {
      // Simuler un processus de sauvegarde
      await new Promise(resolve => setTimeout(resolve, 1500));
      
      // Ici, vous pouvez ajouter le code pour réellement sauvegarder l'image
      // Par exemple, l'envoyer à un serveur, la sauvegarder dans le localStorage, etc.
      
      setSaveStatus('success');
      setTimeout(() => setSaveStatus(''), 3000);
    } catch (error) {
      console.error('Erreur lors de la sauvegarde:', error);
      setSaveStatus('error');
      setTimeout(() => setSaveStatus(''), 3000);
    } finally {
      setIsSaving(false);
    }
  };

  // Réinitialiser la sélection
  const resetSelection = () => {
    setSelectedImage(null);
    setPreviewUrl('');
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  // Télécharger l'image
  const downloadImage = () => {
    if (!previewUrl) return;
    
    const link = document.createElement('a');
    link.href = previewUrl;
    link.download = selectedImage.name || 'image_sauvegardee';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 py-12 px-4">
      <div className="max-w-md mx-auto bg-white rounded-xl shadow-md overflow-hidden p-6">
        <h2 className="text-2xl font-bold text-center text-gray-800 mb-6">Sauvegarder une Image</h2>
        
        <div className="mb-6">
          <div 
            className="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center cursor-pointer transition-colors hover:border-blue-400 hover:bg-blue-50"
            onClick={() => fileInputRef.current?.click()}
          >
            <input
              type="file"
              ref={fileInputRef}
              onChange={handleImageSelect}
              accept="image/*"
              className="hidden"
            />
            
            {previewUrl ? (
              <div className="flex flex-col items-center">
                <img 
                  src={previewUrl} 
                  alt="Aperçu" 
                  className="max-h-64 max-w-full rounded-md mb-4 object-contain"
                />
                <p className="text-sm text-gray-600">Cliquez pour changer d'image</p>
              </div>
            ) : (
              <div className="flex flex-col items-center">
                <svg className="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
                </svg>
                <p className="text-gray-600">Cliquez pour sélectionner une image</p>
                <p className="text-sm text-gray-500 mt-2">PNG, JPG, JPEG jusqu'à 10MB</p>
              </div>
            )}
          </div>
        </div>

        {selectedImage && (
          <div className="mb-6 p-4 bg-gray-50 rounded-lg">
            <h3 className="font-medium text-gray-700 mb-2">Détails de l'image</h3>
            <p className="text-sm text-gray-600">Nom: {selectedImage.name}</p>
            <p className="text-sm text-gray-600">Taille: {(selectedImage.size / 1024).toFixed(2)} KB</p>
            <p className="text-sm text-gray-600">Type: {selectedImage.type}</p>
          </div>
        )}

        <div className="flex flex-col gap-3">
          {selectedImage && (
            <>
              <button
                onClick={saveImage}
                disabled={isSaving}
                className="w-full py-3 px-4 bg-blue-600 text-white rounded-md font-medium hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50 transition-colors"
              >
                {isSaving ? 'Sauvegarde en cours...' : 'Sauvegarder l\'image'}
              </button>
              
              <button
                onClick={downloadImage}
                className="w-full py-3 px-4 bg-green-600 text-white rounded-md font-medium hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2 transition-colors"
              >
                Télécharger l'image
              </button>
            </>
          )}
          
          <button
            onClick={resetSelection}
            className="w-full py-3 px-4 bg-gray-200 text-gray-800 rounded-md font-medium hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2 transition-colors"
          >
            {selectedImage ? 'Sélectionner une autre image' : 'Effacer la sélection'}
          </button>
        </div>

        {saveStatus === 'success' && (
          <div className="mt-6 p-3 bg-green-100 text-green-700 rounded-md text-center">
            Image sauvegardée avec succès!
          </div>
        )}
        
        {saveStatus === 'error' && (
          <div className="mt-6 p-3 bg-red-100 text-red-700 rounded-md text-center">
            Erreur lors de la sauvegarde. Veuillez réessayer.
          </div>
        )}
      </div>

      <div className="max-w-md mx-auto mt-8 bg-white rounded-xl shadow-md overflow-hidden p-6">
        <h3 className="text-lg font-medium text-gray-800 mb-4">Comment utiliser ce composant</h3>
        <ul className="list-disc pl-5 text-gray-600 space-y-2">
          <li>Cliquez sur la zone pointillée pour sélectionner une image</li>
          <li>L'image sélectionnée s'affichera en aperçu</li>
          <li>Cliquez sur "Sauvegarder l'image" pour enregistrer</li>
          <li>Utilisez "Télécharger l'image" pour la sauvegarder sur votre appareil</li>
        </ul>
      </div>
    </div>
  );
};

export default ImageSaver;