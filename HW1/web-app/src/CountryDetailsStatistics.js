import useFetch from "./useFetch";
import {Col, Container, Row} from 'react-bootstrap';
import { useEffect } from "react";

const CountryDetailsStatistics = ({country, date, setContinent}) => {
    const BASE_URL = "http://localhost:8080/cases/" + country;
    console.log(BASE_URL);
    var URL = BASE_URL;
    
    if(date !== "")
        URL += "/" + date

    const { data , loading, error } = useFetch(URL);

    useEffect(() => {
        console.log(date);
        URL = BASE_URL;
        if(date !== "")
            URL += "/" + date
    }, [,date])

    if(data){
        data.country = data.country.replace('-', ' ');
        if(data.continent)
            data.continent = data.continent.replace('-', ' ');
        if(setContinent)
            setContinent(data.continent);
        for (const item in data) {
            if(data[item] === -1)
                data[item] = "-";
        }
    }

    if(error){
        console.log(error);
    }
    return ( 
        <div className="CountryDetailsStats">
            {loading && <h4><i style={{color:"orange"}}>Loading...</i></h4>}
            {!loading && error && data && !data.time.includes(date) &&
                <h4><i style={{color:"red"}}>Couldn't find statistics for country {country} {date && <i  style={{color:"red"}}>on {date.toString()}</i>}</i></h4>}
            {error && !data &&
                <h4><i style={{color:"red"}}>Couldn't find statistics for country {country} {date && <i  style={{color:"red"}}>on {date.toString()}</i>}</i></h4>}
            {data &&
            <Container>
                <Row>
                    <Col sm={"auto"}>
                        {data.country !== data.continent &&
                            <h3>Population: {data.population}</h3>
                        }
                    </Col>
                    <Col style={{textAlign:"right"}}>
                        <i>Last update: {data.time.replace('T', ' ').substring(0, 19)}</i>
                    </Col>
                </Row>
                <br/>
                <Row>
                    <Row>
                        <h4><i name="statsDay">Statistics for {data.day.replace('T', ' ').substring(0, 10)}</i></h4>
                    </Row>
                    <Col  sm={4} style={{fontSize:"larger"}}>
                        <h4><b style={{color:"bisque"}}>Cases</b></h4>
                        <Row>
                            <Col sm={6}> <h5>New</h5> </Col>
                            <Col sm={6}> <p>{data.newCases}</p> </Col>
                        </Row>
                        <Row>
                            <Col sm={6}> <h5>Active</h5> </Col>
                            <Col sm={6}> <p>{data.activeCases}</p> </Col>
                        </Row>
                        <Row>
                            <Col sm={6}> <h5>Critical</h5> </Col>
                            <Col sm={6}> <p>{data.criticalCases}</p> </Col>
                        </Row>
                        <Row>
                            <Col sm={6}> <h5>Recovered</h5> </Col>
                            <Col sm={6}> <p>{data.recovered}</p> </Col>
                        </Row>
                        <Row>
                            <Col sm={6}> <h5>Total</h5> </Col>
                            <Col sm={6}> <p>{data.totalCases}</p></Col>
                        </Row>
                        <Row>
                            <Col sm={6}> <h5>Per Million</h5> </Col>
                            <Col sm={6}> <p>{data.casesPerMillion}</p> </Col>
                        </Row>
                    </Col>
                    <Col  sm={4} style={{fontSize:"larger"}}>
                        <h4><b style={{color:"bisque"}}>Deaths</b></h4>
                        <Row>
                            <Col sm={6}> <h5>New</h5> </Col>
                            <Col sm={6}> <p>{data.newDeaths}</p> </Col>
                        </Row>
                        <Row>
                            <Col sm={6}> <h5>Total</h5> </Col>
                            <Col sm={6}> <p>{data.totalDeaths}</p></Col>
                        </Row>
                        <Row>
                            <Col sm={6}> <h5>Per Million</h5> </Col>
                            <Col sm={6}> <p>{data.deathsPerMillion}</p> </Col>
                        </Row>
                    </Col>
                    <Col  sm={4} style={{fontSize:"larger"}}>
                        <h4><b style={{color:"bisque"}}>Tests</b></h4>
                        <Row>
                            <Col sm={6}> <h5>Total</h5> </Col>
                            <Col sm={6}> <p>{data.totalTests}</p></Col>
                        </Row>
                        <Row>
                            <Col sm={6}> <h5>Per Million</h5> </Col>
                            <Col sm={6}> <p>{data.testsPerMillion}</p> </Col>
                        </Row>
                    </Col>
                </Row>
            </Container>
            }
        </div>
     );
}
 
export default CountryDetailsStatistics;