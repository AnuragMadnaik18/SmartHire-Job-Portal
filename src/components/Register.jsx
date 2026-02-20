import React, { useState } from 'react'
import "../css/UserRegister.css"
import { registerUser } from '../services/user';
import { Link,useNavigate } from "react-router-dom";

const Register = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        fullName: "",
        email: "",
        password: "",
        phoneNumber:"",
        role: "JOBSEEKER"
    });

    const [message, setMessage] = useState("");

    const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await registerUser(formData);
            setMessage("Registration Successful");

            setTimeout(()=>{
                navigate("/login");
            },1000);
        } catch (error) {
            setMessage("Registration failed.");
            console.log(error);
        }
    };

    return (
        <div className="register-container">
            <h2>Register</h2>
            <form onSubmit={handleSubmit} >
                <input type="text" name="fullName" placeholder='Full Name' onChange={handleChange} required />
                <input type="email" name="email" placeholder='Email' onChange={handleChange} required />
                <input type="password" name="password" placeholder='Password' onChange={handleChange} required />
                <input type="text" name="phoneNumber" placeholder="Phone Number" onChange={handleChange} required />
                
                <select name="role" value= {formData.role} onChange={handleChange}>
                    <option value="JOBSEEKER">Job Seeker</option>
                    <option value="RECRUITER">Recruiter</option>
                    <option value="ADMIN">Admin</option>
                </select>

                <button type='submit'>Register</button>
            </form>
            {message && <p>{message}</p>}

            <p style={{marginTop:"15px"}}>
                Already registered? <Link to="/login">Login</Link>
            </p>
        </div>
    );
};

export default Register
