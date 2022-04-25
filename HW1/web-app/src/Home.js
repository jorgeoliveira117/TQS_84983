import { Button, Container, Col, Row} from 'react-bootstrap';
import TopList from './TopList';
import useFetch from './useFetch';
import { useNavigate } from "react-router-dom";


const Home = () => {
    
    const navigate = useNavigate();
    const URL = "http://localhost:8080/cases/continents";
    const { data: continents , loading, error } = useFetch(URL);
    let c = [];

    const goToWorldPage = () => {
        navigate("/world");
    }

    if(error)
        console.log(error);

    if(continents){
        var ordering = {"All": 1, 
                        "Europe": 2, 
                        "North-America": 3, 
                        "South-America": 4, 
                        "Asia": 5, 
                        "Africa": 6, 
                        "Oceania": 7};
        c = [... continents]
        c.sort( function(a, b) {
            return(ordering[a.continent] - ordering[b.continent])
        });
    }

    return ( 
        <Row>
            
            <Col sm={4}>
                <Row>
                    <TopList />
                </Row>
                <br/>
                <Row>
                    <Button variant="info" size="lg" onClick={goToWorldPage}>
                        World's Covid Evolution
                    </Button>
                </Row>
            </Col>
            <Col sm={6} style={{textAlign:"left"}}>
                <h2>
                    <b>Today's Statistics</b>
                </h2>
                <br/>
                    {loading && <p>Loading...</p>}
                    {c && c.length > 0 &&
                        c.map((stats) => (
                            <Row >
                                <h3>{stats.country}</h3>
                                <Col>
                                <b>New Cases</b> {stats.newCases}
                                </Col>
                            </Row>
                        ))
                    }
            </Col>
        </Row>
    );
}

export default Home;