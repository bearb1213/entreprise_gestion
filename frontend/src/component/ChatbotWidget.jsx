import React, { useState } from 'react';
import Chatbot from './Chatbot'; // adapte le chemin si besoin

function ChatbotWidget({ userId }) {
  const [isOpen, setIsOpen] = useState(false);

  const toggleWidget = () => setIsOpen((prev) => !prev);

  return (
    <>
      {/* Bouton flottant */}
      <button
        onClick={toggleWidget}
        className="fixed bottom-6 right-6 z-40 rounded-full shadow-lg flex items-center justify-center"
        style={{
          width: 60,
          height: 60,
          background:
            'linear-gradient(135deg, #2563eb 0%, #4f46e5 50%, #6366f1 100%)',
          color: '#f9fafb',
          border: 'none',
          cursor: 'pointer',
          boxShadow: '0 18px 30px rgba(15, 23, 42, 0.35)',
          transition: 'transform 0.1s ease, box-shadow 0.1s ease, opacity 0.15s ease',
        }}
        onMouseDown={(e) => {
          e.currentTarget.style.transform = 'scale(0.95)';
          e.currentTarget.style.boxShadow = '0 10px 18px rgba(15,23,42,0.30)';
        }}
        onMouseUp={(e) => {
          e.currentTarget.style.transform = 'scale(1)';
          e.currentTarget.style.boxShadow = '0 18px 30px rgba(15,23,42,0.35)';
        }}
      >
        {!isOpen ? (
          // Icône "bulle de chat"
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="26"
            height="26"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z" />
          </svg>
        ) : (
          // Icône "croix" pour fermer
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="22"
            height="22"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2.2"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <line x1="18" y1="6" x2="6" y2="18" />
            <line x1="6" y1="6" x2="18" y2="18" />
          </svg>
        )}
      </button>

      {/* Fenêtre de chat flottante */}
      {isOpen && (
        <div
          className="fixed bottom-24 right-6 z-30"
          style={{ width: 420, maxWidth: '90vw', height: 540, maxHeight: '80vh' }}
        >
          <Chatbot userId={userId} />
        </div>
      )}
    </>
  );
}

export default ChatbotWidget;