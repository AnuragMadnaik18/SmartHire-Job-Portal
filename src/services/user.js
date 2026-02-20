import axios from "axios";
import API_BASE_URL from "./config";

//Register API
export const registerUser = async(userData) => {
    return await axios.post(`${API_BASE_URL}/users/register`,userData);
}

//Login API
export const loginUser = async(loginData) => {
    return await axios.post(`${API_BASE_URL}/users/login`,loginData);
}


