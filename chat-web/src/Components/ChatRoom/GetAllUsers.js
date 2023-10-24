import {useUser} from "../../UserProvider/UserProvider";
import ajax from "../../Service/FetchService";
import baseURL from "../BaseURL/BaseURL";
import {useEffect, useState} from "react";


const GetAllUsers = () => {

    const user = useUser();
    const [users, setUsers] = useState(null)

    useEffect(() => {
        ajax(`${baseURL}api/chat-rooms/users`, "GET", user.jwt)
            .then((response) => {
                setUsers(response);
            })
    }, [user.jwt])



    return (
        <main className="all-users-chat">
            {users ? (
                <section className="all-users-chat-container">
                    {users.map((user) => (
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