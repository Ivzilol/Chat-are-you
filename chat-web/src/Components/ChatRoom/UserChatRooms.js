import {useUser} from "../../UserProvider/UserProvider";
import {useEffect, useState} from "react";
import ajax from "../../Service/FetchService";
import baseURL from "../BaseURL/BaseURL";


const UserChatRooms = () => {
    const user = useUser();
    const [rooms, setRooms] = useState(null);

    useEffect(() => {
        ajax(`${baseURL}api/chat-rooms/user-rooms`, "GET", user.jwt)
            .then((response) => {
                setRooms(response)
            });
    }, [user.jwt])


    return (
        <main className="user-chat-rooms">
            {rooms ? (
                <section className="user-chat-rooms-container">
                    {rooms.map((room) => (
                        <div id={room.code}
                             key={room.id}
                             className="user-chat-rooms-container-items">
                            <button
                                id={room.code}
                                key={room.id}
                                type="button"
                                onClick={() => {
                                    window.location.href = `/chat-rooms/${room.code}`
                                }}
                            >
                                {room.name}
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

export default UserChatRooms;