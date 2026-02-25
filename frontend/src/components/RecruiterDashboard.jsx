import React, { useState, useEffect } from "react";
import "../css/RecruiterDashboard.css"
import { getAllCompanies, createCompany, deleteCompany, restoreCompany } from "../services/recruiter";
import { postJob, getTotalJobs, getJobsByCompany, toggleJobStatus } from "../services/recruiter";
import { updateProfile, changePassword } from "../services/user";

const RecruiterDashboard = () => {
    const user = JSON.parse(sessionStorage.getItem("user"));

    const [activeTab, setActiveTab] = useState("dashboard");

    const [companies, setCompanies] = useState([]);
    const [showCompanyForm, setShowCompanyForm] = useState(false);
    const [selectedCompany, setSelectedCompany] = useState(null);
    
    const [totalJobs, setTotalJobs] = useState(0);
    const [jobs, setJobs] = useState([]);

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

    const [jobData, setJobData] = useState({
        title: "",
        description: "",
        salary: "",
        location: "",
        experience: "",
        jobType: "",
        companyId: ""
    });


    useEffect(() => {

        if (activeTab === "dashboard") {
            fetchTotalJobs();
            fetchCompanies();
        }

        if (activeTab === "companies" || activeTab == "postJob") {
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

    const handlePostChange = (e) => {
        setJobData({
            ...jobData,
            [e.target.name]: e.target.value
        });
    };

    const handlePostSubmit = async (e) => {
        e.preventDefault();
        try {
            await postJob(jobData);
            alert("Job Posted Successfully.");
            setJobData({
                title: "",
                description: "",
                salary: "",
                location: "",
                experience: "",
                jobType: "",
                companyId: ""
            });
        } catch (error) {
            console.log(error);
            alert("Error posting job!");
        }
    }

    const handleCompanyClick = async (company) => {
        try {
            setSelectedCompany(company);
            const response = await getJobsByCompany(company.id);
            setJobs(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    const fetchTotalJobs = async () => {
        try {
            const response = await getTotalJobs(); // we will create this service
            setTotalJobs(response.data);
        } catch (error) {
            console.error("Error fetching total jobs:", error);
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
                            <div className="card">Total Jobs: {totalJobs}</div>
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
                                        <td
                                            style={{ cursor: "pointer", color: "blue" }}
                                            onClick={() => handleCompanyClick(company)}
                                        >
                                            {company.companyName}
                                        </td>
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
                        {selectedCompany && (
                            <div style={{ marginTop: "30px" }}>
                                <h3>
                                    Jobs at {selectedCompany.companyName}
                                </h3>

                                {jobs.length === 0 ? (
                                    <p>No jobs posted for this company.</p>
                                ) : (
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Title</th>
                                                <th>Status</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {jobs.map(job => (
                                                <tr key={job.id}>
                                                    <td>{job.id}</td>
                                                    <td>{job.title}</td>
                                                    <td>{job.status}</td>
                                                    <td>
                                                        <button
                                                            onClick={() =>
                                                                toggleJobStatus(job.id)
                                                                    .then(() =>
                                                                        handleCompanyClick(selectedCompany)
                                                                    )
                                                            }
                                                        >
                                                            {job.status === "OPEN"
                                                                ? "Close Job"
                                                                : "Reopen Job"}
                                                        </button>
                                                    </td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                )}
                            </div>
                        )}
                    </div>
                );

            case "postJob":
                return (
                    <div className="job-form-container">
                        <h2>Post New Job</h2>

                        <form onSubmit={handlePostSubmit} className="job-form">

                            <div className="form-group">
                                <label>Job Title</label>
                                <input
                                    type="text"
                                    name="title"
                                    onChange={handlePostChange}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label>Job Description</label>
                                <textarea
                                    name="description"
                                    onChange={handlePostChange}
                                    required
                                />
                            </div>

                            <div className="form-row">
                                <div className="form-group">
                                    <label>Salary</label>
                                    <input
                                        type="number"
                                        name="salary"
                                        onChange={handlePostChange}
                                        required
                                    />
                                </div>

                                <div className="form-group">
                                    <label>Location</label>
                                    <input
                                        type="text"
                                        name="location"
                                        onChange={handlePostChange}
                                        required
                                    />
                                </div>
                            </div>

                            <div className="form-row">
                                <div className="form-group">
                                    <label>Experience</label>
                                    <select name="experience" onChange={handlePostChange} required>
                                        <option value="">Select Experience</option>
                                        <option value="FRESHER">FRESHER</option>
                                        <option value="ZERO_TO_TWO">0 - 2</option>
                                        <option value="TWO_TO_FOUR">2 - 4</option>
                                        <option value="TWO_TO_FOUR">2 - 4</option>
                                        <option value="FOUR_TO_SIX">4 - 6</option>
                                        <option value="SIX_TO_EIGHT">6 - 8</option>
                                        <option value="EIGHT_PLUS">8+</option>
                                    </select>
                                </div>

                                <div className="form-group">
                                    <label>Job Type</label>
                                    <select name="jobType" onChange={handlePostChange} required>
                                        <option value="">Select Job Type</option>
                                        <option value="FULLTIME">FULLTIME</option>
                                        <option value="PARTTIME">PARTTIME</option>
                                        <option value="INTERNSHIP">INTERNSHIP</option>
                                    </select>
                                </div>
                            </div>

                            <div className="form-group">
                                <label>Select Company</label>
                                <select
                                    name="companyId"
                                    value={jobData.companyId}
                                    onChange={handlePostChange}
                                    required
                                >
                                    <option value="">Select Company</option>

                                    {companies.map((company) => (
                                        <option key={company.id} value={company.id}>
                                            {company.companyName}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <button type="submit" className="submit-btn">
                                Post Job
                            </button>

                        </form>
                    </div>
                );

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