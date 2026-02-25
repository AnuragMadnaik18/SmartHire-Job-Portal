import axios from "axios";
import API_BASE_URL from "./config";

// Get JWT from sessionStorage
const getAuthHeader = () => {
    const token = sessionStorage.getItem("token");
    return {
        headers: {
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

// To Post the job
export const postJob = (jobData) => {
    return axios.post(`${API_BASE_URL}/jobs/create`,
        jobData,
        getAuthHeader()
    )
}


// To count total jobs posted by recruiter
export const getTotalJobs = () => {
    return axios.get(`${API_BASE_URL}/jobs/count`,
        getAuthHeader()
    )
}

// To get all the jobs posted for a company
export const getJobsByCompany = (companyId) => {
    return axios.get(
        `${API_BASE_URL}/jobs/company/${companyId}`,
        getAuthHeader()
    );
};

// To toggle job status
export const toggleJobStatus = (jobId) => {
    return axios.put(`${API_BASE_URL}/jobs/toggleStatus/${jobId}`,
        {},
        getAuthHeader(),
    )
}

