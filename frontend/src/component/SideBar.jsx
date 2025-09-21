import React from "react";
import { Link, useLocation } from "react-router-dom";

// Icônes SVG intégrées
const DashboardIcon = () => (
  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
  </svg>
);

const CandidatureIcon = () => (
  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
  </svg>
);

const LogoutIcon = () => (
  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
  </svg>
);

export default function SideBar() {
  const location = useLocation();
  const [menuItems] = React.useState([
    { id: 1, name: "Dashboard", path: "/dashboard", icon: DashboardIcon },
    { id: 2, name: "Candidature", path: "/candidature", icon: CandidatureIcon },
  ]);

  const handleLogout = () => {
    console.log("User logged out");
  };

  return (
    <div className="w-64 bg-gradient-to-b from-gray-800 to-gray-900 text-white h-screen flex flex-col shadow-xl">
      <h2 className="text-xl font-bold p-5 border-b border-gray-700 flex items-center">
        <span className="bg-blue-500 p-2 rounded-lg mr-3">
          <DashboardIcon />
        </span>
        Recrutement App
      </h2>
      
      <nav className="flex-1 overflow-y-auto py-4">
        <ul className="px-3">
          {menuItems.map((item) => {
            const IconComponent = item.icon;
            const isActive = location.pathname === item.path;
            
            return (
              <li
                key={item.id}
                className={`mb-2 rounded-lg transition-all duration-200 ${
                  isActive
                    ? "bg-blue-600 text-white shadow-md"
                    : "text-gray-300 hover:bg-gray-700 hover:text-white"
                }`}
              >
                <Link
                  to={item.path}
                  className="flex items-center p-3 cursor-pointer"
                >
                  <IconComponent />
                  <span className="ml-3">{item.name}</span>
                </Link>
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