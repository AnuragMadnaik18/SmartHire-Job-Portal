import React, { useState, useEffect } from "react";
import "../css/RecruiterDashboard.css"
import { getAllCompanies } from "../services/recruiter";
import { createCompany } from "../services/recruiter";
import { deleteCompany } from "../services/recruiter";

const RecruiterDashboard = () => {
    const [activeTab, setActiveTab] = useState("dashboard");
    const [companies, setCompanies] = useState([]);
    const [companyForm, setCompanyForm] = useState({
        companyName: "",
        description: "",
        website: "",
        location: ""
    });

    useEffect(() => {
        if (activeTab === "companies") {
            fetchCompanies();
        }
    }, [activeTab]);

    const fetchCompanies = async () => {
        try {
            const response = await getAllCompanies();
            setCompanies(response.data);
        } catch (error) {
            console.error("Error fetching companies:", error);
        }
    };

    const handleCompanyChange = (e) => {
        setCompanyForm({
            ...companyForm,
            [e.target.name]: e.target.value
        });
    };

    const handleCompanySubmit = async (e) => {
        e.preventDefault();

        try {
            const user = JSON.parse(sessionStorage.getItem("user"));

            const payload = {
                ...companyForm,
                recruiterId: user.id
            };

            await createCompany(payload);

            alert("Company Created Successfully!");

            setCompanyForm({
                companyName: "",
                description: "",
                website: "",
                location: ""
            });

        } catch (error) {
            console.error("Error creating company:", error);
        }
    };

    const handleDelete = async (id) => {
        const token = sessionStorage.getItem("token");

        if (window.confirm("Are you sure you want to delete this company?")) {
            try {
                await deleteCompany(id, token);

                // remove deleted company from UI without reload
                setCompanies(companies.filter(company => company.id !== id));

                alert("Company deleted successfully");
            } catch (error) {
                console.error(error);
                alert("Error deleting company");
            }
        }
    };

    const renderContent = () => {
        switch (activeTab) {
            case "dashboard":
                return (
                    <div>
                        <h2>Dashboard Overview</h2>
                        <div className="card-container">
                            <div className="card">Total Companies: {companies.length}</div>
                            <div className="card">Total Jobs: 0</div>
                            <div className="card">Applications: 0</div>
                        </div>
                    </div>
                );

            case "companies":
                return (
                    <div>
                        <h2>My Companies</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                </tr>
                            </thead>
                            <tbody>
                                {companies.map((company) => (
                                    <tr key={company.id}>
                                        <td>{company.id}</td>
                                        <td>{company.companyName}</td>
                                        <td>
                                            <button
                                                className="delete-btn"
                                                onClick={() => handleDelete(company.id)}
                                            >
                                                Delete
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                );

            case "postJob":
                return (
                    <div>
                        <h2>Create Company</h2>

                        <form onSubmit={handleCompanySubmit} className="company-form">
                            <input
                                type="text"
                                name="companyName"
                                placeholder="Company Name"
                                value={companyForm.companyName}
                                onChange={handleCompanyChange}
                                required
                            />

                            <input
                                type="text"
                                name="location"
                                placeholder="Location"
                                value={companyForm.location}
                                onChange={handleCompanyChange}
                                required
                            />

                            <input
                                type="text"
                                name="website"
                                placeholder="Website"
                                value={companyForm.website}
                                onChange={handleCompanyChange}
                            />

                            <textarea
                                name="description"
                                placeholder="Description"
                                value={companyForm.description}
                                onChange={handleCompanyChange}
                                required
                            />

                            <button type="submit">Create Company</button>
                        </form>
                    </div>
                );

            case "jobs":
                return <h2>My Jobs (Coming Soon)</h2>;

            case "applications":
                return <h2>Applications (Coming Soon)</h2>;

            case "settings":
                return <h2>Settings (Coming Soon)</h2>;

            default:
                return <h2>Dashboard</h2>;
        }
    };

    return (
        <div className="dashboard-container">
            {/* Top Navbar */}
            <div className="top-navbar">
                <div className="logo">SmartHire</div>
                <div className="navbar-right">
                    <span>🔔</span>
                    <span>👤</span>
                </div>
            </div>

            <div className="main-layout">
                {/* Sidebar */}
                <div className="sidebar">
                    <ul>
                        <li onClick={() => setActiveTab("dashboard")}>Dashboard</li>
                        <li onClick={() => setActiveTab("companies")}>My Companies</li>
                        <li onClick={() => setActiveTab("postJob")}>Post Job</li>
                        <li onClick={() => setActiveTab("jobs")}>My Jobs</li>
                        <li onClick={() => setActiveTab("applications")}>Applications</li>
                        <li onClick={() => setActiveTab("settings")}>Settings</li>
                        <li onClick={() => {
                            sessionStorage.clear();
                            window.location.href = "/";
                        }}>
                            Logout
                        </li>
                    </ul>
                </div>

                {/* Dynamic Content */}
                <div className="content">{renderContent()}</div>
            </div>
        </div>
    );
};

export default RecruiterDashboard;