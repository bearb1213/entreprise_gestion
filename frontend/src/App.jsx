import { useState } from 'react';
import './assets/App.css';
import SideBar from './component/SideBar.jsx';
import ChatbotWidget from './component/ChatbotWidget.jsx'; 

function App() {
  const [count, setCount] = useState(0);
  const userId = '123'; // plus tard : user connecté

  return (
    <div className="flex">
      <SideBar active="Profile" />
      <div className="flex-1 p-8 relative">
        <h1 className="text-3xl font-bold">Bienvenue sur l'app !</h1>
        <p className="mt-4 text-gray-700">
          Contenu principal à droite de la sidebar.
        </p>

        
        <ChatbotWidget userId={userId} />
      </div>
    </div>
  );
}

export default App;