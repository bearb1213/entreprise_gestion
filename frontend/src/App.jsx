import { useState } from 'react'
import './assets/App.css'
import SideBar from './component/SideBar.jsx'
import CvForm from './component/CvForm.jsx'

function App() {
  const [currentView, setCurrentView] = useState('dashboard');

  const renderContent = () => {
    switch (currentView) {
      case 'candidature':
        return <CvForm />;
      case 'dashboard':
      default:
        return (
          <div className="flex-1 p-8">
            <h1 className="text-3xl font-bold">Bienvenue sur l'app de Recrutement !</h1>
            <p className="mt-4 text-gray-700">
              Sélectionnez "Candidature" dans le menu pour accéder au formulaire de CV.
            </p>
          </div>
        );
    }
  };

  return (
    <div className="flex h-screen bg-gray-100">
      <SideBar active={currentView} onNavigate={setCurrentView} />
      <div className="flex-1 overflow-auto">
        {renderContent()}
      </div>
    </div>
  )
}

export default App