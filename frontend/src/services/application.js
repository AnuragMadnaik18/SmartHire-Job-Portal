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

// Get application by JobId(Recruiter only)
export const getApplicationsByJob = (jobId) => {
    return axios.get(`${API_BASE_URL}/applications/job/${jobId}`,
        getAuthHeader()
    )
}

// Update application Status (Recruiter only)
export const updateApplicationStatus = (applicationId,status) => {
    return axios.put(`${API_BASE_URL}/applications/status/${applicationId}?status=${status}`,
        {},
        getAuthHeader()
    )
}

// Get Logged-in user's applications
export const getMyApplications = () => {
    return axios.get(`${API_BASE_URL}/applications/my`,
        getAuthHeader()
    )
}

// Apply to Job
export const applyToJob = (jobId) => {
    return axios.post(`${API_BASE_URL}/applications/apply`,
        getAuthHeader()
    )
}