import {useUser} from "../../UserProvider/UserProvider";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import ajax from "../../Service/FetchService";
import baseURL from "../BaseURL/BaseURL";

const UserEditProfile = () => {

    const user = useUser();
    const userId = window.location.href.split("/users/")[1];
    let navigate = useNavigate();
    const [avatar, setAvatar] = useState();

    const avatarSubmit = (e) => {
        if (e.target.files[0] && e.target.files[0].name !== "") {
            setAvatar(e.target.files[0])
        }
    }

    const [currentUser, setCurrentUser] = useState({
        username: "",
        firstName: "",
        lastName: "",
        email: "",
    });

    useEffect(() => {
        ajax(`${baseURL}api/users/${userId}`, "GET", user.jwt)
            .then(userResponse => {
                setCurrentUser(userResponse);
            });
        if (!user.jwt) navigate("/login");
    }, [navigate, user.jwt, userId]);

    function updateUser(prop, value) {
        const newUser = {...currentUser}
        newUser[prop] = value;
        setCurrentUser(newUser);
    }

    function bodyEditProfile(avatar, currentUser) {
        const formData = new FormData();
        formData.append("avatar", avatar)
        const dto = {
            username: currentUser.username,
            firstName: currentUser.firstName,
            lastName: currentUser.lastName,
            email: currentUser.email
        }
        formData.append("dto",
            new Blob([JSON.stringify(dto)], {
                type: "application/json",
            })
        );
        return formData;
    }

    function editProfile() {
        const formData = bodyEditProfile(avatar, currentUser);
        fetch(`${baseURL}api/users/edit/${userId}`, {
            method: "PATCH",
            body: formData
        })
            .then(userData => {
                console.log(userData);
                if (userData.status === 200) {
                    alert("Successful update your personal info, please login again in your profile")
                    user.setJwt(null);
                } else {
                    alert(userData.custom);
                    navigate("/users");
                }
            });
    }

    return (
        <main className="user-edit-profile">
            {currentUser ? (
                <section className="user-edit-profile-container">
                    {currentUser ? (
                        <div className="user-edit-profile-items">
                            <article className="user-edit-profile-item">
                                <h6>Username</h6>
                                <input
                                    onChange={(e) => updateUser("username", e.target.value)}
                                    value={currentUser.username}
                                    type="text"
                                    name="username"
                                />
                            </article>
                            <article className="user-edit-profile-item">
                                <h6>First Name</h6>
                                <input
                                    onChange={(e) => updateUser("firstName", e.target.value)}
                                    value={currentUser.firstName}
                                    type="text"
                                    name="firstName"
                                />
                            </article>
                            <article className="user-edit-profile-item">
                                <h6>Last Name</h6>
                                <input
                                    onChange={(e) => updateUser("lastName", e.target.value)}
                                    value={currentUser.lastName}
                                    type="text"
                                    name="lastName"
                                />
                            </article>
                            <article className="user-edit-profile-item">
                                <h6>Email</h6>
                                <input
                                    onChange={(e) => updateUser("email", e.target.value)}
                                    value={currentUser.email}
                                    type="text"
                                    name="email"
                                />
                            </article>
                            <article className="user-edit-profile-item">
                                <h6>Avatar</h6>
                                <img className="user-edit-profile-item-avatar"
                                     src={currentUser.avatar} alt="new"
                                />
                            </article>
                            <label>New Avatar</label>
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
                            <section className="user-edit-profile-button">
                                <button
                                    type="submit"
                                    onClick={() => editProfile()}
                                >
                                    Edit Profile
                                </button>
                            </section>
                        </div>
                    ) : (
                        <></>
                    )}
                </section>
            ) : (
                <></>
            )}
        </main>
    )
}

export default UserEditProfile;