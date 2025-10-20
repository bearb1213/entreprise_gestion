import { useState } from 'react';
//eto tena mila zavatra komondry be mintsy satria une question doit avoir un metier_id si elle vient d'une unite
//dcp na atao directement anatina session any rehefa misy chef d'unite mico
//na tsotsora kely,idUser ao am session no ampiasaintsika hitadiavana ny metier anle olona co afaka mahita anty ecran ty,puis iny id iny no alatsaka miaraka amle izy
//na forcena atao select maina be eo ambony eo ilay metier id (solution ratsy indrindra !)

//fin bon,testons d'abord l'endpoint sans id,oui,questions generales aloha zany 
const submitForm = async (data) =>
{
    try {
        const url = "http://localhost:8080/questions/create";
        const response = await fetch(url,{
            method : 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body : JSON.stringify(data)  
        });    

        if (!response.ok) {
            throw new Error(`Probleme de connexion au serveur :${url}`);
        }
        
    } catch (error) {
        console.error("Erreur lors de l'envoi du formulaire");
        throw error;
    }
}


const Question = ({ question, onQuestionChange }) => {
  return (
    <div className="bg-white rounded-lg shadow-md p-6 mb-6">
      <h2 className="text-xl font-semibold text-gray-800 mb-4">Configuration de la question</h2>
      
      <div className="mb-4">
        <label className="block text-sm font-medium text-gray-700 mb-1">Nom de la question</label>
        <input
          type="text"
          value={question.title}
          onChange={(e) => onQuestionChange('title', e.target.value)}
          className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          placeholder="Donnez un nom à votre question"
        />
      </div>
      
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Contenu de la question</label>
        <textarea
          value={question.content}
          onChange={(e) => onQuestionChange('content', e.target.value)}
          rows={3}
          className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          placeholder="Écrivez le contenu de votre question"
        />
      </div>
    </div>
  );
};

const AnswerInput = ({ answer, index, onChange, onRemove }) => {
  return (
    <div className="bg-white rounded-lg shadow-sm p-4 mb-4 border border-gray-200">
      <div className="flex justify-between items-center mb-3">
        <h3 className="font-medium text-gray-700">Réponse #{index + 1}</h3>
        <button
          type="button"
          onClick={() => onRemove(index)}
          className="text-red-500 hover:text-red-700 text-sm font-medium"
        >
          Supprimer
        </button>
      </div>
      
      <div className="mb-3">
        <label className="block text-sm text-gray-600 mb-1">Réponse</label>
        <input
          type="text"
          value={answer.text}
          onChange={(e) => onChange(index, 'text', e.target.value)}
          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          placeholder="Saisissez une réponse"
        />
      </div>
      
      <div>
        <label className="block text-sm text-gray-600 mb-1">Note</label>
        <input
          type="number"
          value={answer.score}
          onChange={(e) => onChange(index, 'score', parseInt(e.target.value) || 0)}
          min="0"
          className="w-24 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          placeholder="0"
        />
      </div>
    </div>
  );
};

function QuestionManager() {
  const [formData, setFormData] = useState({
    title: '',
    content: 'Texte de la question',
    answers: [
      { text: 'Proposition de réponse 1', score: 0 },
      { text: 'Proposition de réponse 2', score: 0 }
    ]
  });

  const handleQuestionChange = (field, value) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleAnswerChange = (index, field, value) => {
    setFormData(prev => {
      const newAnswers = [...prev.answers];
      newAnswers[index] = { ...newAnswers[index], [field]: value };
      return { ...prev, answers: newAnswers };
    });
  };

  const addAnswer = () => {
    setFormData(prev => ({
      ...prev,
      answers: [...prev.answers, { text: `Proposition de réponse ${prev.answers.length + 1}`, score: 0 }]
    }));
  };

  const removeAnswer = (index) => {
    if (formData.answers.length > 1) {
      setFormData(prev => ({
        ...prev,
        answers: prev.answers.filter((_, i) => i !== index)
      }));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Données du formulaire:', formData);
    submitForm(formData);
    // Ici tu pourras envoyer les données à ton API
    alert('Formulaire soumis avec succès!');
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8 px-4 sm:px-6 lg:px-8">
      <div className="max-w-3xl mx-auto">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Formulaire d'insertion de question</h1>
        </div>
        
        <form onSubmit={handleSubmit} className="space-y-6">
          <Question 
            question={formData} 
            onQuestionChange={handleQuestionChange} 
          />
          
          <div className="bg-white rounded-lg shadow-md p-6">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-semibold text-gray-800">Réponses</h2>
              <button 
                type="button" 
                onClick={addAnswer}
                className="flex items-center px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
              >
                <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
                </svg>
                Ajouter une réponse
              </button>
            </div>
            
            {formData.answers.map((answer, index) => (
              <AnswerInput
                key={index}
                index={index}
                answer={answer}
                onChange={handleAnswerChange}
                onRemove={removeAnswer}
              />
            ))}
          </div>

          <div className="flex justify-end space-x-4">
            <button 
              type="button" 
              className="px-6 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
              Annuler
            </button>
            <button 
              type="submit" 
              className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
              Enregistrer la question
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default QuestionManager;