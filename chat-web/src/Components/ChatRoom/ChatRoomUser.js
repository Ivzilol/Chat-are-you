import {useUser} from "../../UserProvider/UserProvider";
import {useEffect, useState} from "react";
import ajax from "../../Service/FetchService";
import baseURL from "../BaseURL/BaseURL";
import {over} from 'stompjs';
import SockJS from 'sockjs-client';
import GetAllUsers from "./GetAllUsers";
import jwt_decode from "jwt-decode";

let stompClient = null;
const ChatRoomUser = () => {

    const user = useUser();
    const roomCode = window.location.href.split("/chat-rooms/")[1];
    const [users, setUsers] = useState(null);
    const [publicChats, setPublicChats] = useState([]);
    const [roles, setRoles] = useState(null);
    const [userData, setUserData] = useState({
        username: '',
        receiverName: '',
        connected: false,
        message: ''
    });

    useEffect(() => {
        ajax(`${baseURL}api/chat-rooms/${roomCode}`, "GET", user.jwt)
            .then((response) => {
                setUsers(response)
                const decodeJwt = jwt_decode(user.jwt);
                setRoles(decodeJwt.sub)
                // connect()
            })
    }, [roomCode, user.jwt])


    function connect() {
        let Sock = new SockJS('http://localhost:8080/ws');
        stompClient = over(Sock)
        stompClient.connect({}, onConnected, onError)
    }

    function onConnected() {
        console.log('Свързване успешно установено.')
        setUserData({...userData, "connected": true})
        stompClient.subscribe(`/chat-rooms/${roomCode}`, onMessageReceived);
        userJoin();
    }


    const userJoin = () => {
        const chatMessage = {
            senderName: roles,
            status: "JOIN"
        }
        stompClient.send(`/api/message/${roomCode}`, {}, JSON.stringify(chatMessage));
    }

    const onError = (err) => {
        console.log(err);
    }

    function onMessageReceived(payload) {
        const payloadData = JSON.parse(payload.body);
        publicChats.push(payloadData)
        setPublicChats([...publicChats]);
    }

    const handleMessage = (event) => {
        const {value} = event.target;
        setUserData({...userData, "message": value});
    }

    const sendValue = () => {
        if (stompClient) {
            const chatMessage = {
                senderName: roles,
                message: userData.message,
                status: "MESSAGE"
            }
            if(userData.message.trim() === ""){
                return;
            }
            console.log(chatMessage);
            stompClient.send(`/api/message/${roomCode}`, {}, JSON.stringify(chatMessage));
            setUserData({...userData, "message": ""})
        }
    }

    useEffect(() => {
        if (roles !== null) {
            connect()
        }
    }, [roles]);


    return (
        <main className="chat-room">
            <GetAllUsers/>
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
            <div className="chat-container">
                {userData.connected ?
                    <div className="chat-box">
                        <div className="chat-content">
                            <ul className="chat-message">
                                {publicChats.map((chat, index) => (
                                    <li key={index}
                                        className="chat-message-row">
                                        <div className="chat-message-data">{chat.senderName}: {chat.message}</div>
                                    </li>
                                ))}
                            </ul>
                            <div className="send-message">
                                <label>Send message</label>
                                <input
                                    type="text"
                                    className="input-message"
                                    placeholder="Send Message"
                                    value={userData.message}
                                    onChange={handleMessage}
                                />
                                <button
                                    type="button"
                                    className="send-button"
                                    onClick={sendValue}
                                >
                                    send
                                </button>
                            </div>
                        </div>
                    </div>
                    :
                    <></>
                }
            </div>
        </main>
    )
}

export default ChatRoomUser;