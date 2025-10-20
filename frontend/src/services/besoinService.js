const API_BASE_URL = 'http://localhost:8080/api';

class BesoinService {
    // Récupérer toutes les annonces
    getAllBesoins() {
        try {
            const response = fetch(`${API_BASE_URL}/besoins` , {credentials : "include"});
            console.log(response);
            if (!response.ok) {
                throw new Error(`Error`);
            }
            return response.json();
        } catch (error) {
            console.error('Erreur lors de la récupération des annonces:', error);
            throw error;
        }
    }

    // Récupérer tous les métiers
    getMetiers() {
        try {
            const response = fetch(`${API_BASE_URL}/metiers` , {credentials : "include"});
            console.log(response);

            //if (!response.ok) {
            //    throw new Error(`Error`);
            //}
            const data = response.json();
            // Transformer en format attendu par les composants
            return response.map(metier => ({
                value: metier.id,
                label: metier.libelle
            }));
        } catch (error) {
            console.error('Erreur lors de la récupération des métiers:', error);
            throw error;
        }
    }

    // Récupérer tous les départements
    getDepartements() {
        try {
            const response = fetch(`${API_BASE_URL}/departements` , {credentials : "include"});
            console.log(response);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = response.json();
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
    getCompetences() {
        try {
            const response = fetch(`${API_BASE_URL}/competences` , {credentials : "include"});
            console.log(response);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = response.json();
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
    getLangues() {
        try {
            const response = fetch(`${API_BASE_URL}/langues` , {credentials : "include"});
            console.log(response);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = response.json();
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
    getDiplomeFilieres() {
        try {
            const response = fetch(`${API_BASE_URL}/diplomes-filieres` , {credentials : "include"});
            console.log(response);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = response.json();
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
    createBesoin(besoinData) {
        try {
            const response = fetch(`${API_BASE_URL}/besoins`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(besoinData),
                credentials : "include",
            });
            
            return response;
        } catch (error) {
            console.error('Erreur lors de la création de l\'annonce:'+ error.message);
            throw error;
        }
    }

    // Filtrer les annonces
    filterBesoins(criteria) {
        try {
            const response = fetch(`${API_BASE_URL}/filtre`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(criteria), 
                credentials : "indlude",

            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            return response.json();
        } catch (error) {
            console.error('Erreur lors du filtrage des annonces:', error);
            throw error;
        }
    }
}

export default new BesoinService();