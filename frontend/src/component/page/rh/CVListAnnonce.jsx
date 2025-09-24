import React, { useEffect, useState } from 'react';
import CompactCVPreview from '../../template/CompactCVPreview';
import { Link } from 'react-router-dom';
import { useParams } from 'react-router-dom';

const CVListAnnonce = () => {
    const {id} = useParams();

    const [besoins , setBesoins] = useState({});
    const [chargedBesoin , setChargedBesoin] = useState(false);

    
  const [cvs, setCvs] = useState([]);
    const [chargedCvs,setChargedCv] = useState(false);

  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCity, setSelectedCity] = useState('');
  const [selectedJob, setSelectedJob] = useState('');

  // Filtrer les CVs
  const filteredCVs = cvs.filter(cv => {
    const matchesSearch = 
      `${cv.prenom} ${cv.nom}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
      cv.metiers.some(metier => 
        metier.metier.libelle.toLowerCase().includes(searchTerm.toLowerCase())
      ) ||
      cv.competences.some(comp => 
        comp.libelle.toLowerCase().includes(searchTerm.toLowerCase())
      );

    const matchesCity = selectedCity ? cv.ville === selectedCity : true;
    const matchesJob = selectedJob ? 
      cv.metiers.some(metier => metier.metier.libelle === selectedJob) : true;

    return matchesSearch && matchesCity && matchesJob;
  });

  // Options pour les filtres
  const cities = [...new Set(cvs.map(cv => cv.ville))];
  const jobs = [...new Set(cvs.flatMap(cv => cv.metiers.map(m => m.metier.libelle)))];

  const handleCVClick = (cv) => {
    
  };
  /// alana rehefa manao integration
    function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
    }
    const getAllCv = async () => {
        await delay(2000);
        
        setCvs([
            {
            id: 1,
            nom: 'Dupont',
            prenom: 'Jean',
            image: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop&crop=face',
            ville: 'Paris',
            metiers: [
                { 
                id: 1, 
                nb_annee: 3, 
                metier: { id: 1, libelle: "Développeur Frontend" } 
                }
            ],
            competences: [
                { id: 1, libelle: "React" },
                { id: 2, libelle: "Node.js" },
                { id: 3, libelle: "MongoDB" },
                { id: 4, libelle: "TypeScript" }
            ]
            },
            {
            id: 2,
            nom: 'Martin',
            prenom: 'Sophie',
            image: 'https://images.unsplash.com/photo-1494790108755-2616b612b786?w=150&h=150&fit=crop&crop=face',
            ville: 'Lyon',
            metiers: [
                { 
                id: 2, 
                nb_annee: 5, 
                metier: { id: 2, libelle: "Designer UX/UI" } 
                }
            ],
            competences: [
                { id: 6, libelle: "Figma" },
                { id: 7, libelle: "Adobe XD" },
                { id: 8, libelle: "Photoshop" }
            ]
            },
            {
            id: 3,
            nom: 'Bernard',
            prenom: 'Pierre',
            image: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face',
            ville: 'Marseille',
            metiers: [
                { 
                id: 3, 
                nb_annee: 7, 
                metier: { id: 3, libelle: "Data Scientist" } 
                }
            ],
            competences: [
                { id: 9, libelle: "Python" },
                { id: 10, libelle: "Machine Learning" },
                { id: 11, libelle: "SQL" },
                { id: 12, libelle: "TensorFlow" }
            ]
            },
            {
            id: 4,
            nom: 'Dubois',
            prenom: 'Marie',
            image: 'https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150&h=150&fit=crop&crop=face',
            ville: 'Toulouse',
            metiers: [
                { 
                id: 4, 
                nb_annee: 2, 
                metier: { id: 4, libelle: "Marketing Digital" } 
                }
            ],
            competences: [
                { id: 13, libelle: "SEO" },
                { id: 14, libelle: "Google Analytics" },
                { id: 15, libelle: "Réseaux sociaux" }
            ]
            },
            {
            id: 5,
            nom: 'Moreau',
            prenom: 'Thomas',
            image: null, // Pas de photo
            ville: 'Bordeaux',
            metiers: [
                { 
                id: 5, 
                nb_annee: 4, 
                metier: { id: 5, libelle: "DevOps Engineer" } 
                }
            ],
            competences: [
                { id: 16, libelle: "Docker" },
                { id: 17, libelle: "Kubernetes" },
                { id: 18, libelle: "AWS" },
                { id: 19, libelle: "CI/CD" }
            ]
            }
        ])
    }

  useEffect(()=>{
    if(!chargedBesoin){
        //fetch besoin
        setBesoins({
        id: 1,
        statu: 0,
        minAge: 20,
        maxAge: 30,
        nbPosteDispo: 2,
        minExperience: 2,
        metierNom: "Développeur Full-Stack",
        departementNom: "Département Informatique",
        competences: ["React", "Node.js", "MongoDB", "TypeScript"],
        langues: ["Français", "Anglais"],
        diplomes: ["Licence en Informatique", "Master en Développement Web"],
        })
        setChargedBesoin(true);
    }
    if(chargedBesoin && !chargedCvs){
        //fetch cvs

        getAllCv();
        setChargedCv(true)
    }
    
    if(chargedCvs && chargedBesoin){

    }


  },[cvs,chargedCvs,besoins,chargedBesoin])



  return (
    <div className="min-h-screen bg-gray-50 py-8 ">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Liste des CVs qui ont postule pour {chargedBesoin && besoins.metierNom} </h1>
          <p className="text-gray-600">Découvrez les profils talentueux de notre communauté</p>
        </div>

        {/* Filtres et recherche */}
        <div className="bg-white rounded-lg shadow p-6 mb-8">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            {/* Barre de recherche */}
            <div className="md:col-span-2">
              <label htmlFor="search" className="block text-sm font-medium text-gray-700 mb-1">
                Rechercher
              </label>
              <input
                type="text"
                id="search"
                placeholder="Nom, compétence, métier..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            {/* Filtre par ville */}
            <div>
              <label htmlFor="city" className="block text-sm font-medium text-gray-700 mb-1">
                Ville
              </label>
              <select
                id="city"
                value={selectedCity}
                onChange={(e) => setSelectedCity(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Toutes les villes</option>
                {cities.map(city => (
                  <option key={city} value={city}>{city}</option>
                ))}
              </select>
            </div>

            {/* Filtre par métier */}
            <div>
              <label htmlFor="job" className="block text-sm font-medium text-gray-700 mb-1">
                Métier
              </label>
              <select
                id="job"
                value={selectedJob}
                onChange={(e) => setSelectedJob(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Tous les métiers</option>
                {jobs.map(job => (
                  <option key={job} value={job}>{job}</option>
                ))}
              </select>
            </div>
          </div>
        </div>

        {/* Statistiques */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
          <div className="bg-white rounded-lg shadow p-4 text-center">
            <div className="text-2xl font-bold text-blue-600">{cvs.length}</div>
            <div className="text-gray-600">CVs au total</div>
          </div>
          <div className="bg-white rounded-lg shadow p-4 text-center">
            <div className="text-2xl font-bold text-green-600">{filteredCVs.length}</div>
            <div className="text-gray-600">Résultats filtrés</div>
          </div>
          <div className="bg-white rounded-lg shadow p-4 text-center">
            <div className="text-2xl font-bold text-purple-600">{cities.length}</div>
            <div className="text-gray-600">Villes différentes</div>
          </div>
        </div>

        {/* Liste des CVs */}
        {filteredCVs.length === 0 ? (
          <div className="bg-white rounded-lg shadow p-8 text-center">
            <div className="text-gray-400 text-6xl mb-4">🔍</div>
            <h3 className="text-lg font-medium text-gray-900 mb-2">Aucun résultat</h3>
            <p className="text-gray-600">Aucun CV ne correspond à vos critères de recherche.</p>
          </div>
        ) : (
          <>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mb-8">
              {filteredCVs.map(cv => (
                <Link to={`/cv/${cv.id}/${besoins.id}`} key={cv.id}>
                  <CompactCVPreview
                    cvData={cv}
                  />
                </Link>
              ))}
            </div>

            {/* Pagination (exemple) */}
            <div className="flex justify-center items-center space-x-2">
              <button className="px-3 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50">
                Précédent
              </button>
              <span className="px-3 py-2 text-gray-700">Page 1 sur 2</span>
              <button className="px-3 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50">
                Suivant
              </button>
            </div>
          </>
        )}

        {/* Bouton d'action */}
        <div className="fixed bottom-8 right-8">
          <Link to="/profile">
            <button
            className="bg-blue-600 text-white p-4 rounded-full shadow-lg hover:bg-blue-700 transition-colors flex items-center justify-center"
            >
            
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4v16m8-8H4" />
            </svg>
            </button>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default CVListAnnonce;