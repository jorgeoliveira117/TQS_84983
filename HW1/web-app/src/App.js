import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Navbar from './Navbar';
import {Container} from 'react-bootstrap';
import { BrowserRouter as Router, Route, Routes as Switch } from 'react-router-dom';
import Home from './Home';
import CountryDetails from './CountryDetails';
import World from './World';
import Cache from './Cache';

function App() {
  return (
    <Router>
      <div className="App">
        <Container>
          <Navbar/>
          <Switch>
            <Route exact path="/" element={<Home/>}/>
            <Route path="/world" element={<World/>}/>
            <Route path="/cache" element={<Cache/>}/>
            <Route path="/country/:name"  element={<CountryDetails/>}/>
            <Route path="*" element={<World/>}/>
          </Switch>
        </Container>
      </div>
    </Router>
    
  );
}

export default App;
