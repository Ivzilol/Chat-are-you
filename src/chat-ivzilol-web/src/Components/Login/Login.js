import {useUser} from "../../UserProvider/UserProvider";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import baseURL from "../BaseURL/BaseURL";


const Login = () => {

    const user = useUser();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    let navigate = useNavigate();

    useEffect(() => {
        if (user.jwt) navigate("/")
    }, [navigate, user]);

    function loginUser() {
        const requestBody = {
            username: username,
            password: password,
        };
        fetch(`${baseURL}api/auth/login`, {
            method: "post",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestBody)
        })
            .then((response) => {
                if (response.status === 200)
                    return Promise.all([response.json(), response.headers])
                else return Promise.reject("Invalid login attempt")
            })
            .then(([, headers]) => {
                user.setJwt(headers.get("Authorization"))
            }).catch(() => {
        });
    }

    return (
        <main className="login">
            <h1>Login</h1>
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
            <button
                id="submit"
                type="button"
                onClick={() => loginUser()}
            >Login
            </button>
        </main>
    )
}

export default Login;