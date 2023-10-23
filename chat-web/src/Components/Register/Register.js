import {useUser} from "../../UserProvider/UserProvider";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import baseURL from "../BaseURL/BaseURL";

const Register = () => {

    const user = useUser();
    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [avatar, setAvatar] = useState();


    useEffect(() => {
        if (user.jwt) navigate("/")
    }, [navigate, user.jwt])

    const avatarSubmit = (e) => {
        if (e.target.files[0] && e.target.files[0].name !== "") {
            setAvatar(e.target.files[0])
        }

    }

    function bodyUserRegister(avatar, username, firstName, lastName, email, password, confirmPassword) {
        const formData = new FormData();
        formData.append("avatar", avatar);
        const dto = {
            username: username,
            firstName: firstName,
            lastName: lastName,
            email: email,
            password: password,
            confirmPassword: confirmPassword
        }
        formData.append("dto",
            new Blob([JSON.stringify(dto)], {
                type: "application/json",
            })
        );
        return formData;
    }

    function registerUser() {
        const formData = bodyUserRegister(avatar, username, firstName, lastName, email, password, confirmPassword);
        fetch(`${baseURL}api/users/register`, {
            method: "POST",
            body: formData,
        })
            .then((response) => {
                if (response.status === 200)
                    return Promise.all([response.json(), response.headers]);
                else return Promise.reject("Invalid attempt");
            })
            .then(() => {
                user.setJwt(user.jwt)
                alert("Successful registration")
            })
            .catch(() => {
                alert("Unsuccessful registration")
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
                <label>Email</label>
                <input
                    type="text"
                    id="email"
                    autoComplete="off"
                    name="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <label>Avatar</label>
                <input
                    className="input-avatar"
                    type="file"
                    accept='image/png, image/jpeg'
                    size='sm'
                    id="avatar"
                    name="avatar"
                    placeholder="Avatar"
                    onChange={avatarSubmit}
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