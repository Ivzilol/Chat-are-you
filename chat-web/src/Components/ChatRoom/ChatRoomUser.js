import {useUser} from "../../UserProvider/UserProvider";
import {useEffect, useState} from "react";
import ajax from "../../Service/FetchService";
import baseURL from "../BaseURL/BaseURL";
import GetAllUsers from "./GetAllUsers";


const ChatRoomUser = () => {

    const user = useUser();
    const roomCode = window.location.href.split("/chat-rooms/")[1];
    const [users, setUsers] = useState(null);


    useEffect(() => {
        ajax(`${baseURL}api/chat-rooms/${roomCode}`, "GET", user.jwt)
            .then((response) => {
                setUsers(response)
            })
    }, [roomCode, user.jwt])

    return (
        <main className="chat-room">
            {/*<GetAllUsers/>*/}
            {users ? (
                <section className="chat-room-container">
                    {users.map(user => (
                        <div id={user.id}
                             key={user.id}
                            className="chat-room-container-items">
                            <button>{user.username}</button>
                        </div>
                    ))}
                </section>
            ) : (
                <>No users in this room</>
            )}
        </main>
    )
}

export default ChatRoomUser;