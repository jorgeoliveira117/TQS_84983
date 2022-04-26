import { useParams } from "react-router-dom";
import {Col, Row} from 'react-bootstrap';
import { useState } from "react";
import DatePicker from 'react-date-picker'
import CountryDetailsStatistics from "./CountryDetailsStatistics";
import CountryHistory from "./CountryHistory";

const CountryDetails = () => {



    const { name } = useParams();
    const countryName = name.replace(' ', '-');
    const [startDate, setStartDate] = useState(new Date());
    const [day, setDay] = useState("");
    const [continent, setContinent] = useState(name);

    const HISTORY_URL = "http://localhost:8080/cases/evolution/" + countryName;

    const handleChange = (value) => {
        setStartDate(value);
        const offset = startDate.getTimezoneOffset()
        const newDate = new Date(value.getTime() - (offset*60*1000))
        setDay(newDate.toISOString().split('T')[0]);
    }

    return ( 
        <div className="CountryDetails" style={{textAlign:"left"}}>
            <Row>
                <Col sm={"auto"}>
                    <h1>
                        <b style={{color:"bisque"}}>
                        {name} 
                        {name !== continent && 
                            <img className="flag" src={"https://countryflagsapi.com/png/" + name} alt={name + " flag"}/>
                        }
                        </b>
                    </h1>
                    <hr className="custom-hr"/>
                </Col>
                <Col style={{textAlign:"right"}}>
                    {continent &&
                        <img className="continent" src={"/" + continent + ".png"} alt={continent + " - image from www.flaticon.com"}></img>
                    }
                </Col>
            </Row>
            <CountryDetailsStatistics country={countryName} date={day} setContinent={setContinent}/>
            <Row>
                <Col style={{textAlign:"left"}}>
                    Change day: <DatePicker onChange={(value, event) => handleChange(value)} value={startDate} />
                </Col>
            </Row>
            <hr className="custom-hr"/>
            <CountryHistory country={countryName} URL={HISTORY_URL} title="Statistics for the last 7 days"/>
        </div>
     );
}
 
export default CountryDetails;