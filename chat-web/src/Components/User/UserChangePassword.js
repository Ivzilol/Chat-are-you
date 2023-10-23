import {useState} from "react";
import ajax from "../../Service/FetchService";
import baseURL from "../BaseURL/BaseURL";
import {useUser} from "../../UserProvider/UserProvider";


const UserChangePassword = () => {
    const user = useUser();
    const [oldPassword, setOldPassword] = useState("")
    const [newPassword, setNewPassword] = useState("")
    const [confirmNewPassword, setConfirmNewPassword] = useState("")
    const [showPassword, setShowPassword] = useState(false);

    function changePassword() {
        const requestBody = {
            oldPassword: oldPassword,
            newPassword: newPassword,
            confirmNewPassword: confirmNewPassword
        }
        ajax(`${baseURL}api/users/change-password`, "PATCH", user.jwt, requestBody)
            .then((response) => {
                if (response.custom !== undefined) {
                    return alert("You have successfully changed your password, please login with your new password"),
                        user.setJwt(null),
                        window.location.href = "/login"
                } else {
                    handleSubmit()
                    return alert("Password change failed")

                }
            })
    }

    const handleSubmit = () => {
        setOldPassword("");
        setNewPassword("");
        setConfirmNewPassword("");
    }

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    return (
        <main className="user-change-pass">
            <section className="user-change-pass-container">
                <h1 className="change-password">Change password</h1>
                <label
                    htmlFor="oldPassword"
                >
                    Old password
                </label>
                <input
                    type={showPassword ? "text" : "password"}
                    id="oldPassword"
                    name="oldPassword"
                    placeholder="Old Password"
                    value={oldPassword}
                    onChange={(e) => setOldPassword(e.target.value)}
                />
                <a className="forgotten-password-send-show-password"
                   onClick={togglePasswordVisibility}>
                    {showPassword ? 'Hide password' : 'Show password'}
                </a>
                <label
                    htmlFor="oldPassword"
                >
                    New password
                </label>
                <input
                    type={showPassword ? "text" : "password"}
                    id="newPassword"
                    name="newPassword"
                    placeholder="New Password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                />
                <a className="forgotten-password-send-show-password"
                   onClick={togglePasswordVisibility}>
                    {showPassword ? 'Hide password' : 'Show password'}
                </a>
                <label
                    htmlFor="oldPassword"
                >
                    Confirm new password
                </label>
                <input
                    type={showPassword ? "text" : "password"}
                    id="confirmNewPassword"
                    name="confirmNewPassword"
                    placeholder="Confirm New Password"
                    value={confirmNewPassword}
                    onChange={(e) => setConfirmNewPassword(e.target.value)}
                />
                <a className="forgotten-password-send-show-password"
                   onClick={togglePasswordVisibility}>
                    {showPassword ? 'Hide password' : 'Show password'}
                </a>
                <button
                    id="submit"
                    type="button"
                    onClick={() => changePassword()}
                >
                    Change password
                </button>
            </section>
        </main>
    )

}

export default UserChangePassword;