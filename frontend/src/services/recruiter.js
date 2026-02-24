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

// Create Company
export const createCompany = (companyData) => {
    return axios.post(
        `${API_BASE_URL}/company`,
        companyData,
        getAuthHeader()
    );
};

// Get all companies(For logged-in recruiter)
export const getAllCompanies = () => {
    return axios.get(
        `${API_BASE_URL}/company`,
        getAuthHeader()
    );
};

// Get company by id
export const getCompanyById = (id) => {
    return axios.get(
        `${API_BASE_URL}/company/${id}`,
        getAuthHeader()
    );
};

// Delete company(Soft delete)
export const deleteCompany = (id) => {
    return axios.delete(
        `${API_BASE_URL}/company/${id}`,
        getAuthHeader()
    );
};

// To restore the company
export const restoreCompany = async (companyId) => {
    return await axios.put(`${API_BASE_URL}/company/${companyId}/restore`, {}, {
        headers: {
            Authorization: `Bearer ${sessionStorage.getItem("token")}`
        }
    });
};
