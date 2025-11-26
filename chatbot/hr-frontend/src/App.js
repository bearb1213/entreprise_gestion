import React from 'react';
import Chatbot from './Chatbot';

function App() {
  // Plus tard : ce userId viendra de mon système d'authentification
  const userId = '123';

  return (
    <div style={{ padding: 20 }}>
      <h1>Portail RH</h1>
      <p>Bienvenue sur votre portail de gestion RH.</p>

      <div style={{ marginTop: 20 }}>
        <Chatbot userId={userId} />
      </div>
    </div>
  );
}

export default App;