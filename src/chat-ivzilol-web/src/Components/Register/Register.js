
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import baseURL from "../BaseURL/BaseURL";
import {useUser} from "../../UserProvider/UserProvider";


const Register = () => {

    const user = useUser();
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");


    useEffect(() => {
        if (user.jwt) navigate("/")
    }, [navigate, user.jwt])

    function registerUser() {
        const requestBody = {
            username: username,
            firstName: firstName,
            lastName: lastName,
            email: email,
            password: password,
            confirmPassword: confirmPassword
        }
        fetch(`${baseURL}api/users/register`, {
            headers: {
                "Content-Type": "application/json",
            },
            method: "POST",
            body: JSON.stringify(requestBody),
        })
            .then((response) => {
                if (response.status === 200)
                    return Promise.all([response.json(), response.headers]);
                else return Promise.reject("Invalid attempt");
            })
            .then(() => {
                user.setJwt(user.jwt)
            })
    }

    return (
        <main>
            <section className="register">
                <h1>Register</h1>
                <label>Username</label>
                <input
                    type="text"
                    id="username"
                    autoComplete="off"
                    name="username"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <label>Password</label>
                <input
                    type="password"
                    id="password"
                    autoComplete="off"
                    name="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <label>Confirm Password</label>
                <input
                    type="password"
                    id="confirmPassword"
                    autoComplete="off"
                    name="confirmPassword"
                    placeholder="Confirm Password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                />
                <label>First Name</label>
                <input
                    type="text"
                    id="firstName"
                    autoComplete="off"
                    name="firstName"
                    placeholder="First Name"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                />
                <label>Last Name</label>
                <input
                    type="text"
                    id="lastName"
                    autoComplete="off"
                    name="lastName"
                    placeholder="Last Name"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                />
                <label>Last Name</label>
                <input
                    type="text"
                    id="email"
                    autoComplete="off"
                    name="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <button
                    id="submit"
                    type="button"
                    onClick={() => registerUser()}
                >Register</button>
            </section>
        </main>
    )
}

export default Register;