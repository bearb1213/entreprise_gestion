import { useState } from 'react'
import './assets/App.css'
import SideBar from './component/SideBar.jsx'
import CvForm from './component/CvForm.jsx'
import Questionnaire from './component/Questionnaire.jsx'
import EntretienNote from './component/EntretienNote.jsx'
import { Routes, Route, Navigate } from "react-router-dom";

function App() {
  return (
    <div className="flex h-screen bg-gray-100">
      <SideBar />
      <div className="flex-1 overflow-auto">
        <Routes>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route 
            path="/dashboard" 
            element={
              <div className="flex-1 p-8">
                <h1 className="text-3xl font-bold">Bienvenue sur l'app de Recrutement !</h1>
                <p className="mt-4 text-gray-700">
                  Sélectionnez "Candidature" dans le menu pour accéder au formulaire de CV.
                </p>
              </div>
            } 
          />
          <Route path="/candidature" element={<CvForm />} />
          <Route path="/entretien" element={<EntretienNote />} />
          <Route path="/questionnaire/:candidatureId" element={<Questionnaire />} />
        </Routes>
      </div>
    </div>
  )
}

export default App