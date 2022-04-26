import { Link as RouterLink} from 'react-router-dom';
import { useNavigate } from "react-router-dom";
import { Col, Row, Form, Button, Container } from 'react-bootstrap';
import useFetch from "./useFetch";



const Cache = () => {

    
    const URL = "http://localhost:8080/cache";
    const { data , loading, error } = useFetch(URL);

    
    if(error)
        console.log(error);

    return ( 
        <Container  style={{textAlign:"left"}}>
            <Row>
                <h1 style={{color:"bisque"}}>
                    Cache Statistics
                </h1>
            </Row>
            {loading && <h4><i style={{color:"orange"}}>Loading...</i></h4>}
            {error && <h4><i style={{color:"red"}}>Couldn't get Cache Statistics from server...</i></h4>}
            {data &&
                data.map((cache) => (
                <Row>
                    <Col>
                    <h4><b>Name</b></h4>
                    <h5>{cache.name}</h5>
                    </Col>
                    <Col>
                    <h4><b>Time to Live</b></h4>
                    <h5>{cache.timeToLive / 1000} seconds</h5>
                    </Col>
                    <Col>
                    <h4><b>Total Keys</b></h4>
                    <h5>{cache.totalKeys}</h5>
                    </Col>
                    <Col>
                    <h4><b>Successful Hits</b></h4>
                    <h5>{cache.successfulHits}</h5>
                    </Col>
                    <Col>
                    <h4><b>Missed Hits</b></h4>
                    <h5>{cache.missedHits}</h5>
                    </Col>
                    <hr/>
                </Row>
                ))
            }
        </Container>
     );
}
 
export default Cache;