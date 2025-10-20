// src/Sidebar.jsx
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

// Icônes SVG intégrées
const DashboardIcon = () => (
  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
  </svg>
);

const ProfileIcon = () => (
  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
  </svg>
);

const SettingsIcon = () => (
  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
  </svg>
);

const TasksIcon = () => (
  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
  </svg>
);

const LogoutIcon = () => (
  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
  </svg>
);

export default function SideBar({ active = "Dashboard", role = null }) {
  const navigate = useNavigate();
  const [activeItem, setActiveItem] = useState(active);
  const [menuItems, setMenuItems] = useState([]);
  const [userInfo, setUserInfo] = useState(null);
    const [loading, setLoading] = useState(true);
    const [roles, setRoles] = useState(null);


    const getUserRole = async () => {
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
      console.log("UserInfo dans sidebar :", userInfo);
      setRoles(getUserRole());
      console.log("Roles dans sidebar :", roles);
    if (roles == "ADMIN") {
      setMenuItems([
        { id: 1, name: "Liste CV", path: "/CvList", icon: TasksIcon },
      ]);
    } else if (roles == "DEPARTEMENT") {
      setMenuItems([
        { id: 1, name: "Dashboard", path: "/departement-dashboard", icon: DashboardIcon },
        { id: 2, name: "Creer une annonce", path: "/creer-annonce", icon: ProfileIcon },
      ]);
    } else {
      setMenuItems([
        { id: 1, name: "Dashboard", path: "/dashboard", icon: DashboardIcon },
        { id: 2, name: "Profile", path: "/profile", icon: ProfileIcon },
<<<<<<< Updated upstream
        { id: 3, name: "Tasks", path: "/tasks", icon: TasksIcon },
=======
>>>>>>> Stashed changes
      ]);
    }
  }, []);



  const handleNavigation = (path, name) => {
    navigate(path);
    setActiveItem(name);
  };

<<<<<<< Updated upstream
  const handleLogout = () => {
    // Add logout logic here
    console.log("User logged out");
    navigate("/login");
=======
  const handleLogout = async () => {
    try {
      // Appeler l'API de logout
      const response = await fetch("http://localhost:8080/api/utilisateur/logout", {
        method: 'POST',
        credentials: "include"
      });

      if (response.ok) {
        console.log("Déconnexion réussie");
      } else {
        console.error("Erreur lors de la déconnexion");
      }
    } catch (error) {
      console.error("Erreur lors de la déconnexion:", error);
    } finally {
      // Rediriger vers la page de login dans tous les cas
      navigate("/");
    }
>>>>>>> Stashed changes
  };

  return (
    <div className="w-64 bg-gradient-to-b from-gray-800 to-gray-900 text-white h-screen flex flex-col shadow-xl">
      <h2 className="text-xl font-bold p-5 border-b border-gray-700 flex items-center">
        <span className="bg-blue-500 p-2 rounded-lg mr-3">
          <DashboardIcon />
        </span>
        My App
      </h2>
      
      <nav className="flex-1 overflow-y-auto py-4">
        <ul className="px-3">
          {menuItems.map((item) => {
            const IconComponent = item.icon;
            return (
              <li
                key={item.id}
                className={`mb-2 rounded-lg transition-all duration-200 ${
                  activeItem === item.name
                    ? "bg-blue-600 text-white shadow-md"
                    : "text-gray-300 hover:bg-gray-700 hover:text-white"
                }`}
                onClick={() => handleNavigation(item.path, item.name)}
              >
                <div className="flex items-center p-3 cursor-pointer">
                  <IconComponent />
                  <span className="ml-3">{item.name}</span>
                </div>
              </li>
            );
          })}
        </ul>
      </nav>
      
      <div className="p-4 border-t border-gray-700">
        <div
          className="flex items-center p-3 rounded-lg cursor-pointer text-gray-300 hover:bg-gray-700 hover:text-white transition-colors"
          onClick={handleLogout}
        >
          <LogoutIcon />
          <span className="ml-3">Logout</span>
        </div>
      </div>
    </div>
  );
}