import React, { useEffect, useState, useRef } from "react";
import "../css/JobSeekerDashboard.css";
import { applyToJob, getMyApplications } from "../services/application";

const JobSeekerDashboard = () => {

    const user = JSON.parse(sessionStorage.getItem("user"));

    const [activeTab, setActiveTab] = useState("dashboard");
    const [applications, setApplications] = useState([]);
    const [jobId, setJobId] = useState("");
    const [message, setMessage] = useState("");
    const [showProfile, setShowProfile] = useState(false);
    const profileRef = useRef();

    // Fetch Applications
    const fetchApplications = async () => {
        try {
            const res = await getMyApplications();
            setApplications(res.data);
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        if (activeTab === "applications" || activeTab === "dashboard") {
            fetchApplications();
        }
    }, [activeTab]);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (profileRef.current && !profileRef.current.contains(event.target)) {
                setShowProfile(false);
            }
        };

        document.addEventListener("mousedown", handleClickOutside);

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    // Apply to job
    const handleApply = async () => {
        if (!jobId) {
            setMessage("Enter Job ID");
            return;
        }

        try {
            await applyToJob(jobId);
            setMessage("Applied Successfully!");
            setJobId("");
            fetchApplications();
        } catch (err) {
            console.error(err);
            setMessage("Failed to Apply");
        }
    };

    // Render Content
    const renderContent = () => {
        switch (activeTab) {

            case "dashboard":
                return (
                    <div>
                        <h2>Dashboard Overview</h2>

                        <div className="card-container">
                            <div className="card">
                                Total Applications: {applications.length}
                            </div>
                        </div>
                    </div>
                );

            case "apply":
                return (
                    <div>
                        <h2>Apply for Job</h2>

                        <div className="apply-box">
                            <input
                                type="text"
                                placeholder="Enter Job ID"
                                value={jobId}
                                onChange={(e) => setJobId(e.target.value)}
                            />
                            <button onClick={handleApply}>Apply</button>
                            <p>{message}</p>
                        </div>
                    </div>
                );

            case "applications":
                return (
                    <div>
                        <h2>My Applications</h2>

                        {applications.length === 0 ? (
                            <p>No applications found</p>
                        ) : (
                            <table>
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Company</th>
                                        <th>Role</th>
                                        <th>Status</th>
                                        <th>Date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {applications.map((app) => (
                                        <tr key={app.id}>
                                            <td>{app.id}</td>
                                            <td>{app.companyName}</td>   {/* ✅ Company Name */}
                                            <td>{app.jobTitle}</td>      {/* ✅ Job Role */}
                                            <td>{app.applicationStatus}</td>

                                            {/* ✅ Show only Date */}
                                            <td>
                                                {new Date(app.appliedDate).toLocaleDateString()}
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        )}
                    </div>
                );

            case "profile":
                return (
                    <div>
                        <h2>My Profile</h2>
                        <p><b>Name:</b> {user?.fullName}</p>
                        <p><b>Email:</b> {user?.email}</p>
                        <p><b>Phone:</b> {user?.phoneNumber}</p>
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

                <div className="navbar-right" ref={profileRef}>
                    <div
                        className="profile-name"
                        onClick={() => setShowProfile(!showProfile)}
                    >
                        {user?.fullName}
                    </div>

                    {showProfile && (
                        <div className="profile-dropdown">
                            <p><b>Name:</b> {user?.fullName}</p>
                            <p><b>Email:</b> {user?.email}</p>
                            <p><b>Phone:</b> {user?.phoneNumber}</p>

                            <button
                                onClick={() => {
                                    sessionStorage.clear();
                                    window.location.href = "/";
                                }}
                            >
                                Logout
                            </button>
                        </div>
                    )}
                </div>
            </div>

            <div className="main-layout">

                {/* Sidebar */}
                <div className="sidebar">
                    <ul>
                        <li onClick={() => setActiveTab("dashboard")}>Dashboard</li>
                        <li onClick={() => setActiveTab("apply")}>Apply Job</li>
                        <li onClick={() => setActiveTab("applications")}>My Applications</li>
                    </ul>
                </div>

                {/* Content */}
                <div className="content">
                    {renderContent()}
                </div>

            </div>
        </div>
    );
};

export default JobSeekerDashboard;