import { Link as RouterLink} from 'react-router-dom';
import { useNavigate } from "react-router-dom";
import { Col, Row, Form, Button } from 'react-bootstrap';
import useFetch from "./useFetch";



const Navbar = () => {

    
    const URL = "http://localhost:8080/countries";
    const { data: countries , loading, error } = useFetch(URL);
    const navigate = useNavigate();


    if(error)
        console.log(error);

    const handleChange = (value) => {
        if(value === "default")
            return;
        navigate("/country/" + value);
    }

    return ( 
        <Row>
            <Col style={{textAlign:"left"}}>
                <RouterLink as to="/" style={{textDecoration:"none"}}>
                    <h2 style={{color:"bisque"}}>
                        Covid Statistics App
                    </h2>
                </RouterLink>
            </Col>
            <Col>
            </Col>
            <Col>
                <Row>
                    <Form>
                        <Form.Group>
                            <Form.Select onChange={(e) => handleChange(e.target.value)}>
                                <option value="default">Select a country</option>
                                {loading && <option disabled>Getting countries...</option>}
                                {countries &&
                                    countries.map((country) => (
                                        <option key={country} value={country}>{country}</option>
                                    ))
                                }
                            </Form.Select>
                        </Form.Group>
                    </Form>
                </Row>
            </Col>

            <hr style={{color:"bisque"}}/>
        </Row>
     );
}
 
export default Navbar;