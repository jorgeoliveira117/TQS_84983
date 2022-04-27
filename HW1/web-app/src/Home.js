import { Button, Col, Row} from 'react-bootstrap';
import TopList from './TopList';
import useFetch from './useFetch';
import { useNavigate, Link } from "react-router-dom";


const Home = () => {
    
    const navigate = useNavigate();
    const URL = "http://localhost:8080/cases/continents";
    const { data: continents , loading, error } = useFetch(URL);
    let c = [];

    const goToWorldPage = () => {
        navigate("/world");
    }

    const goToCachePage = () => {
        navigate("/cache");
    }

    if(error)
        console.log(error);

    if(continents){

        for (var i = 0; i < continents.length; i++) {
            if(continents[i].country === "All")
            continents[i].country = "World";

            continents[i].country = continents[i].country.replace('-', ' ');
            continents[i].continent = continents[i].continent.replace('-', ' ');

            if(continents[i].newCases === -1)
                continents[i].newCases = "-";
            if(continents[i].activeCases === -1)
                continents[i].activeCases = "-";
            if(continents[i].criticalCases === -1)
                continents[i].criticalCases = "-";
            if(continents[i].newDeaths === -1)
                continents[i].newDeaths = "-";
        } 

        var ordering = {"All": 1, 
                        "Europe": 2, 
                        "North America": 3, 
                        "South America": 4, 
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
                <br/>
                <Button variant="light" size="lg" onClick={goToCachePage} style={{paddingTop:"10px"}}>
                    Cache Statistics
                </Button>
            </Col>
            <Col sm={8} style={{textAlign:"left"}}>
                <h2>
                    <b style={{color:"bisque"}}>Today's Statistics</b>
                </h2>
                <br/>
                    {loading && <h4><i style={{color:"orange"}}>Loading...</i></h4>}
                    {c && c.length > 0 &&
                        c.map((stats) => (
                            <Row>
                                <Col sm={2} style={{textAlign:"right"}}>
                                    <img className="continent" src={"/" + stats.country + ".png"} alt={stats.country + " - image from www.flaticon.com"}></img>
                                </Col>
                                <Col  sm={10}>
                                <Link to={stats.country === "World" ? "/world" : "/country/" + stats.country} style={{textDecoration:"none", color:"white"}}>
                                    <Row >
                                    <h3>
                                        {stats.country}
                                    </h3>
                                    </Row>
                                    <Row>
                                        <Col>
                                            <b>New Cases</b>
                                        </Col>
                                        <Col>
                                            <b>Active Cases</b>
                                        </Col>
                                        <Col>
                                            <b>Critical Cases</b>
                                        </Col>
                                        <Col>
                                            <b>New Deaths</b>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col>
                                            {stats.newCases}
                                        </Col>
                                        <Col>
                                            {stats.activeCases}
                                        </Col>
                                        <Col>
                                            {stats.criticalCases}
                                        </Col>
                                        <Col>
                                            {stats.newDeaths}
                                        </Col>
                                    </Row>
                                </Link>
                                <hr/>
                                </Col>
                            </Row>
                        ))
                    }
            </Col>
        </Row>
    );
}

export default Home;