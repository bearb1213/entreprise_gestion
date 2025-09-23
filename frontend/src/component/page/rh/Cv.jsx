import React, { useEffect, useState } from 'react';
import CVTemplate from '../../template/CVTemplate';
import CVPreview from '../../template/CVPreview';
import CompactCVPreview from '../../template/CompactCVPreview';
import {useParams} from 'react-router-dom';

const Cv = ({navigate}) => {

    const {id}=useParams();
    const [cvData , setCvData] = useState(null);
    const [charged , setCharged] = useState(false)

    useEffect(()=>{
        if (!charged) {
            // Simuler la récupération des données du CV depuis une API ou une source de données
            fetch("htpp://localhost:8080/api/candidats/"+id , {credentials : true}) 
            .then((response) => response.json())
            
            .then((data) => {setCvData(data) ;setCharged(true) } )
            .catch((error) => console.error("Erreur fetch CV :", error));
        }
    },[cvData,charged])


// return (
//     <div className="min-h-screen bg-gray-100 py-8 px-4">
//         <CompactCVPreview
//         cvData={cvData}
        
//         />
    
//     </div>
    
// );



  return (
    <div className="min-h-screen bg-gray-100 py-8 px-4">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold text-center text-gray-800 mb-8">
          Mon Curriculum Vitae
        </h1>
        
        {charged && <CVTemplate cvData={cvData} />}
        
        {/* Bouton d'impression */}
        <div className="text-center mt-6">
          <button
            onClick={() => window.print()}
            className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors"
          >
            📄 Imprimer le CV
          </button>
        </div>
        <div className='fixed bottom-4 left-4'>
            <a href="/dashboard" className="bg-blue-500 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors">
                retour
            </a>
        </div>
      </div>
    </div>
 );
};

export default Cv;