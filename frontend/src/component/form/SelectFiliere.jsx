import {React , useEffect ,useState} from "react";
import Select from "./Select";

const SelectFiliere = ({name , values, index,onChange ,error =''}) =>{
    const [diplomes , setDiplomes] = useState([]);
    const [diplomeSelected , setDiplomeSelected] = useState(null);
    const [diplomeIsSelected , setDiplomeIsSelected] = useState(false);
    const [diplomeIsCharged , setDiplomeIsCharged] = useState(false);
    const [filieres , setFilieres] = useState([]);
    const [filiereIsCharged , setFiliereIsCharged] = useState(false)



    const handleChoiceDiplome = (e) => {
        const { name, value, type, checked } = e.target;
        setDiplomeSelected(value);
        if(value) {
            setDiplomeIsSelected(true)
        } else {
            setDiplomeIsSelected(false)
            setFilieres([]);
        }
    };

    const handleChoiceFiliere = (e) =>{
        const { name, value, type, checked } = e.target;
        values = value;
        if(onChange){
            onChange(index,value)
        }
    }


    
    useEffect(()=>{
        // on fais fetch ici
        
        if(!diplomeIsCharged){
            let diplomeFetched = [
            ];
            
            fetch("http://localhost:8080/api/filieres", {credentials: "include"}) 
            .then((response) => response.json())
            .then((data) => {
                diplomeFetched = data;
                setDiplomes(diplomeFetched.map(item => ({
                    value: item.id,
                    label : item.libelle
                })));
                setDiplomeIsCharged(true)
            })
            .catch((error) => {
                alert(`Erreur fetch diplome: ${error.message}`);
                // Gérer l'erreur de manière appropriée, par exemple en affichant un message à l'utilisateur
            }); 
        }

        if (diplomeIsCharged && diplomeSelected && !filiereIsCharged){
            // on fais fetch ihany
            let filiereFetched = [];
            fetch(`http://localhost:8080/api/filieres/${diplomeSelected}/diplomes`, {credentials: "include"})
            .then((response) => response.json())
            .then((data) => {
                filiereFetched = data;
                setFilieres(filiereFetched.map(item => ({
                    value : item.id,
                    label : item.libelle
                })));
                setFiliereIsCharged(true)
            })
            .catch((error) => {
                alert(`Erreur fetch diplome 2 : ${error.message}`);
            })
        }
    },[diplomes ,diplomeIsCharged,diplomeSelected,filieres ,filiereIsCharged])



        return (
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                <Select
                label="Diplome"
                name="diplome"
                value={diplomeSelected}
                onChange={handleChoiceDiplome}
                options={diplomes}
                required
                />

            {diplomeIsSelected && (
                
                <Select
                label="Filiere"
                name={name}
                value={values}
                onChange={handleChoiceFiliere}
                options={filieres}
                required 
                error={error}
                />
                )
            }
        </div>
        );
    

}
export default SelectFiliere ;