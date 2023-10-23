import './App.css';
import {Route, Routes} from "react-router-dom";
import Register from "./Components/Register/Register";
import ConfirmRegister from "./Components/Register/ConfirmRegister";
import Login from "./Components/Login/Login";
import ForgottenPasswordEmail from "./Components/Login/ForgottenPasswordEmail";
import ForgottenPasswordNewPassword from "./Components/Login/ForgottenPasswordNewPassword";
import {useUser} from "./UserProvider/UserProvider";
import {useEffect, useState} from "react";
import UserProfile from "./Components/User/UserProfile";
import AdminUsers from "./Components/User/AdminUsers";
import Homepage from "./Components/Homepage/Homepage";
import jwt_decode from 'jwt-decode'

function App() {
    const user = useUser();
    const [roles, setRoles] = useState(getRolesFromJWT());

    useEffect(() => {
        setRoles(getRolesFromJWT())
    }, [user.jwt])

    function getRolesFromJWT() {
        if (user.jwt) {
            const decodeJwt = jwt_decode(user.jwt)
            return decodeJwt.authorities;
        }
        return [];
    }

    return (
        <Routes>
            <Route path="/users"
                   element={
                       roles.find((role) => role === 'admin')
                           ?
                           <AdminUsers/>
                           :
                           <UserProfile/>
                   }
            />
            <Route path="/" element={<Homepage/>}/>
            <Route path="/register" element={<Register/>}/>
            <Route path="/register/verify/:verificationCode" element={<ConfirmRegister/>}/>
            <Route path="/login" element={<Login/>}></Route>
            <Route path="/forgotten-password" element={<ForgottenPasswordEmail/>}/>
            <Route path="/forgotten-password/:verificationCode" element={<ForgottenPasswordNewPassword/>}/>
        </Routes>
    );
}

export default App;
