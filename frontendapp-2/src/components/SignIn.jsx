import React, { useState } from 'react'
import api from '../api/axiosConfig';
import { useNavigate } from 'react-router-dom';

export const SignIn = () => {

    const [login, setLogin] = useState({

        custEmailId:"",
        custPassword:""      
    });

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const res = await api.post("/v1/auth/signin", login);

            localStorage.setItem("token", res.data);

            alert("Login Successfull");

            navigate('/customers')
        } catch (error) {
            alert("Invalid Email or Password");

            console.error("Login error : ", error);
        }
    };

  return (
    <div>
        
        <h3 className="text-center">SignIn</h3>
        
        <form onSubmit={handleSubmit} className="card p-4 shadow">
           <input className="form-control mb-2"
             placeholder="Email"   
             type='email' 
             onChange={(e) => setLogin({...login, custEmailId : e.target.value})}
             />

            <input className="form-control mb-2"
            placeholder="Password"
            type="password"
            onChange={(e) => setLogin({...login, custPassword: e.target.value})} 
            />

            <button className="btn btn-danger mt-2 w-100">SignIn</button>
             <p className="mt-3">If you are new ? 
                  <span style={{cursor:"pointer", color:"blue"}} 
                        onClick={() => navigate("/signup")}>Sign Up</span>
             </p>
        </form>
    </div>
  )
}
