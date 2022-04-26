import { useParams } from "react-router-dom";
import useFetch from "./useFetch";
import {Button, Col, Container, Row} from 'react-bootstrap';
import { useEffect, useState } from "react";
import { LineChart, Line, CartesianGrid, XAxis, YAxis, Tooltip } from 'recharts';

const CountryHistory = ({country, URL, title}) => {

    var daysRev = [];
    const { data: days , loading, error } = useFetch(URL);
    const [dataKey, setDataKey] = useState("Cases");


    if(days){
        for(var i = 0; i < days.length ; i++){
            if (days[i].newCases === -1)
                days[i].newCases = 0;
            if (days[i].newDeaths === -1)
                days[i].newDeaths = 0;
            if (days[i].criticalCases === -1)
                days[i].criticalCases = 0;
            daysRev.push({
                "Cases": days[i].newCases, 
                "Deaths": days[i].newDeaths, 
                "Critical Cases": days[i].criticalCases, 
                "Day": days[i].day.split("T")[0]
            });
        }
        if(Date.parse(daysRev[0]["Day"]) > Date.parse(daysRev[daysRev.length-1]["Day"]))
            daysRev.reverse();
    }

    return ( 
        <div className="CountryHistory">
            {loading && <h4><i style={{color:"orange"}}>Loading...</i></h4> }
            {error && <h4><i style={{color:"red"}}>Couldn't find history of statistics</i></h4>}

            {days &&
            <Container>
                <Row className="justify-content-md-center">
                    <Col md="auto">
                        <Row>
                            <Col sm={"auto"}>
                            <h4 style={{color:"bisque"}}>{title}</h4>
                            </Col>
                            <Col sm={"auto"}>
                                <Button variant="info" onClick={(e) => (setDataKey("Cases"))}>
                                Cases
                                </Button>
                            </Col>
                            <Col sm={"auto"}>
                                <Button variant="info" onClick={(e) => (setDataKey("Critical Cases"))}>
                                Critical Cases
                                </Button>
                            </Col>
                            <Col sm={"auto"}>
                                <Button variant="info" onClick={(e) => (setDataKey("Deaths"))}>
                                Deaths
                                </Button>
                            </Col>
                        </Row>
                        <h5>{dataKey}</h5>
                        <LineChart width={800} height={300} data={daysRev} margin={{ top: 5, right: 20, bottom: 5, left: 0 }}>
                            <Line type="monotone" dataKey={dataKey} stroke="#8884d8" />
                            <CartesianGrid stroke="#ccc"/>
                            <XAxis dataKey="Day" />
                            <YAxis />
                            <Tooltip />
                        </LineChart>
                    </Col>
                    <Col md={1}>
                        <Row>
                        
                        </Row>
                        
                    </Col>
                </Row>
            </Container>
            }

        </div>
     );
}
 
export default CountryHistory;