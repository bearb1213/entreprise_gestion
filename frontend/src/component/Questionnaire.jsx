import React, { useEffect, useState } from "react";
<<<<<<< Updated upstream

// ici je dois recuperer un pathVariable contenu dans le lien menant a cette page
const candidatureId = 1;

async function fetchQuestions() {
    try {
        const formData = new URLSearchParams();
        formData.append('id_dept', '1');
        formData.append('id_metier', '1');

        const response = await fetch('http://localhost:8080/questions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            },
            body: formData
        });
        
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        
        const data = await response.json();
        console.log(data);
        return data;
    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
        return [];
    }
}

async function submitAnswers(answers) {
    try {
        const response = await fetch(`http://localhost:8080/questions/submit/${candidatureId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(answers)
        });
        
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        
        const data = await response.json();
        console.log("Réponses soumises avec succès:", data);
        return data;
    } catch (error) {
        console.error('Erreur lors de la soumission des réponses:', error);
        throw error;
    }
}

const CheckBoxOption = ({ reponse, questionId, optionId, onSelect, isSelected }) => {
    return (
        <div className="flex items-center mb-3 p-3 rounded-lg border border-gray-200 hover:border-blue-300 hover:bg-blue-50 transition-all">
            <input 
                type="checkbox" 
                checked={isSelected}
                onChange={() => onSelect(questionId, optionId, !isSelected)}
                className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" 
            /> 
            <span className="ml-3 text-gray-700">{reponse}</span>
        </div>
    )
};

const RadioOption = ({ reponse, questionId, optionId, onSelect, isSelected }) => {
    return (
        <div className="flex items-center mb-3 p-3 rounded-lg border border-gray-200 hover:border-blue-300 hover:bg-blue-50 transition-all">
            <input 
                type="radio" 
                checked={isSelected}
                onChange={() => onSelect(questionId, optionId, true)}
                className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300" 
            /> 
            <span className="ml-3 text-gray-700">{reponse}</span>
        </div>
    )
};

const Question = ({ content, index, onAnswer }) => {
    const handleSelect = (questionId, optionId, isSelected) => {
        onAnswer(questionId, optionId, isSelected);
    };

    return (
        <div className="mb-8 p-6 bg-white rounded-xl shadow-sm border border-gray-100">
            <p className="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                <span className="bg-blue-100 text-blue-600 p-2 rounded-lg mr-3">
                    {index + 1}
                </span>
                {content.intitule}
            </p>
            <div className="space-y-2 pl-11">
                {content.choix.map((rep, optionIndex) => (
                    content.type === 'radio' 
                        ? <RadioOption 
                            key={optionIndex} 
                            reponse={rep.reponse} 
                            questionId={content.id}
                            optionId={rep.id}
                            onSelect={handleSelect}
                            isSelected={rep.selected || false}
                          />
                        : <CheckBoxOption 
                            key={optionIndex} 
                            reponse={rep.reponse} 
                            questionId={content.id}
                            optionId={rep.id}
                            onSelect={handleSelect}
                            isSelected={rep.selected || false}
                          />
                ))}
            </div>
        </div>
    )
}

function Questionnaire() {
    const [questions, setQuestions] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [submitStatus, setSubmitStatus] = useState(null);

    useEffect(() => {
        const getQuestions = async () => {
            setIsLoading(true);
            const fetchedQuestions = await fetchQuestions();
            if (fetchedQuestions) {
                const questionsWithIds = fetchedQuestions.map((q, index) => ({
                    ...q,
                    id: q.id || `question-${index}`,
                    type: q.type || 'checkbox', // Par défaut checkbox si non spécifié
                    choix: q.choix.map((c, cIndex) => ({
                        ...c,
                        id: c.id || `option-${index}-${cIndex}`,
                        selected: false
                    }))
                }));
                setQuestions(questionsWithIds);
            }
            setIsLoading(false);
        };
        getQuestions();
    }, []);

    const handleAnswer = (questionId, optionId, isSelected) => {
        setQuestions(prevQuestions => 
            prevQuestions.map(question => {
                if (question.id === questionId) {
                    const updatedChoix = question.choix.map(option => {
                        if (option.id === optionId) {
                            return { ...option, selected: isSelected };
                        } else if (question.type === 'radio') {
                            return { ...option, selected: false };
                        }
                        return option;
                    });
                    
                    return {
                        ...question,
                        choix: updatedChoix
                    };
                }
                return question;
            })
        );
    };

    const handleSubmit = async () => {
        setIsSubmitting(true);
        setSubmitStatus(null);
        
        try {
            const answers = questions.map(question => ({
                questionId: question.id,
                question: question.intitule,
                responses: question.choix
                    .filter(option => option.selected)
                    .map(option => ({
                        optionId: option.id,
                        response: option.reponse
                    }))
            }));
            
            console.log("Réponses à envoyer:", answers);
            await submitAnswers(answers);
            setSubmitStatus('success');
        } catch (error) {
            console.error("Erreur lors de la soumission:", error);
            setSubmitStatus('error');
        } finally {
            setIsSubmitting(false);
        }
    };

    const allQuestionsAnswered = questions.every(question => 
        question.type === 'radio' 
            ? question.choix.some(option => option.selected)
            : true
    );

    return (
        <div className="min-h-screen bg-gray-50 py-8 px-4 sm:px-6 lg:px-8">
            <div className="max-w-3xl mx-auto">
                <div className="text-center mb-10">
                    <h1 className="text-3xl font-bold text-gray-900 mb-2">Questionnaire</h1>
                    <p className="text-gray-600">Répondez à toutes les questions ci-dessous</p>
                    <div className="w-20 h-1 bg-blue-500 mx-auto mt-4 rounded-full"></div>
                </div>
                
                {isLoading ? (
                    <div className="bg-white p-8 rounded-xl shadow-sm border border-gray-100 text-center">
                        <div className="inline-flex items-center">
                            <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-500 mr-3"></div>
                            <span className="text-gray-600">Chargement des questions...</span>
                        </div>
                    </div>
                ) : questions.length > 0 ? (
                    <div className="space-y-6">
                        {questions.map((question, index) => (
                            <Question 
                                key={question.id} 
                                content={question} 
                                index={index} 
                                onAnswer={handleAnswer}
                            />
                        ))}
                        
                        <div className="mt-8 p-6 bg-white rounded-xl shadow-sm border border-gray-100">
                            {submitStatus === 'success' && (
                                <div className="mb-4 p-4 bg-green-100 text-green-700 rounded-lg flex items-center">
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                                        <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                                    </svg>
                                    Vos réponses ont été enregistrées avec succès!
                                </div>
                            )}
                            
                            {submitStatus === 'error' && (
                                <div className="mb-4 p-4 bg-red-100 text-red-700 rounded-lg flex items-center">
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                                        <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
                                    </svg>
                                    Une erreur s'est produite lors de l'envoi. Veuillez réessayer.
                                </div>
                            )}
                            
                            <button 
                                onClick={handleSubmit}
                                disabled={isSubmitting || !allQuestionsAnswered}
                                className={`w-full text-white font-medium py-3 px-4 rounded-lg transition-colors shadow-md flex items-center justify-center ${
                                    isSubmitting || !allQuestionsAnswered
                                        ? 'bg-gray-400 cursor-not-allowed'
                                        : 'bg-blue-600 hover:bg-blue-700'
                                }`}
                            >
                                {isSubmitting ? (
                                    <>
                                        <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
                                        Envoi en cours...
                                    </>
                                ) : (
                                    <>
                                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                                            <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                                        </svg>
                                        Soumettre le questionnaire
                                    </>
                                )}
                            </button>
                            
                            {!allQuestionsAnswered && (
                                <p className="text-sm text-orange-600 mt-3 text-center">
                                    Veuillez répondre à toutes les questions obligatoires avant de soumettre.
                                </p>
                            )}
                        </div>
                    </div>
                ) : (
                    <div className="bg-white p-8 rounded-xl shadow-sm border border-gray-100 text-center">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-12 w-12 text-gray-400 mx-auto mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        <p className="text-gray-600">Aucune question disponible pour le moment.</p>
                    </div>
                )}
            </div>
        </div>
    )
=======
import { useParams, useNavigate } from "react-router-dom";

// Composant d'alerte de login style Tomcat simplifié
const EmailLoginAlert = ({ onLogin, onCancel, show, error }) => {
  const [email, setEmail] = useState("");

  if (!show) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    if (email.trim()) {
      onLogin({ email: email.trim() });
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-96">
        {/* Header style Tomcat */}
        <div className="bg-gray-800 text-white px-4 py-3 rounded-t-lg">
          <div className="flex items-center">
            <div className="w-6 h-6 bg-yellow-400 rounded-full mr-2"></div>
            <h3 className="font-bold">Authentification requise</h3>
          </div>
        </div>
        
        {/* Body */}
        <div className="p-6">
          <div className="mb-4 p-3 bg-yellow-50 border border-yellow-200 rounded">
            <p className="text-sm text-yellow-800">
              Veuillez entrer votre email pour accéder au questionnaire.
            </p>
          </div>
          
          {error && (
            <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded">
              <p className="text-sm text-red-800">{error}</p>
            </div>
          )}
          
          <form onSubmit={handleSubmit}>
            <div className="mb-6">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Adresse email
              </label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="votre@email.com"
                autoFocus
                required
              />
            </div>
            
            <div className="flex justify-end space-x-3">
              <button
                type="button"
                onClick={onCancel}
                className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 border border-gray-300 rounded hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-500"
              >
                Annuler
              </button>
              <button
                type="submit"
                disabled={!email.trim()}
                className="px-4 py-2 text-sm font-medium text-white bg-blue-600 border border-transparent rounded hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Se connecter
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

// Fonctions API CORRIGÉES
async function fetchQuestions(candidatureId) {
  try {
    console.log("🔍 Fetching questions for candidature:", candidatureId);
    
    // D'abord, récupérer les infos de la candidature pour avoir id_dept et id_metier
    const candidatureResponse = await fetch(`http://localhost:8080/api/candidature/details/${candidatureId}`, {
      method: 'GET',
      credentials: 'include'
    });
    
    if (!candidatureResponse.ok) {
      throw new Error('Cannot fetch candidature details');
    }
    
    const candidatureData = await candidatureResponse.json();
    console.log("📋 Candidature data:", candidatureData);
    
    // Extraire id_dept et id_metier depuis les données de la candidature
    // Adaptez ces lignes selon la structure de votre réponse
    const idDept = candidatureData.besoin?.departementId || 1; // Fallback si non trouvé
    const idMetier = candidatureData.besoin?.metierId || 1;    // Fallback si non trouvé
    
    console.log("🎯 Using parameters - id_dept:", idDept, "id_metier:", idMetier);
    
    // Maintenant appeler l'endpoint POST avec les paramètres requis
    const formData = new URLSearchParams();
    formData.append('id_dept', idDept);
    formData.append('id_metier', idMetier);
    formData.append('candidatureId', candidatureId);
    
    const response = await fetch('http://localhost:8080/api/questions/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
      },
      credentials: 'include',
      body: formData
    });
    
    if (!response.ok) {
      throw new Error(`Network response was not ok: ${response.status}`);
    }
    
    const data = await response.json();
    console.log("✅ Questions received:", data);
    return data;
  } catch (error) {
    console.error('❌ Error fetching questions:', error);
    return [];
  }
}

