import { useParams } from "react-router-dom";
import useFetch from "./useFetch";
import {Col, Container, Row, Button} from 'react-bootstrap';
import { useEffect, useState } from "react";
import DatePicker from 'react-date-picker'
import CountryDetailsStatistics from "./CountryDetailsStatistics";
import CountryHistory from "./CountryHistory";

const World = () => {

    const HISTORY_URL = "http://localhost:8080/cases/world/evolution";

    const [startDate, setStartDate] = useState(new Date());
    const [day, setDay] = useState("");
    const [viewHistory, setViewHistory] = useState(false);

    const handleChange = (value) => {
        setStartDate(value);
        const offset = startDate.getTimezoneOffset()
        const newDate = new Date(value.getTime() - (offset*60*1000))
        setDay(newDate.toISOString().split('T')[0]);
    }

    const renderHistory = () => {
        setViewHistory(true);
    }

    return ( 
        <div className="World" style={{textAlign:"left"}}>
            <Row>
                <Col sm={2}>
                    <h1>
                        <b style={{color:"bisque"}}>
                        World
                        </b>
                    </h1>
                    <hr className="custom-hr"/>
                </Col>
                <Col style={{textAlign:"right"}}>
                    <img className="continent" src={"/world.png"} alt={"Planet - image from www.flaticon.com"}></img>
                </Col>
            </Row>
            <CountryDetailsStatistics country="all" date={day}/>
            <Row>
                <Col style={{textAlign:"left"}}>
                    Change day: <DatePicker onChange={(value, event) => handleChange(value)} value={startDate} />
                </Col>
            </Row>
            <hr className="custom-hr"/>
            
            {!viewHistory &&
                <Button variant="info" onClick={renderHistory}>
                    See World's Covid evolution
                </Button>
            }
            {viewHistory &&
                <CountryHistory URL={HISTORY_URL} title="Evolution of Covid in the world"/>
            }
        </div>
     );
}
 
export default World;
