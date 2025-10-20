import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './assets/App.css'
import SideBar from './component/SideBar.jsx' 
<<<<<<< Updated upstream

function App() {
  const [count, setCount] = useState(0)
=======
import Dashboard from './component/page/rh/Dashboard.jsx'
import FormCandidat from './component/page/rh/FormCandidat.jsx'
import Cv from './component/page/rh/Cv.jsx';
import LogEmploye from './component/page/rh/LogEmloye.jsx';
import CVList from './component/page/rh/CVList.jsx';
import Questionnaire from './component/Questionnaire';
import EntretienNote from './component/page/rh/EntretienNote';
import ImageSaver from './component/page/test/ImageSaver.jsx';
import AnnonceDetail from './component/page/rh/AnnonceDetail.jsx';
import FormAnnonce from './component/page/rh/FormAnnonce.jsx';
import DepartementDashboard from './component/page/rh/DepartementDashboard.jsx';
import UniteDashboard from "./component/page/rh/UniteDashboard.jsx"
import CandidaturesListParBesoin from "./component/page/rh/CandidaturesListParBesoin.jsx"
import CandidatureDetail from "./component/page/rh/CandidatureDetail.jsx"
import FormulaireEntretien from "./component/page/rh/FormulaireEntretien"
import DashboardRH from './component/page/rh/DashboardRH';
import DashboardAdmin  from './component/page/rh/DashboardAdmin.jsx';



function App() {
  const location = useLocation(); 
  const navigate = useNavigate();
  const [showSidebar, setShowSidebar] = useState(false);
  const [userInfo, setUserInfo] = useState(null);
  const [loading, setLoading] = useState(true);

  // Fetch user info on component mount
  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/utilisateur/me", {
          credentials: "include"
        });

        if (response.ok) {
          const data = await response.json();
          setUserInfo(data);
          console.log("User info:", data);
        } else {
          console.log("User not authenticated or session expired");
          setUserInfo(null);
        }
      } catch (error) {
        console.error("Error fetching user info:", error);
        setUserInfo(null);
      } finally {
        setLoading(false);
      }
    };

    fetchUserInfo();
  }, []);

  // Determine if sidebar should be shown
  useEffect(() => {
    const showSidebarRoutes = ['/dashboard', "/profile", "/CvList", '/settings'];
    const shouldShowSidebar = showSidebarRoutes.some(route =>
      location.pathname.startsWith(route)
    );

    setShowSidebar(shouldShowSidebar);
  }, [location.pathname]);
>>>>>>> Stashed changes

  // Extract role from user info
  const getUserRole = () => {
    if (!userInfo || !userInfo.roles) return null;

    // Extract role from authorities (assuming roles are in the format "ROLE_XXX")
    const authorities = userInfo.roles;
    if (Array.isArray(authorities)) {
      const roleAuthority = authorities.find(auth => 
        auth.authority && auth.authority.startsWith('ROLE_')
      );
      if (roleAuthority) {
        return roleAuthority.authority.replace('ROLE_', '');
      }
    }
    
    // Fallback to check the roles array directly
    if (userInfo.roles.includes('ROLE_ADMIN')) return 'ADMIN';
    if (userInfo.roles.includes('ROLE_DEPARTMENT')) return 'DEPARTMENT';
    if (userInfo.roles.includes('ROLE_RH')) return 'RH';
    
    return null;
  };

  const userRole = getUserRole();

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-gray-100">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto"></div>
          <p className="mt-4 text-gray-600">Chargement...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex">
<<<<<<< Updated upstream
      <SideBar active='Profile' />
      <div className="flex-1 p-8">
        <h1 className="text-3xl font-bold">Bienvenue sur l'app !</h1>
        <p className="mt-4 text-gray-700">
          Contenu principal à droite de la sidebar.
        </p>
=======
      {showSidebar && (
        <SideBar 
          active={location.pathname}
          role={userRole}
          navigate={navigate}
        />
      )}
      <div className={`flex-1 overflow-auto p-6 ${showSidebar ? 'ml-64' : ''}`}>
        <Routes>
          <Route path='/' element={<LogEmploye navigate={navigate} />} />
          <Route path='/Cv/:id' element={<Cv navigate={navigate} />} />
          <Route path='/profile' element={<FormCandidat navigate={navigate} />} />
          <Route path='/dashboard' element={<Dashboard navigate={navigate} />} />
          <Route path='/CvList' element={<CVList navigate={navigate} />} />
          <Route path='/sary' element={<ImageSaver />} />
          <Route path="/entretien" element={<EntretienNote />} />
          <Route path="/questionnaire/:candidatureId" element={<Questionnaire />} />
          <Route path="/annonce/:id" element={<AnnonceDetail />} />
          <Route path="/creer-annonce" element={<FormAnnonce />} />
          <Route path="/dashboard-departement" element={<DepartementDashboard />} />
          <Route path="/dashboard-unite" element={<UniteDashboard />} />
          <Route path="/candidatures/:id" element={<CandidaturesListParBesoin />} />
          <Route path="/candidature-detail/:id" element={<CandidatureDetail />} />
          <Route path="/entretien/:candidatureId" element={<FormulaireEntretien />} />
          <Route path="/dashboard-rh" element={<DashboardRH navigate={navigate} />} />
          <Route path="/questionnaire/:candidatureId" element={<Questionnaire />} />
          <Route path="/entretien/:id/evaluation" element={<EntretienNote />} />
          <Route path="/dashboard-admin" element={<DashboardAdmin navigate={navigate} />} />
        </Routes>
>>>>>>> Stashed changes
      </div>
    </div>
  )
}

export default App
