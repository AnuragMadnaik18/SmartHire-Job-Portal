import axios from "axios";
import API_BASE_URL from "./config";

// Get JWT from sessionStorage
const getAuthHeader = () => {
    const token = sessionStorage.getItem("token");
    return {
        headers : {
            Authorization: `Bearer ${token}`,
        },
    }
}

//Register API
export const registerUser = async(userData) => {
    return await axios.post(`${API_BASE_URL}/users/register`,userData);
}

//Login API
export const loginUser = async(loginData) => {
    return await axios.post(`${API_BASE_URL}/users/login`,loginData);
}

// To update profile
export const updateProfile = (id,data) => {
    return axios.put(`${API_BASE_URL}/users/update-profile/${id}`,data,
        getAuthHeader()
    );
}

// to change password
export const changePassword = (id,data) => {
    return axios.put(`${API_BASE_URL}/users/change-password/${id}`,data,
        getAuthHeader()
    );
}