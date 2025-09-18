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

export default function SideBar({ active = "Dashboard", roles = null }) {
  const navigate = useNavigate();
  const [activeItem, setActiveItem] = useState(active);
  const [menuItems, setMenuItems] = useState([]);

  useEffect(() => {
    if (roles == "ADMIN") {
      setMenuItems([
        { id: 1, name: "Dashboard", path: "/dashboard", icon: DashboardIcon },
        { id: 2, name: "Profile", path: "/profile", icon: ProfileIcon },
      ]);
    } else if (roles == "DEPARTMENT_CHIEF") {
      setMenuItems([
        { id: 1, name: "Dashboard", path: "/dashboard", icon: DashboardIcon },
        { id: 2, name: "Profile", path: "/profile", icon: ProfileIcon },
        { id: 4, name: "Settings", path: "/settings", icon: SettingsIcon },
      ]);
    } else {
      setMenuItems([
        { id: 1, name: "Dashboard", path: "/dashboard", icon: DashboardIcon },
        { id: 2, name: "Profile", path: "/profile", icon: ProfileIcon },
        { id: 3, name: "Tasks", path: "/tasks", icon: TasksIcon },
      ]);
    }
  }, [roles]);

  const handleNavigation = (path, name) => {
    navigate(path);
    setActiveItem(name);
  };

  const handleLogout = () => {
    // Add logout logic here
    console.log("User logged out");
    navigate("/login");
  };

  return (
    <div className="w-64 bg-gradient-to-b from-gray-800 to-gray-900 text-white h-screen flex flex-col shadow-xl fixed">
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