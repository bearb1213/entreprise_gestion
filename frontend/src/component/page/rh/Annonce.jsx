import React from "react";
import AnnonceCard from "../../template/AnnonceCard";
import CompactAnnonceCard from "../../template/CompactAnnonceCard";
import { Link } from "react-router-dom";

const Annonce = () => {
  const annonces = [
    {
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
    },
    {
      id: 2,
      statu: 1,
      minAge: 25,
      maxAge: 35,
      nbPosteDispo: 1,
      minExperience: 3,
      metierNom: "Data Scientist",
      departementNom: "Département Data",
      competences: ["Python", "Pandas", "Machine Learning", "TensorFlow"],
      langues: ["Français", "Anglais"],
      diplomes: ["Licence en Statistique", "Master en Intelligence Artificielle"],
    },
    {
      id: 3,
      statu: 0,
      minAge: 22,
      maxAge: 40,
      nbPosteDispo: 5,
      minExperience: 1,
      metierNom: "Designer UX/UI",
      departementNom: "Département Design",
      competences: ["Figma", "Photoshop", "Illustrator"],
      langues: ["Français"],
      diplomes: ["Licence en Design Graphique"],
    },
  ];

  return (
    <div className="mx-auto p-4 space-y-4">
      {annonces.map((annonce) => (
        <CompactAnnonceCard key={annonce.id} annonce={annonce} />
      ))}
        <div className="fixed bottom-8 right-8">
                <Link to="/formAnnonce">
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
  );
};

export default Annonce;
