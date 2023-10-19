import {useNavigate, useParams} from "react-router-dom";
import {useState} from "react";
import ajax from "../../Service/FetchService";
import baseURL from "../BaseURL/BaseURL";


const ForgottenPasswordNewPassword = () => {

    const {verificationCode} = useParams();
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [dialogVisible, setDialogVisible] = useState(false);
    const [dialogErrorWrongFilling, setDialogErrorWrongFilling] = useState(false);
    const [errorResponse, setErrorResponse] = useState("");
    const [errorWrongFilling, setErrorWrongFilling] = useState("");
    const navigate = useNavigate();

    function setNewPassword() {
        const requestBody = {
            verificationCode: verificationCode,
            password: password,
            confirmPassword: confirmPassword
        }
        if (password.trim() === '' || confirmPassword.trim() === '') {
            setErrorWrongFilling("Please fill in both fields")
            setDialogErrorWrongFilling(true);
            return;
        }
        ajax(`${baseURL}api/users/register/forgotten-password/new-password`, "PATCH",
            null, requestBody)
            .then(response => {
                if (response === undefined) {
                    alert("Successful change your password");
                    navigate("/login")
                } else {
                    handleSubmit()
                    setErrorResponse(response.custom)
                    setDialogVisible(true)
                }
            })
    }

    const handleSubmit = () => {
        setPassword("");
        setConfirmPassword("");
    }

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    return (
        <main className="forgotten-password-send">
            <section className="user-forgotten-password-container">
                <h1>Generate new password</h1>
                <label
                    htmlFor="password"
                >
                    New password
                </label>
                <input
                    type={showPassword ? "text" : "password"}
                    id="password"
                    name="password"
                    placeholder="New Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <a className="forgotten-password-send-show-password"
                   onClick={togglePasswordVisibility}>
                    {showPassword ? 'Show password' : 'Hide password'}
                </a>
                <label
                    htmlFor="password"
                >
                    Confirm new password
                </label>
                <input
                    type={showPassword ? "text" : "password"}
                    id="confirmPassword"
                    name="confirmPassword"
                    placeholder="Confirm New Password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                />
                <a className="forgotten-password-send-show-password"
                   onClick={togglePasswordVisibility}>
                    {showPassword ? 'Show password' : 'Hide password'}
                </a>
                {dialogErrorWrongFilling &&
                    <h5 className="forgotten-password-invalid">{errorWrongFilling}
                    </h5>
                }
                {dialogVisible &&
                    <h5 className="forgotten-password-invalid">{errorResponse}
                    </h5>
                }
                <button
                    id="submit"
                    type="button"
                    onClick={() => setNewPassword()}
                >
                    Send
                </button>
            </section>
        </main>
    )

}

export default ForgottenPasswordNewPassword;