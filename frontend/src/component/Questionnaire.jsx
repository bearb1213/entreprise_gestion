import React, { useEffect, useState } from "react";

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

const CheckBoxOption = (props) => {
    return (
        <div className="flex items-center mb-3 p-3 rounded-lg border border-gray-200 hover:border-blue-300 hover:bg-blue-50 transition-all">
            <input 
                type="checkbox" 
                className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" 
            /> 
            <span className="ml-3 text-gray-700">{props.reponse}</span>
        </div>
    )
};

const RadioOption = (props) => {
    return (
        <div className="flex items-center mb-3 p-3 rounded-lg border border-gray-200 hover:border-blue-300 hover:bg-blue-50 transition-all">
            <input 
                type="radio" 
                name={props.groupName} 
                className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300" 
            /> 
            <span className="ml-3 text-gray-700">{props.reponse}</span>
        </div>
    )
};

const Question = (props) => {
    return (
        <div className="mb-8 p-6 bg-white rounded-xl shadow-sm border border-gray-100">
            <p className="text-lg font-semibold text-gray-800 mb-4 flex items-center">
                <span className="bg-blue-100 text-blue-600 p-2 rounded-lg mr-3">
                    {props.index + 1}
                </span>
                {props.content.intitule}
            </p>
            <div className="space-y-2 pl-11">
                {props.content.choix.map((rep, index) => (
                    props.content.type === 'radio' 
                        ? <RadioOption key={index} reponse={rep.reponse} groupName={`group-${props.index}`} />
                        : <CheckBoxOption key={index} reponse={rep.reponse} />
                ))}
            </div>
        </div>
    )
}

function Questionnaire() {
    const [questions, setQuestions] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const getQuestions = async () => {
            setIsLoading(true);
            const fetchedQuestions = await fetchQuestions();
            if (fetchedQuestions) {
                setQuestions(fetchedQuestions);
            }
            setIsLoading(false);
        };
        getQuestions();
    }, []);

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
                            <Question key={index} content={question} index={index} />
                        ))}
                        
                        <div className="mt-8 p-6 bg-white rounded-xl shadow-sm border border-gray-100">
                            <button className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-3 px-4 rounded-lg transition-colors shadow-md flex items-center justify-center">
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" viewBox="0 0 20 20" fill="currentColor">
                                    <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                                </svg>
                                Soumettre le questionnaire
                            </button>
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
}

export default Questionnaire;