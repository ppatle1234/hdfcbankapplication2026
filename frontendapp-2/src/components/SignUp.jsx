import React,{ useState } from "react";
import api from "../api/axiosConfig";
import { useNavigate } from "react-router-dom";

export const SignUp = () => {

  const [customer, setCustomers] = useState({
    custName: "",
    custAccountNumber: "",
    custAddress: "",
    custContactNumber: "",
    custAccountBalance: "",
    custGender: "",
    custDOB: "",
    custPanCard: "",
    custUID: "",
    custEmailId: "",
    custPassword: ""
  });

  const {custName, custAccountNumber, custAddress, custContactNumber,
        custAccountBalance, custGender, custDOB, custPanCard,
        custUID, custEmailId, custPassword} = customer;

  const onInputChange = (e) => {
    setCustomers({ ...customer, [e.target.name]: e.target.value });
  };

  const navigate = useNavigate();

  const onSubmit = async (e) => {
    e.preventDefault();

    try {
      await api.post("/v1/auth/signup", customer);
      alert("Signup Done Successfully");

      navigate("/");
      //setCustomers(customer);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="container mt-5">
      <div className="row justify-content-center">

        <div className="col-md-8">

          <div className="card shadow-lg border-0">

            <div className="card-header bg-success text-white text-center">
              <h3>Create Customer Account</h3>
            </div>
       
            <div className="card-body">

              <form onSubmit={onSubmit}>

                <div className="row">

                  <div className="col-md-6 mb-3">
                    <label className="form-label">Customer Name</label>
                    <input type="text" className="form-control"
                      name="custName" value={custName} onChange={onInputChange}/>
                  </div>

                  <div className="col-md-6 mb-3">
                    <label className="form-label">Account Number</label>
                    <input type="number" className="form-control"
                      name="custAccountNumber" value={custAccountNumber} onChange={onInputChange}/>
                  </div>

                  <div className="col-md-12 mb-3">
                    <label className="form-label">Address</label>
                    <input type="text" className="form-control"
                      name="custAddress" value={custAddress} onChange={onInputChange}/>
                  </div>

                  <div className="col-md-6 mb-3">
                    <label className="form-label">Contact Number</label>
                    <input type="number" className="form-control"
                      name="custContactNumber" value={custContactNumber} onChange={onInputChange}/>
                  </div>

                  <div className="col-md-6 mb-3">
                    <label className="form-label">Account Balance</label>
                    <input type="number" className="form-control"
                      name="custAccountBalance" value={custAccountBalance} onChange={onInputChange}/>
                  </div>

                  <div className="col-md-6 mb-3">
                    <label className="form-label">Gender</label>
                    <select className="form-control"
                      name="custGender" value={custGender} onChange={onInputChange}>
                      <option value="">Select</option>
                      <option value="Male">Male</option>
                      <option value="Female">Female</option>
                      <option value="Other">Other</option>
                    </select>
                  </div>

                  <div className="col-md-6 mb-3">
                    <label className="form-label">Date of Birth</label>
                    <input type="date" className="form-control"
                      name="custDOB" value={custDOB} onChange={onInputChange}/>
                  </div>

                  <div className="col-md-6 mb-3">
                    <label className="form-label">PAN Card</label>
                    <input type="text" className="form-control"
                      name="custPanCard" value={custPanCard} onChange={onInputChange}/>
                  </div>

                  <div className="col-md-6 mb-3">
                    <label className="form-label">Aadhar UID</label>
                    <input type="text" className="form-control"
                      name="custUID" value={custUID} onChange={onInputChange}/>
                  </div>

                  <div className="col-md-6 mb-3">
                    <label className="form-label">Email</label>
                    <input type="email" className="form-control"
                      name="custEmailId" value={custEmailId} onChange={onInputChange}/>
                  </div>

                  <div className="col-md-6 mb-3">
                    <label className="form-label">Password</label>
                    <input type="password" className="form-control"
                      name="custPassword" value={custPassword} onChange={onInputChange}/>
                  </div>

                </div>

                <div className="text-center mt-4">
                  <button className="btn btn-success px-5">
                    Sign Up
                  </button>

                <p className="mt-3">Already have an account? 
                  <span style={{cursor:"pointer", color:"blue"}} 
                        onClick={() => navigate("/")}>
                     Sign In</span>
                </p>
                </div>

              </form>

            </div>
          </div>

        </div>

      </div>
    </div>
  );
};