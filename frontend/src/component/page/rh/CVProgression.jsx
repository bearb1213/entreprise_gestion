import React, { useEffect, useState } from 'react';
import CVTemplate from '../../template/CVTemplate';
import CVPreview from '../../template/CVPreview';
import CompactCVPreview from '../../template/CompactCVPreview';
import { useParams } from 'react-router-dom';

const CVProgression = ({navigate}) => {
    const {idCandidat , idBesoin} = useParams();
    

    const [cvData , setCvData] = useState(null);
    const [charged , setCharged] = useState(false)

    useEffect(()=>{
        if (!charged) {
            
                setCvData({
                    nom: 'Dupont',
                    prenom: 'Jean',
                    date_naissance: '1990-05-15',
                    image: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop&crop=face',
                    genre: 'M',
                    email: 'jean.dupont@email.com',
                    telephone: '+33 6 12 34 56 78',
                    ville: 'Paris',
                    description: 'Développeur full-stack passionné avec 5 ans d\'expérience dans le développement web. spécialisé en React, Node.js et bases de données. Toujours à la recherche de nouveaux défis techniques.',

                    langues: [
                    { id: 0, libelle: "Français" },
                    { id: 1, libelle: "Anglais" },
                    { id: 2, libelle: "Espagnol" }
                    ],

                    filiere: [
                    { 
                        id: 2, 
                        filiere: { id: 1, libelle: "Informatique" }, 
                        diplome: { id: 1, libelle: "Master" } 
                    },
                    { 
                        id: 3, 
                        filiere: { id: 2, libelle: "Mathématiques" }, 
                        diplome: { id: 2, libelle: "Licence" } 
                    }
                ],
                
                competences: [
                    { id: 1, libelle: "React" },
                    { id: 2, libelle: "Node.js" },
                    { id: 3, libelle: "MongoDB" },
                    { id: 4, libelle: "TypeScript" },
                    { id: 5, libelle: "Git" }
                ],
                
                metiers: [
                    { 
                        id: 1, 
                        nb_annee: 3, 
                        metier: { id: 1, libelle: "Développeur Frontend" } 
                    },
                    { 
                        id: 2, 
                        nb_annee: 2, 
                        metier: { id: 2, libelle: "Développeur Full-Stack" } 
                    }
                ]
            });
            setCharged(true)
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
        
        
        <div className='fixed bottom-4 left-4'>
            <a href={`/CvList/${idBesoin}`} className="bg-blue-500 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors">
                retour
            </a>
        </div>

      </div>
    </div>
 );
};

export default CVProgression;