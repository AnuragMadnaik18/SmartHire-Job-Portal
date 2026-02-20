import './App.css'
import { BrowserRouter as Router,Routes, Route,Navigate } from 'react-router-dom'
import Login from './components/Login'
import Register from './components/Register'
import Home from './components/Home';

function App() {
  return(
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to = "/login"/>} />
        <Route path="/register" element={<Register/>} />
        <Route path="/login" element={<Login/>} />
        <Route path="/home" element={<Home/>} />
      </Routes>
    </Router>
  );
}

export default App 
