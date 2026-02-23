import './App.css'
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import Login from './components/Login'
import Register from './components/Register'
import Home from './components/Home';
import RecruiterDashboard from './components/RecruiterDashboard';


// Function to get Logged in user
const getUser = () => {
  const user = sessionStorage.getItem("user")
  return user ? JSON.parse(user) : null
};

// Protected route compponent
const ProtectedRoute = ({ children, allowedRole }) => {
  const user = getUser();

  if (!user) {
    return <Navigate to="/login" />
  }
  if (allowedRole && user.role !== allowedRole) {
    return <Navigate to="/login" />
  }
  return children;
}


function App() {
  return (
    <Router>
      <Routes>
        {/* Default */}
        <Route path="/" element={<Navigate to="/login" />} />

        {/* Public Routes */}
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />

        {/* Job Seeker Home */}
        <Route
          path="/home"
          element={
            <ProtectedRoute allowedRole="JOBSEEKER">
              <Home />
            </ProtectedRoute>
          }
        />

        {/* Recruiter Dashboard */}
        <Route
          path="/recruiter"
          element={
            <ProtectedRoute allowedRole="RECRUITER">
              <RecruiterDashboard />
            </ProtectedRoute>
          }
        />

          {/* Optional Admin Route (Future) */}
        {/* 
        <Route
          path="/admin"
          element={
            <ProtectedRoute allowedRole="ADMIN">
              <AdminDashboard />
            </ProtectedRoute>
          }
        /> 
        */}

      </Routes>
    </Router>
  );
}

export default App 
