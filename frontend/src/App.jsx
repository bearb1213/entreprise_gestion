import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './assets/App.css'

import Dashboard from './component/page/rh/Dashboard.jsx'
import Form from './component/page/test/Form.jsx'
import ImageSaver from './component/page/test/ImageSaver.jsx'

import SideBar from './component/SideBar.jsx' 


import FormCandidat from './component/page/rh/FormCandidat.jsx'

function App() {
  const [count, setCount] = useState(0)

  return (
    <div className="flex">
      <SideBar active='Profile' />
      <div className="flex-1 p-8">
        <FormCandidat/>
      </div>
    </div>
  )
}

export default App
