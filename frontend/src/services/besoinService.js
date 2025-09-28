const API_BASE_URL = 'http://localhost:8080/api/besoins';

class BesoinService {
    // Récupérer toutes les annonces
    async getAllBesoins() {
        try {
            const response = await fetch(`${API_BASE_URL}/besoins`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('Erreur lors de la récupération des annonces:', error);
            throw error;
        }
    }

    // Récupérer tous les métiers
    async getMetiers() {
        try {
            const response = await fetch(`${API_BASE_URL}/metiers`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            // Transformer en format attendu par les composants
            return data.map(metier => ({
                value: metier.id,
                label: metier.libelle
            }));
        } catch (error) {
            console.error('Erreur lors de la récupération des métiers:', error);
            throw error;
        }
    }

    // Récupérer tous les départements
    async getDepartements() {
        try {
            const response = await fetch(`${API_BASE_URL}/departements`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            return data.map(dept => ({
                value: dept.id,
                label: dept.libelle
            }));
        } catch (error) {
            console.error('Erreur lors de la récupération des départements:', error);
            throw error;
        }
    }

    // Récupérer toutes les compétences
    async getCompetences() {
        try {
            const response = await fetch(`${API_BASE_URL}/competences`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            return data.map(comp => ({
                value: comp.id,
                label: comp.libelle
            }));
        } catch (error) {
            console.error('Erreur lors de la récupération des compétences:', error);
            throw error;
        }
    }

    // Récupérer toutes les langues
    async getLangues() {
        try {
            const response = await fetch(`${API_BASE_URL}/langues`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            return data.map(langue => ({
                value: langue.id,
                label: langue.libelle
            }));
        } catch (error) {
            console.error('Erreur lors de la récupération des langues:', error);
            throw error;
        }
    }

    // Récupérer les diplômes-filières
    async getDiplomeFilieres() {
        try {
            const response = await fetch(`${API_BASE_URL}/diplomes-filieres`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            return data.map(df => ({
                value: df.id,
                label: `${df.diplome.libelle} - ${df.filiere.libelle}`
            }));
        } catch (error) {
            console.error('Erreur lors de la récupération des diplômes-filières:', error);
            throw error;
        }
    }

    // Créer une nouvelle annonce
    async createBesoin(besoinData) {
        try {
            const response = await fetch(`${API_BASE_URL}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(besoinData)
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Erreur lors de la création de l\'annonce:', error);
            throw error;
        }
    }

    // Filtrer les annonces
    async filterBesoins(criteria) {
        try {
            const response = await fetch(`${API_BASE_URL}/filtre`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(criteria)
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Erreur lors du filtrage des annonces:', error);
            throw error;
        }
    }
}

export default new BesoinService();