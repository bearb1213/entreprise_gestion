import {useState , useEffect, use} from "react";
import InputText from "../../form/InputText";
import InputDate from "../../form/InputDate";
import RadioGroup from "../../form/RadioGroup";
import Select from "../../form/Select";
import TextArea from "../../form/TextArea";
import Button from "../../form/Button";


const FormCandidat = () =>{
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

    });

    const genres = [
        {value:0 , label:"Homme"} , 
        {value:1 , label:"Femme"}
    ] ;



    
    const [errors,setErrors] = useState({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    

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
        } else if (page>=0){
            setPage(page+1);
        }
    }

    const handleReinitialize = (e) => {
        if(page==0){
            setFormData({
                ...formData,
                nom:'',
                prenom:'',
                date_naissance:'',
                genre:'',
                email: '',
                telephone:'',
                ville:'',
                description:'',
            });
        }
    }
    
    if (page==0) {

        return(
            <div className="min-h-screen bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
                <div className="max-w-2xl mx-auto">
                    <div className="bg-white py-8 px-6 shadow rounded-lg sm:px-10">
                        <div className="mb-8 text-center">
                            <h2 className="text-3xl font-extrabold text-gray-900">Formulaire pour votre recrutement</h2>
                            <p className="mt-2 text-gray-600">Remplissez le formulaire ci-dessous avec vos info perso</p>
                        </div>
                        <form className="space-y-6" >
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
                            
                        </form>
                    </div>

                </div>
            </div>
        );
    }
};
export default FormCandidat;