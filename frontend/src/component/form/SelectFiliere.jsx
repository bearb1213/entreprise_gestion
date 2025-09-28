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
            const diplomeFetched = [
                {id: 0 , libelle : "Bacc"},
                {id: 1 , libelle: "license"},
                {id: 2 , libelle : "master"},
                {id: 3 , libelle: "doctorat"}
            ];
            //
            setDiplomes(diplomeFetched.map(item => ({
                value: item.id,
                label : item.libelle
            })));
            setDiplomeIsCharged(true)
        }

        if (diplomeIsCharged && !filiereIsCharged){
            // on fais fetch ihany
            const filiereFetched = [ 
                {id:0 , libelle:"S"},
                {id:1 , libelle:"info"},
                {id:2 , libelle: "commenrce"}
            ];
            
            setFilieres(filiereFetched.map(item => ({
                value : item.id,
                label : item.libelle
            })));
            setFiliereIsCharged(true)
        }
    },[diplomes ,diplomeIsCharged, filieres ,filiereIsCharged])



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