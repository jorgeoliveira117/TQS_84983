import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import TopList from './TopList';
import Navbar from './Navbar';
import {Container, Col, Row} from 'react-bootstrap';
import { BrowserRouter as Router, Route, Routes as Switch } from 'react-router-dom';
import Home from './Home';
import CountryDetails from './CountryDetails';
import World from './World';

function App() {
  return (
    <Router>
      <div className="App">
        <Container>
          <Navbar/>
          <Switch>
            <Route exact path="/" element={<Home/>}/>
            <Route path="/world" element={<World/>}/>
            <Route path="/country/:name"  element={<CountryDetails/>}/>
            <Route path="*" element={<World/>}/>
          </Switch>
        </Container>
      </div>
    </Router>
    
  );
}

export default App;
