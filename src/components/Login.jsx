import React, { useState } from 'react'
import "../css/UserLogin.css"
import { loginUser } from '../services/user';
import { useNavigate } from 'react-router-dom';
import { Link } from 'react-router-dom';

const Login = () => {
    const navigate = useNavigate();

    const [loginData,setLoginData] = useState({
        email:"",
        password:""
    });

    const [message,setMessage] = useState("");

    const handleChange = (e) => {
        setLoginData({
            ...loginData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try{
            const response = await loginUser(loginData);

            // Save token + user data in LocalStorage
            localStorage.setItem("token",response.data.token);
            localStorage.setItem("user",JSON.stringify(response.data));

            setMessage("Login Successul");

            setTimeout(()=>{
                navigate("/home");
            },1000)
        }catch(error){
            setMessage("Login Failed.");
            console.log(error);
        }
    };

    return (
        <div className="login-container">
            <h2>Login</h2>

            <form onSubmit={handleSubmit}>
                <input type="email"  name="email" placeholder='Email' onChange={handleChange} required/>
                <input type="password"  name="password" placeholder='Password' onChange={handleChange} required/>
            
                <button type='submit'>Login</button>
            </form>
            {message && <p>{message}</p>}

            <p style={{marginTop:"15px"}}>
                New User? <Link to="/register">Register</Link>
            </p>
        </div>
    );
};
export default Login
