import { React, useState ,useEffect} from "react";
import SelectFiliere from "./SelectFiliere";

const MultiSelectFiliere = ({ value: externalValues = [], onChange }) => {
    const [selects, setSelects] = useState([]);
    const [internalValues, setInternalValues] = useState(externalValues);

    // Synchroniser avec les valeurs externes si elles changent
    useEffect(() => {
        setInternalValues(externalValues);
    }, [externalValues]);

    // Ajouter un nouveau select
    const addSelect = () => {
        const newId = Date.now(); // ID unique
        setSelects(prev => [...prev, { id: newId }]);
        
        // Ajouter une valeur vide au tableau
        const newValues = [...internalValues, ''];
        setInternalValues(newValues);
        
        // Notifier le parent du changement
        if (onChange) {
            onChange(newValues);
        }
    };

    // Supprimer le dernier select
    const removeSelect = () => {
        if (selects.length > 0) {
            setSelects(prev => prev.slice(0, -1)); // Retire le dernier
            
            // Retirer la dernière valeur du tableau
            const newValues = internalValues.slice(0, -1);
            setInternalValues(newValues);
            
            // Notifier le parent du changement
            if (onChange) {
                onChange(newValues);
            }
        }
    };

    // Gérer le changement d'un select individuel
    const handleFiliereChange = (index, newValue) => {
        const newValues = [...internalValues];
        newValues[index] = newValue;
        
        setInternalValues(newValues);
        console.log("filiere new Value")
        console.log(newValue)
        
        // Notifier le parent du changement
        if (onChange) {
            onChange(newValues);
        }
    };

    return (
        <div className="space-y-4 p-4">
            <div className="flex space-x-4 mb-4">
                <button
                    type="button"
                    onClick={addSelect}
                    className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                    + Ajouter une filière
                </button>
                
                <button
                    type="button"
                    onClick={removeSelect}
                    disabled={selects.length === 0}
                    className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 disabled:bg-gray-300"
                >
                    - Supprimer une filière
                </button>
            </div>

            <div className="space-y-4">
                {selects.map((select, index) => (
                    <div key={select.id} className="border p-4 rounded-lg">
                        <h3 className="text-lg font-semibold mb-2">
                            Filière {index + 1}
                        </h3>
                        
                        <SelectFiliere
                            name={`filiere-${index}`}
                            value={externalValues[index] || ''}
                            index = {index}
                            onChange={handleFiliereChange}
                            error={null}
                        />
                        
                    </div>
                ))}
            </div>

            {selects.length === 0 && (
                <div className="text-center text-gray-500 py-8">
                    Cliquez sur "+ Ajouter une filière" pour commencer
                </div>
            )}

        </div>
    );
};

export default MultiSelectFiliere;