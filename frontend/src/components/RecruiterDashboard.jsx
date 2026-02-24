import React, { useState, useEffect } from "react";
import "../css/RecruiterDashboard.css"
import { getAllCompanies, createCompany, deleteCompany, restoreCompany } from "../services/recruiter";
import { updateProfile , changePassword } from "../services/user";

const RecruiterDashboard = () => {
    const user = JSON.parse(sessionStorage.getItem("user"));
    const [activeTab, setActiveTab] = useState("dashboard");
    const [companies, setCompanies] = useState([]);
    const [showCompanyForm, setShowCompanyForm] = useState(false);
    const [companyForm, setCompanyForm] = useState({
        companyName: "",
        description: "",
        website: "",
        location: ""
    });
    const [profileForm, setProfileForm] = useState({
        fullName: user?.fullName || "",
        email: user?.email || "",
        phoneNumber: user?.phoneNumber || ""
    });

    const [passwordForm, setPasswordForm] = useState({
        currentPassword: "",
        newPassword: ""
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
            setShowCompanyForm(false);
            fetchCompanies();

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

    const handleRestore = async (id) => {
        try {
            await restoreCompany(id);
            alert("Company restored successfully");
            fetchCompanies();   // reload list
        } catch (error) {
            console.error(error);
            alert("Failed to restore company");
        }
    };

    const handleProfileChange = (e) => {
        setProfileForm({
            ...profileForm,
            [e.target.name]: e.target.value
        });
    };

    const handleProfileSubmit = async (e) => {
        e.preventDefault();

        try {
            await updateProfile(user.id, profileForm);

            alert("Profile updated successfully");

            sessionStorage.setItem("user", JSON.stringify({
                ...user,
                ...profileForm
            }));

        } catch (error) {
            alert("Failed to update profile");
        }
    };

    const handlePasswordChange = (e) => {
        setPasswordForm({
            ...passwordForm,
            [e.target.name]: e.target.value
        });
    };

    const handlePasswordSubmit = async (e) => {
        e.preventDefault();
        console.log("Password form submitted");
        try {
            await changePassword(user.id, passwordForm);
            alert("Password changed successfully");

            setPasswordForm({
                currentPassword: "",
                newPassword: ""
            });

        } catch (error) {
            alert("Incorrect current password");
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
                        <button className="create-company-btn"
                            onClick={() => setShowCompanyForm(!showCompanyForm)}>
                            {showCompanyForm ? "Close Form" : "+ Create Company"}
                        </button>

                        {showCompanyForm && (
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
                        )}

                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {companies.map((company) => (
                                    <tr key={company.id}>
                                        <td>{company.id}</td>
                                        <td>{company.companyName}</td>
                                        <td>
                                            {company.deleted ? (
                                                <button
                                                    className="restore-btn"
                                                    onClick={() => handleRestore(company.id)}
                                                >
                                                    Restore
                                                </button>
                                            ) : (
                                                <button
                                                    className="delete-btn"
                                                    onClick={() => handleDelete(company.id)}
                                                >
                                                    Delete
                                                </button>
                                            )}
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
                        <h2>Post Job</h2>
                        <p>Select a company and create job vacancies here.</p>
                        <p><b>Coming Soon...</b></p>
                    </div>
                );

            case "jobs":
                return <h2>My Jobs (Coming Soon)</h2>;

            case "applications":
                return <h2>Applications (Coming Soon)</h2>;

            case "settings":
                return (
                    <div>
                        <h2>Settings</h2>

                        <h3>Profile Information</h3>
                        <form onSubmit={handleProfileSubmit} className="company-form">
                            <input
                                type="text"
                                name="fullName"
                                value={profileForm.fullName}
                                onChange={handleProfileChange}
                                placeholder="Full Name"
                                required
                            />

                            <input
                                type="email"
                                name="email"
                                value={profileForm.email}
                                onChange={handleProfileChange}
                                placeholder="Email"
                                required
                            />

                            <input
                                type="text"
                                name="phoneNumber"
                                value={profileForm.phoneNumber}
                                onChange={handleProfileChange}
                                placeholder="Phone Number"
                                required
                            />

                            <button type="submit">Update Profile</button>
                        </form>

                        <hr />

                        <h3>Change Password</h3>
                        <form onSubmit={handlePasswordSubmit} className="company-form">
                            <input
                                type="password"
                                name="currentPassword"
                                value={passwordForm.currentPassword}
                                onChange={handlePasswordChange}
                                placeholder="Current Password"
                                required
                            />

                            <input
                                type="password"
                                name="newPassword"
                                value={passwordForm.newPassword}
                                onChange={handlePasswordChange}
                                placeholder="New Password"
                                required
                            />

                            <button type="submit">Change Password</button>
                        </form>
                    </div>
                );

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
                    <span>
                        <div className="profile-name">
                            {user?.fullName}
                        </div>
                    </span>
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