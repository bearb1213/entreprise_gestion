import React, { useState } from 'react';
import Card from '../../card/Card';
import CardGrid from '../../card/CardGrid';
import CardList from '../../card/CardList';
import { CardActions, CardButton } from '../../card/CardActions';
import { CardSkeleton, CardGridSkeleton } from '../../card/CardSkeleton';

const Dashboard = () => {
  const [loading, setLoading] = useState(false);

  const annonces = [
    {
      id: 1,
      title: 'Developpeur',
      description: 'Nous cherchon des developpeurs passionner et motiver , jeune , car la jeunesse est l\'avenir',
      image: 'fond1.jpeg',
      price: '29.99€',
      category: 'Électronique'
    },
    {
      id: 2,
      title: 'Produit Standard', 
      description: 'Une solution abordable pour vos besoins quotidiens',
      image: 'https://via.placeholder.com/300/10B981/FFFFFF?text=Produit+2',
      price: '49.99€',
      category: 'Maison'
    },
    {
      id: 3,
      title: 'Produit Pro',
      description: 'Pour les professionnels exigeants',
      image: 'https://via.placeholder.com/300/F59E0B/FFFFFF?text=Produit+3',
      price: '99.99€',
      category: 'Bureau'
    }
  ];

  const listItems = [
    { 
      id: 1, 
      title: 'Nouvelle commande', 
      description: 'Commande #1234 a été passée', 
      meta: '2 hours ago', 
      status: 'completed' 
    },
    { 
      id: 2, 
      title: 'Message reçu', 
      description: 'Nouveau message de Jean Dupont', 
      meta: '1 day ago', 
      status: 'pending' 
    },
    { 
      id: 3, 
      title: 'Mise à jour système', 
      description: 'Le système a été mis à jour vers la version 2.0', 
      meta: '3 days ago', 
      status: 'active' 
    }
  ];

  const handleAddToCart = (annonce) => {
    setCart(prev => [...prev, annonce]);
    alert(`${annonce.title} ajouté au panier !`);
  };

  const simulateLoading = () => {
    setLoading(true);
    setTimeout(() => setLoading(false), 2000);
  };

  if (loading) {
    return (
      <div className="p-6 max-w-6xl mx-auto">
        <h1 className="text-3xl font-bold mb-8">Chargement en cours...</h1>
        <CardGridSkeleton count={6} columns={3} />
      </div>
    );
  }

  return (
    <div className="p-6 max-w-6xl mx-auto">
      {/* En-tête */}
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Dashboard Test</h1>
        <div className="flex gap-4">
          
          <button
            onClick={simulateLoading}
            className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-colors"
          >
            Simuler chargement
          </button>
        </div>
      </div>

      {/* Grid de produits */}
      <section className="mb-12">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-gray-800">Nos Annonces</h2>
        </div>
        <CardGrid columns={3}>
          {annonces.map((annonce) => (
            <Card
              key={annonce.id}
              title={annonce.title}
              description={annonce.description}
              imageUrl={annonce.image}
              width={450}
              height={450}
              border={true}
              footer={
                <div className="flex justify-between items-center">
                  
                  <CardActions>
                    <CardButton 
                      variant="primary"
                      onClick={() => handleAddToCart(annonce)}
                    >
                      Postuler
                    </CardButton>
                    <CardButton variant="secondary">
                      Détails
                    </CardButton>
                  </CardActions>
                </div>
              }
            />
          ))}
        </CardGrid>
      </section>

      {/* Liste d'activités */}
      <section className="mb-12">
        <h2 className="text-2xl font-bold text-gray-800 mb-6">Dernières activités</h2>
        <CardList 
          items={listItems}
          onItemClick={(item) => alert(`Clic sur: ${item.title}`)}
        />
      </section>

      {/* Statistiques */}
      <section>
        <h2 className="text-2xl font-bold text-gray-800 mb-6">Statistiques</h2>
        <CardGrid columns={3}>
          <Card
            title="Ventes totales"
            description="Ce mois-ci"
            width={250}
            height={200}
            footer={<span className="text-2xl font-bold text-green-600">2,456€</span>}
            variant="success"
          />
          <Card
            title="Utilisateurs"
            description="Actifs"
            width={250}
            height={200}
            footer={<span className="text-2xl font-bold text-blue-600">1,234</span>}
            variant="primary"
          />
          <Card
            title="Taux de conversion"
            width={250}
            height={200}
            description="Global"
            footer={<span className="text-2xl font-bold text-purple-600">24.5%</span>}
            variant="warning"
          />
        </CardGrid>
      </section>
    </div>
  );
};

export default Dashboard;