import {useState , useEffect, use} from "react";
import InputText from "../../form/InputText";
import InputDate from "../../form/InputDate";
import RadioGroup from "../../form/RadioGroup";
import Select from "../../form/Select";
import TextArea from "../../form/TextArea";
import Button from "../../form/Button";
import Checkbox from "../../form/Checkbox";
import SelectFiliere from "../../form/SelectFiliere";
import MultiSelectFiliere from "../../form/MultiSelectFiliere";
import MultiCheckbox from "../../form/MultiCheckbox";
import MultiSelectInputNumber from "../../form/MultiSelectInputNumber";
import ImageProcessor from "../../form/ImageProcessor";

const FormCandidat = ({navigate}) =>{
// donnee pour la page
    const [page , setPage] = useState(0);
    
    const [formData , setFormData] = useState({
        nom: '',
        prenom: '',
        date_naissance:'',
        genre:'',
        email: '',
        telephone:'',
        ville:'',
        description:'',

        langues:[],
        filiere:[],
        competences:[],
        metiers:[]
    });

    const genres = [
        {value:0 , label:"Homme"} , 
        {value:1 , label:"Femme"}
    ] ;

    const [langues , setLangues] = useState([])
    const [chargedLangue , setChargedLangue] = useState(false)

    const [competences , setCompetences] = useState([])
    const [chargedCompetences , setChargedCompetences] = useState(false)

    const [metiers , setMetiers] = useState([])
    const [chargedMetiers , setChargedMetiers ] = useState(false)

    const [previewImage , setPreviewImage] = useState(null)



    const [errors,setErrors] = useState({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    

/// fonctions handle
    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({
        ...formData,
        [name]: type === 'checkbox' ? checked : value,
        });

        // Clear error when user starts typing
        if (errors[name]) {
            setErrors({
                ...errors,
                [name]: '',
            });
        }
    };

    const handlePage = (e) => {
        if (page==1) {
            setPage(page-1);

        } else if (page==0){
            setPage(page+1);
        }
    }

    const handleReinitialize = (e) => {
        if(page==0){
            setFormData({
                ...formData,
                nom:'',
                prenom:'',
                image:null,
                date_naissance:'',
                genre:'',
                email: '',
                telephone:'',
                ville:'',
                description:'',
            });
        } else if (page==1){
            setFormData({
                ...formData,
                langues:[],
                filiere:[],
                competences:[]
            })
        }
    }

    const handleChangeLangue = (newValues) =>{
        setFormData({
            ...formData,
            langues : newValues
        })
    }

    const handleChangeFiliereDiplome =(newValues)=> {
        setFormData({
            ...formData,
            filiere : newValues
        })
    }    

    const handleChangeCompetence =(newValues)=> {
        setFormData({
            ...formData,
            competences : newValues
        })
    }    

    const handleChangeMetier =(newValues)=> {


        setFormData({
            ...formData,
            metiers : newValues
        })
    }   

    const handelChangeImage = (file,previewUrl)=>{
        setFormData({
            ...formData,
            image : file
        })
        setPreviewImage(previewUrl)
    }






    const validateFormData = (formData) => {
    // Vérification des champs obligatoires simples
    const requiredFields = [
        'nom', 'prenom', 'date_naissance', 'genre', 
        'email', 'ville', 'description'
    ];

    for (const field of requiredFields) {
        if (!formData[field] || formData[field].trim() === '') {
        alert(`Champ manquant: ${field}`);
        return false;
        }
    }

    // Validation spécifique de l'email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
        alert('Email invalide');
        return false;
    }
    if (formData.telephone){
        const phoneRegex = /^[0-9+\s()-]{8,20}$/;
        if (!phoneRegex.test(formData.telephone.replace(/\s/g, ''))) {
            alert('Téléphone invalide');
            return false;
        }
    }

    // Validation de la date de naissance - ÂGE MINIMUM 18 ANS
    const birthDate = new Date(formData.date_naissance);
    const currentDate = new Date();

    if (isNaN(birthDate.getTime())) {
        alert('Date de naissance invalide');
        return false;
    }

    // Vérifier que la date n'est pas dans le futur
    if (birthDate > currentDate) {
        alert('Date de naissance ne peut pas être dans le futur');
        return false;
    }

    // Vérifier que la personne a au moins 18 ans
    const minAgeDate = new Date();
    minAgeDate.setFullYear(currentDate.getFullYear() - 18);

    if (birthDate > minAgeDate) {
        alert('Doit avoir au moins 18 ans');
        return false;
    }

    // Validation des tableaux (doivent être des tableaux non vides)
    const arrayFields = ['langues', 'competences'];
    for (const field of arrayFields) {
        if (formData[field].length == 0) {
        alert(`Tableau vide ou invalide: ${field} ${formData[field].length}`);
        return false;
        }
    }

    
    // Vérification de la longueur des champs texte
    // if (formData.nom.length > 50 || formData.prenom.length > 50) {
    //     alert('Nom ou prénom trop long');
    //     return false;
    // }

    if (formData.description.length > 255) {
        alert('Description trop longue');
        return false;
    }

    // Si toutes les validations passent
    return true;
    };

    const sendData =()=>{
        console.log(formData)

        if (validateFormData(formData)){
            setFormData({
                ...formData,
                metiers: [formData.metiers.map(item => ({
                    metier : item.selectValue,
                    experience : item.inputValue
                }))]
            })
        }
    }







    /// chargement de donnee
    useEffect(()=>{
        if (!chargedLangue) {   
            /// charger les donnees du formulaire avec fetch
            const resultFetch = [
                {id: 0 , libelle: "Francais"},
                {id: 1, libelle: "English"},
                {id: 2, libelle: "Espagnol"},
                {id: 3, libelle: "Autre"},
            ];
            
            setLangues(resultFetch.map(item => ({
                value : item.id ,
                label : item.libelle,
                checked : false
            })));
            setChargedLangue(true);
        }
        
        if (!chargedCompetences) {
            
            const resultFetch = [

                {id: 0 , libelle: "A l'ecoute"},
                {id: 1, libelle: "Tres Actif"},
                {id: 2, libelle: "Team leader"},
                {id: 3, libelle: "Autre"},
            ]

            setCompetences(resultFetch.map(item => ({
                value : item.id ,
                label : item.libelle,
            })));
            setChargedCompetences(true)
        }

        if (!chargedMetiers){
            const resultFetch = [
                {id:0, libelle:"developpeur"},
                {id:1, libelle:"comptable"},
                {id:2, libelle:"gestionnaire"}
            ]

            setMetiers(resultFetch.map(item => ({
                value: item.id,
                label : item.libelle
            })));
            setChargedMetiers(true);

        }
        

    } , [langues,chargedLangue,competences,chargedCompetences,metiers,chargedMetiers,formData]);
    

    if (page==0) {

        return(
            <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8 ">
                <div className="max-w-2xl mx-auto">
                    <div className="bg-white py-8 px-6 shadow rounded-lg sm:px-10">
                        <div className="mb-8 text-center">
                            <h2 className="text-3xl font-extrabold text-gray-900">Formulaire pour votre recrutement</h2>
                            <p className="mt-2 text-gray-600">Remplissez le formulaire ci-dessous avec vos info perso</p>
                        </div>
                        <form className="space-y-6" >
                            <ImageProcessor
                                onImageChange={handelChangeImage}
                                initialImage={previewImage}
                            />
                            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                                <InputText
                                    label="Nom"
                                    name="nom"
                                    value={formData.nom}
                                    onChange={handleChange}
                                    required
                                    error={errors.nom}
                                />
                                <InputText
                                    label="Prenom"
                                    name="prenom"
                                    value={formData.prenom}
                                    onChange={handleChange}
                                    required
                                    error={errors.prenom}
                                />
                            </div>
                            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                                <InputDate
                                    label="date de Naissance"
                                    name="date_naissance"
                                    value={formData.date_naissance}
                                    onChange={handleChange}
                                    required
                                    error={errors.date_naissance}
                                />
                                <Select
                                    label="Genre"
                                    name="genre"
                                    value={formData.genre}
                                    onChange={handleChange}
                                    options={genres}
                                    required
                                    error={errors.genre}
                                />
                            </div>
                            <InputText
                                label="Email"
                                name="email"
                                type="email"
                                value={formData.email}
                                onChange={handleChange}
                                required
                                error={errors.email}
                            />
                            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">

                            <InputText
                                label="Téléphone (optionnel)"
                                name="telephone"
                                value={formData.telephone}
                                onChange={handleChange}
                                />
                            <InputText
                                label="Ville d'habitation"
                                name="ville"
                                value={formData.ville}
                                onChange={handleChange}
                                required
                                error={errors.ville}
                                />
                            </div>
                            <TextArea
                                label="Decriver vous en quelques mots"
                                name="description"
                                value={formData.description}
                                onChange={handleChange}
                                required
                                error={errors.description}
                            />

                            <div className="flex justify-between pt-4">
                                
                                <Button type="button" variant="secondary" onClick={handleReinitialize}>
                                    Reinititaliser
                                </Button>
                                
                                <Button type="button" onClick={handlePage}
                                >Suivant
                                </Button>
                            </div>
                        </form>
                    </div>

                </div>
            </div>
        );
    } else if (page==1){
        return(
            
            <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
                <div className="max-w-2xl mx-auto">
                    <div className="bg-white py-8 px-6 shadow rounded-lg sm:px-10">
                        <div className="mb-8 text-center">
                            <h2 className="text-3xl font-extrabold text-gray-900">Formulaire pour votre recrutement</h2>
                            <p className="mt-2 text-gray-600">Choissier vos skills </p>
                        </div>
                        <form className="space-y-6" >
                            <div className="mb-4">
                                <MultiCheckbox
                                    label="Vos langues"
                                    name="langue"
                                    value={formData.langues}
                                    onChange={handleChangeLangue}
                                    options={langues}
                                    required
                                    error={errors.competences}
                                />
                                <label htmlFor="" className="block text-sm font-medium text-gray-700 mb-1">
                                    Vos formation et filier <span className="text-red-500">*</span>
                                </label>
                                <MultiSelectFiliere
                                    value={formData.filiere}
                                    onChange={handleChangeFiliereDiplome}
                                />
                                
                                <MultiCheckbox
                                    label="Vos competences"
                                    name="competences"
                                    value={formData.competences}
                                    onChange={handleChangeCompetence}
                                    options={competences}
                                    required
                                    error = {errors.competences}
                                />
                                <MultiSelectInputNumber
                                    label="Vos experience professionnel"
                                    name="Exp"
                                    value={formData.metiers}
                                    onChange={handleChangeMetier}
                                    options={metiers}
                                    error={errors.metiers}
                                    selectLabel="Le metier"
                                    inputLabel="Annee d'experience"
                                />

                            </div>
                            <div className="flex justify-between pt-4">
                                
                                <Button type="button" variant="secondary" onClick={handlePage}>
                                    Precedent
                                </Button>
                                
                                <Button type="button" onClick={sendData}
                                >Valider
                                </Button>
                            </div>
                        </form>
                    </div>

                </div>
            </div>
        );
    }
};
export default FormCandidat;