import { useState, useEffect } from 'react'
import { Routes, Route, useLocation, useNavigate } from 'react-router-dom';
import './assets/App.css'

import SideBar from './component/SideBar.jsx' 
import Dashboard from './component/page/rh/Dashboard.jsx'
import FormCandidat from './component/page/rh/FormCandidat.jsx'
import Cv from './component/page/rh/Cv.jsx';
import LogEmploye from './component/page/rh/LogEmloye.jsx';
import CVList from './component/page/rh/CVList.jsx';
import Annonce from './component/page/rh/Annonce.jsx';
import FormAnnonce from './component/page/rh/FormAnnonce.jsx';
import CVListAnnonce from './component/page/rh/CVListAnnonce.jsx';
import CVProgression from './component/page/rh/CVProgression.jsx';

function App() {
  const location = useLocation(); 
  const navigate = useNavigate;
  const [showSidebar, setShowSidebar] = useState(false);

  useEffect(() => {
    const showSidebarRoutes = ['/dashboard', "/profile", "/CvList", '/settings' , '/annonce'];
    const shouldShowSidebar = showSidebarRoutes.some(route => 
      location.pathname.startsWith(route)
    );
    
    setShowSidebar(shouldShowSidebar);
  }, [location.pathname]);

  return (
    <div className="flex">
      {showSidebar && <SideBar 
      active=''
      navigate={navigate}
      />}
      <div className={`flex-1 overflow-auto p-6 ${showSidebar ? 'ml-64' : ''}`}>
        <Routes>
          <Route path='/' element={<LogEmploye navigate={navigate}  />} />
          <Route path='/Cv/:id' element={<Cv navigate={navigate} />} />
          <Route path='/Cv/:idCandidat/:idBesoin' element={<CVProgression navigate={navigate} />} />
          <Route path='/profile' element={<FormCandidat navigate={navigate} />} />
          <Route path='/dashboard' element={<Dashboard navigate={navigate} />} />
          <Route path='/CvList' element={<CVList  />} />
          <Route path='/CvList/:id' element={<CVListAnnonce  />} />
          <Route path='/annonce' element={<Annonce  />} />
          <Route path='/formAnnonce' element={<FormAnnonce />} />

        </Routes>
      </div>
    </div>
  )
}

export default App