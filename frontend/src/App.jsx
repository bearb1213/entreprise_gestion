import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './assets/App.css'
import SideBar from './component/SideBar.jsx' 

function App() {
  const [count, setCount] = useState(0)

  return (
    <div className="flex">
      <SideBar active='Profile' />
      <div className="flex-1 p-8">
        <h1 className="text-3xl font-bold">Bienvenue sur l'app !</h1>
        <p className="mt-4 text-gray-700">
          Contenu principal à droite de la sidebar.
        </p>
      </div>
    </div>
  )
}

export default App