async function submitAnswers(answers, candidatureId) {
  try {
    console.log("📤 Submitting answers for candidature:", candidatureId);
    
    const response = await fetch(`http://localhost:8080/api/questions/submit/${candidatureId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(answers)
    });
    
    if (!response.ok) {
      throw new Error(`Network response was not ok: ${response.status}`);
    }
    
    const data = await response.json();
    console.log("✅ Réponses soumises avec succès:", data);
    return data;
  } catch (error) {
    console.error('❌ Erreur lors de la soumission des réponses:', error);
    throw error;
  }
}

async function authenticateUser(credentials) {
  try {
    console.log("🔐 Authenticating user with email");
    
    const response = await fetch('http://localhost:8080/api/utilisateur/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(credentials)
    });
    
    if (!response.ok) {
      throw new Error('Authentication failed');
    }
    
    const data = await response.json();
    console.log("✅ Authentication successful");
    return data;
  } catch (error) {
    console.error('❌ Authentication error:', error);
    throw error;
  }
}

// Composants des questions (inchangés)
const CheckBoxOption = ({ reponse, questionId, optionId, onSelect, isSelected }) => {
  return (
    <div className="flex items-center mb-3 p-3 rounded-lg border border-gray-200 hover:border-blue-300 hover:bg-blue-50 transition-all">
      <input 
        type="checkbox" 
        checked={isSelected}
        onChange={() => onSelect(questionId, optionId, !isSelected)}
        className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" 
      /> 
      <span className="ml-3 text-gray-700">{reponse}</span>
    </div>
  )
};

const RadioOption = ({ reponse, questionId, optionId, onSelect, isSelected }) => {
  return (
    <div className="flex items-center mb-3 p-3 rounded-lg border border-gray-200 hover:border-blue-300 hover:bg-blue-50 transition-all">
      <input 
        type="radio" 
        checked={isSelected}
        onChange={() => onSelect(questionId, optionId, true)}
        className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300" 
      /> 
      <span className="ml-3 text-gray-700">{reponse}</span>
    </div>
  )
};

const Question = ({ content, index, onAnswer }) => {
  const handleSelect = (questionId, optionId, isSelected) => {
    onAnswer(questionId, optionId, isSelected);
  };

  return (
    <div className="mb-8 p-6 bg-white rounded-xl shadow-sm border border-gray-100">
      <p className="text-lg font-semibold text-gray-800 mb-4 flex items-center">
        <span className="bg-blue-100 text-blue-600 p-2 rounded-lg mr-3">
          {index + 1}
        </span>
        {content.intitule}
      </p>
      <div className="space-y-2 pl-11">
        {content.choix && content.choix.map((rep, optionIndex) => (
          content.type === 'radio' 
            ? <RadioOption 
                key={optionIndex} 
                reponse={rep.reponse} 
                questionId={content.id}
                optionId={rep.id}
                onSelect={handleSelect}
                isSelected={rep.selected || false}
              />
            : <CheckBoxOption 
                key={optionIndex} 
                reponse={rep.reponse} 
                questionId={content.id}
                optionId={rep.id}
                onSelect={handleSelect}
                isSelected={rep.selected || false}
              />
        ))}
      </div>
    </div>
  )
}

// Composant principal Questionnaire
function Questionnaire() {
  const { candidatureId } = useParams();
  const navigate = useNavigate();
  const [questions, setQuestions] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitStatus, setSubmitStatus] = useState(null);
  const [showLogin, setShowLogin] = useState(true);
  const [authError, setAuthError] = useState("");

  console.log("ID Candidature récupéré:", candidatureId);

  // Vérifier si l'utilisateur est déjà authentifié au chargement
  useEffect(() => {
    const checkAuthentication = async () => {
      try {
        // Essayer de récupérer les questions (si déjà authentifié)
        await loadQuestions();
      } catch (error) {
        console.log("Utilisateur non authentifié, affichage de l'alerte de login");
        setShowLogin(true);
      }
    };
    
    if (candidatureId) {
      checkAuthentication();
    }
  }, [candidatureId]);

  const loadQuestions = async () => {
    setIsLoading(true);
    try {
      const fetchedQuestions = await fetchQuestions(candidatureId);
      if (fetchedQuestions && fetchedQuestions.length > 0) {
        const questionsWithIds = fetchedQuestions.map((q, index) => ({
          ...q,
          id: q.id || `question-${index}`,
          type: q.type || 'checkbox',
          choix: q.choix ? q.choix.map((c, cIndex) => ({
            ...c,
            id: c.id || `option-${index}-${cIndex}`,
            selected: false
          })) : []
        }));
        setQuestions(questionsWithIds);
        setShowLogin(false);
      } else {
        console.warn("Aucune question reçue");
        setQuestions([]);
      }
    } catch (error) {
      console.error("Error loading questions:", error);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const handleLogin = async (credentials) => {
    setAuthError("");
    try {
      await authenticateUser(credentials);
      // Si l'authentification réussit, charger les questions
      await loadQuestions();
    } catch (error) {
      setAuthError("Email incorrect ou non autorisé");
    }
  };

  const handleCancelLogin = () => {
    // Rediriger vers la page précédente
    navigate(-1);
  };

  const handleAnswer = (questionId, optionId, isSelected) => {
    setQuestions(prevQuestions => 
      prevQuestions.map(question => {
        if (question.id === questionId) {
          const updatedChoix = question.choix.map(option => {
            if (option.id === optionId) {
              return { ...option, selected: isSelected };
            } else if (question.type === 'radio') {
              return { ...option, selected: false };
            }
            return option;
          });
          
          return {
            ...question,
            choix: updatedChoix
          };
        }
        return question;
      })
    );
  };

  const handleSubmit = async () => {
    setIsSubmitting(true);
    setSubmitStatus(null);
    
    try {
      const answers = questions.map(question => ({
        questionId: question.id,
        question: question.intitule,
        responses: question.choix
          .filter(option => option.selected)
          .map(option => ({
            optionId: option.id,
            response: option.reponse
          }))
      }));
      
      console.log("Réponses à envoyer:", answers);
      await submitAnswers(answers, candidatureId);
      setSubmitStatus('success');
      
      // Rediriger après succès
      setTimeout(() => {
        navigate('/dashboard');
      }, 2000);
      
    } catch (error) {
      console.error("Erreur lors de la soumission:", error);
      setSubmitStatus('error');
    } finally {
      setIsSubmitting(false);
    }
  };

  const allQuestionsAnswered = questions.every(question => 
    question.type === 'radio' 
      ? question.choix.some(option => option.selected)
      : true
  );

  return (
    <>
      {/* Alerte de login */}
      <EmailLoginAlert 
        show={showLogin}
        onLogin={handleLogin}
        onCancel={handleCancelLogin}
        error={authError}
      />

      {/* Questionnaire (seulement si authentifié) */}
      {!showLogin && (
        <div className="min-h-screen bg-gray-50 py-8 px-4 sm:px-6 lg:px-8">
          <div className="max-w-3xl mx-auto">
            <div className="text-center mb-10">
              <h1 className="text-3xl font-bold text-gray-900 mb-2">Questionnaire</h1>
              <p className="text-gray-600">Répondez à toutes les questions ci-dessous</p>
              <div className="w-20 h-1 bg-blue-500 mx-auto mt-4 rounded-full"></div>
            </div>
            
            {isLoading ? (
              <div className="bg-white p-8 rounded-xl shadow-sm border border-gray-100 text-center">
                <div className="inline-flex items-center">
                  <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-500 mr-3"></div>
                  <span className="text-gray-600">Chargement des questions...</span>
                </div>
              </div>
            ) : questions.length > 0 ? (
              <div className="space-y-6">
                {questions.map((question, index) => (
                  <Question 
                    key={question.id} 
                    content={question} 
                    index={index} 
                    onAnswer={handleAnswer}
                  />
                ))}
                
                <div className="mt-8 p-6 bg-white rounded-xl shadow-sm border border-gray-100">
                  {submitStatus === 'success' && (
                    <div className="mb-4 p-4 bg-green-100 text-green-700 rounded-lg flex items-center">
                      <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                        <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                      </svg>
                      Vos réponses ont été enregistrées avec succès!
                    </div>
                  )}
                  
                  {submitStatus === 'error' && (
                    <div className="mb-4 p-4 bg-red-100 text-red-700 rounded-lg flex items-center">
                      <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                        <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
                      </svg>
                      Une erreur s'est produite lors de l'envoi. Veuillez réessayer.
                    </div>
                  )}
                  
                  <button 
                    onClick={handleSubmit}
                    disabled={isSubmitting || !allQuestionsAnswered}
                    className={`w-full text-white font-medium py-3 px-4 rounded-lg transition-colors shadow-md flex items-center justify-center ${
                      isSubmitting || !allQuestionsAnswered
                        ? 'bg-gray-400 cursor-not-allowed'
                        : 'bg-blue-600 hover:bg-blue-700'
                    }`}
                  >
                    {isSubmitting ? (
                      <>
                        <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
                        Envoi en cours...
                      </>
                    ) : (
                      <>
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                          <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                        </svg>
                        Soumettre le questionnaire
                      </>
                    )}
                  </button>
                  
                  {!allQuestionsAnswered && (
                    <p className="text-sm text-orange-600 mt-3 text-center">
                      Veuillez répondre à toutes les questions obligatoires avant de soumettre.
                    </p>
                  )}
                </div>
              </div>
            ) : (
              <div className="bg-white p-8 rounded-xl shadow-sm border border-gray-100 text-center">
                <svg xmlns="http://www.w3.org/2000/svg" className="h-12 w-12 text-gray-400 mx-auto mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <p className="text-gray-600">Aucune question disponible pour le moment.</p>
              </div>
            )}
          </div>
        </div>
      )}
    </>
  );
>>>>>>> Stashed changes
}

export default Questionnaire;