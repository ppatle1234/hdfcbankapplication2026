import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";

export const ShowAllData = () => {

  const [customers, setCustomers] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    if (!localStorage.getItem("token")) {
      navigate("/signin");
    }
    loadEmployees();
  }, [navigate]);

  const loadEmployees = async () => {
    const result = await api.get("/customers/findall");
    setCustomers(result.data);
  };

  // 🔹 Sign Out Function
  const handleSignOut = () => {
    if (window.confirm("Are you sure you want to sign out?")) {
      localStorage.removeItem("token");   // remove JWT
      navigate("/");                // redirect to signin
    }
  };


  return (
    <div className="container-fluid mt-3">

      <div className="card shadow">

      <div className="card-header text-white d-flex justify-content-between align-items-center" style={{ backgroundColor: "#d63384" }}>
          <h5>Customer Management Dashboard</h5>
      
          {/* Sign Out Button */}
          <button
            className="btn btn-light btn-sm fw-bold"
            onClick={handleSignOut}
          >
            Sign Out
          </button>
      </div>
        

        <div className="card-body p-2">

          <div className="table-responsive">

            <table className="table table-bordered table-hover table-sm text-center">

              <thead className="text-white" style={{background:"#343a40", fontSize:"13px"}}>
                <tr>
                  <th style={{width:"40px"}}>ID</th>
                  <th style={{width:"120px"}}>Name</th>
                  <th style={{width:"120px"}}>Account</th>
                  <th style={{width:"140px"}}>Address</th>
                  <th style={{width:"100px"}}>Contact</th>
                  <th style={{width:"90px"}}>Balance</th>
                  <th style={{width:"70px"}}>Gender</th>
                  <th style={{width:"110px"}}>DOB</th>
                  <th style={{width:"100px"}}>PAN</th>
                  <th style={{width:"120px"}}>UID</th>
                  <th style={{width:"160px"}}>Email</th>
                  <th style={{width:"60px"}}>Pwd</th>
                </tr>
              </thead>

              <tbody style={{fontSize:"12px"}}>

                {customers.map((cust) => (

                  <tr key={cust.custId}>

                    <td className="fw-bold">{cust.custId}</td>

                    <td className="text-primary fw-semibold">
                      {cust.custName}
                    </td>

                    <td>{cust.custAccountNumber}</td>

                    <td style={{whiteSpace:"nowrap"}}>
                      {cust.custAddress}
                    </td>

                    <td>{cust.custContactNumber}</td>

                    <td className="text-success fw-bold">
                      ₹ {cust.custAccountBalance}
                    </td>

                    <td>
                      <span className="badge bg-info">
                        {cust.custGender}
                      </span>
                    </td>

                    <td>{cust.custDOB}</td>

                    <td className="text-danger">
                      {cust.custPanCard}
                    </td>

                    <td>{cust.custUID}</td>

                    <td className="text-dark">
                      {cust.custEmailId}
                    </td>

                    <td style={{width:"40px"}}>
                      {"*".repeat(cust.custPassword.length)}
                    </td>

                  </tr>

                ))}

              </tbody>

            </table>

          </div>

        </div>

      </div>

    </div>
  );
};