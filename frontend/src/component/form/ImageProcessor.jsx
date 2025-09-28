import { useState, useRef } from 'react';

const ImageProcessor = ({ onImageChange, initialImage = null }) => {
  const [selectedImage, setSelectedImage] = useState(initialImage);
  const [previewUrl, setPreviewUrl] = useState(initialImage);
  const [isDragging, setIsDragging] = useState(false);
  const [error, setError] = useState('');
  const fileInputRef = useRef(null);

  // Types de fichiers acceptés
  const acceptedFileTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
  const maxFileSize = 5 * 1024 * 1024; // 5MB

  // Vérification du fichier
  const validateFile = (file) => {
    if (!acceptedFileTypes.includes(file.type)) {
      setError('Type de fichier non supporté. Utilisez JPEG, PNG, GIF ou WebP.');
      return false;
    }

    if (file.size > maxFileSize) {
      setError('Fichier trop volumineux. Maximum 5MB autorisé.');
      return false;
    }

    setError('');
    return true;
  };

  // Gestion du drop de fichier
  const handleDrop = (e) => {
    e.preventDefault();
    setIsDragging(false);

    const files = e.dataTransfer.files;
    if (files.length > 0) {
      processFile(files[0]);
    }
  };

  // Gestion de la sélection via input
  const handleFileSelect = (e) => {
    const file = e.target.files[0];
    if (file) {
      processFile(file);
    }
  };

  // Traitement du fichier
  const processFile = (file) => {
    if (!validateFile(file)) return;

    setSelectedImage(file);

    // Création de l'URL de prévisualisation
    const reader = new FileReader();
    reader.onload = (e) => {
      setPreviewUrl(e.target.result);
      if (onImageChange) {
        onImageChange(file, e.target.result);
      }
    };
    reader.readAsDataURL(file);
  };

  // Gestion des événements drag and drop
  const handleDragOver = (e) => {
    e.preventDefault();
    setIsDragging(true);
  };

  const handleDragLeave = (e) => {
    e.preventDefault();
    setIsDragging(false);
  };

  // Supprimer l'image
  const handleRemoveImage = () => {
    setSelectedImage(null);
    setPreviewUrl(null);
    setError('');
    if (onImageChange) {
      onImageChange(null, null);
    }
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  return (
    <div className="w-full max-w-md mx-auto">
      <div
        className={`
          relative border-2 border-dashed rounded-lg p-6 text-center cursor-pointer
          transition-all duration-300 ease-in-out
          ${isDragging 
            ? 'border-blue-500 bg-blue-50 scale-105' 
            : 'border-gray-300 bg-gray-50'
          }
          ${previewUrl 
            ? 'border-0 p-0 bg-transparent' 
            : 'min-h-[200px] flex items-center justify-center'
          }
          hover:border-blue-400 hover:bg-blue-50
        `}
        onDrop={handleDrop}
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
        onClick={() => fileInputRef.current?.click()}
      >
        {previewUrl ? (
          <div className="relative w-full h-full">
            <img 
              src={previewUrl} 
              alt="Aperçu" 
              className="w-full h-full object-cover rounded-lg max-h-80"
            />
            <div className="absolute inset-0 bg-black bg-opacity-50 opacity-0 hover:opacity-100 transition-opacity duration-300 rounded-lg flex items-center justify-center">
              <span className="text-white font-medium">Cliquer pour changer</span>
            </div>
          </div>
        ) : (
          <div className="text-gray-500">
            <div className="text-3xl mb-3">📁</div>
            <p className="font-medium mb-1">Glissez-déposez une image ici</p>
            <p className="text-sm">ou cliquez pour sélectionner</p>
            <small className="text-xs text-gray-400 block mt-2">
              JPEG, PNG, GIF, WebP - Max 5MB
            </small>
          </div>
        )}
      </div>

      <input
        ref={fileInputRef}
        type="file"
        accept="image/*"
        onChange={handleFileSelect}
        className="hidden"
      />

      {error && (
        <div className="mt-3 p-3 bg-red-50 border border-red-200 rounded-lg">
          <p className="text-red-700 text-sm flex items-center">
            <span className="mr-2">⚠️</span>
            {error}
          </p>
        </div>
      )}

      {previewUrl && (
        <div className="mt-4 flex justify-between items-center p-3 bg-gray-50 rounded-lg">
          <button
            type="button"
            onClick={handleRemoveImage}
            className="px-3 py-2 bg-red-500 text-white rounded-md text-sm hover:bg-red-600 transition-colors"
          >
            ✕ Supprimer
          </button>
          
          <div className="text-xs text-gray-500">
            {selectedImage && (
              <span>
                {selectedImage.name} - {(selectedImage.size / 1024).toFixed(1)}KB
              </span>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default ImageProcessor;