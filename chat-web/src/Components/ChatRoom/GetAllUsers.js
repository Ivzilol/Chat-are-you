import {useUser} from "../../UserProvider/UserProvider";
import ajax from "../../Service/FetchService";
import baseURL from "../BaseURL/BaseURL";
import {useEffect, useState} from "react";


const GetAllUsers = (props) => {

    const user = useUser();
    const [usersOnline, setUsersOnline] = useState(null)

    useEffect(() => {
        ajax(`${baseURL}api/chat-rooms/users`, "GET", user.jwt)
            .then((response) => {
                setUsersOnline(response);
            })
    }, [user.jwt])



    return (
        <main className="all-users-chat">
            {usersOnline ? (
                <section className="all-users-chat-container">
                    {usersOnline.map((user) => (
                        <div className="all-users-chat-container-items"
                             key={user.id}
                             id={user.id}
                        >
                            <button
                                id={user.id}
                                key={user.id}
                                type="button"
                            >
                                {user.username}
                            </button>
                        </div>
                    ))}
                </section>
            ) : (
                <></>
            )}
        </main>
    )
}

export default GetAllUsers;