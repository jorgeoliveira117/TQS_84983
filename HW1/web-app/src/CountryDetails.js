import { useParams } from "react-router-dom";


const CountryDetails = () => {
    const { name } = useParams();
    
    return ( 
        <div className="CountryDetails">
            {name} Country Details
        </div>
     );
}
 
export default CountryDetails;