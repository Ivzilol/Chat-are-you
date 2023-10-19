import {useState} from "react";
import ajax from "../../Service/FetchService";
import baseURL from "../BaseURL/BaseURL";


const ForgottenPasswordEmail = () => {
    const [email, setEmail] = useState("");
    const [dialogVisible, setDialogVisible] = useState(false);
    const [emptyEmailDialog, setEmptyEmailDialog] = useState(false);
    const [error, setError] = useState("")
    const [emptyErrorEmail, setEmptyErrorEmail] = useState("");


    function sendEmail() {
        if (email.trim() === '') {
            setEmptyErrorEmail("Please put your email");
            setEmptyEmailDialog(true);
            return;
        }
        const requestBody = {
            email: email
        }

        ajax(`${baseURL}api/users/register/forgotten-password`, "POST", null, requestBody)
            .then((response) => {
                if (response === undefined) {
                    alert("Please check your Email");
                    handleSubmit()
                } else {
                    handleSubmit()
                    setError(response.custom)
                    setDialogVisible(true)
                }
            })
    }

    const handleSubmit = () => {
        setEmail("");
    }

    return (
        <main className="user-forgotten-password">
            <section className="user-forgotten-password-container">
                <h1>Password recovery</h1>
                <label htmlFor="email">
                    Please put your email
                </label>
                <input
                    type="text"
                    id="email"
                    name="email"
                    placeholder="Enter your Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                {dialogVisible &&
                    <h5 className="forgotten-password-invalid">{error}
                    </h5>
                }
                {emptyEmailDialog &&
                    <h5 className="forgotten-password-invalid">{emptyErrorEmail}
                    </h5>
                }
                <button
                    id="submit"
                    type="button"
                    onClick={() => sendEmail()}
                >
                    Send
                </button>
            </section>
        </main>
    )
}

export default ForgottenPasswordEmail;