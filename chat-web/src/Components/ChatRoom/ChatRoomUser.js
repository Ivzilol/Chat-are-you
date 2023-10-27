import {useUser} from "../../UserProvider/UserProvider";
import {useEffect, useState} from "react";
import ajax from "../../Service/FetchService";
import baseURL from "../BaseURL/BaseURL";
import {over} from 'stompjs';
import SockJS from 'sockjs-client';
import jwt_decode from "jwt-decode";
import * as PropTypes from "prop-types";
import {useNavigate} from "react-router-dom";

let stompClient = null;

function UserOnline(props) {
    return <section className="user-online">
        {props.usersOnline ? (
            <section className="all-users-chat-container">
                {props.usersOnline.map(props.prop1)}
            </section>
        ) : (
            <></>
        )}
    </section>;
}

UserOnline.propTypes = {
    usersOnline: PropTypes.any,
    prop1: PropTypes.func
};
const ChatRoomUser = () => {

    const user = useUser();
    const navigate = useNavigate();
    const roomCode = window.location.href.split("/chat-rooms/")[1];
    const [users, setUsers] = useState(null);
    const [publicChats, setPublicChats] = useState([]);
    const [roles, setRoles] = useState(null);
    const [usersOnline, setUsersOnline] = useState(null)
    const [oldMessages, setOldMessages] = useState(null);
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
            if (userData.message.trim() === "") {
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

    useEffect(() => {
        ajax(`${baseURL}api/chat-rooms/users`, "GET", user.jwt)
            .then((response) => {
                setUsersOnline(response);
            })
    }, [user.jwt])


    function addUserInRoom(id) {
        const requestBody = {
            id: id
        }
        ajax(`${baseURL}api/chat-rooms/add-user/${roomCode}`, "POST", user.jwt, requestBody)
            .then((response) => {
                if (response.custom === 'Successful add user in room') {
                    alert(response.custom)
                }
            })
    }

    useEffect(() => {
        ajax(`${baseURL}api/message/get-messages/${roomCode}`, "GET", user.jwt)
            .then((response) => {
                setOldMessages(response);
            })
    }, [])

    function leftRoom() {
        ajax(`${baseURL}api/chat-rooms/left/${roomCode}`, "DELETE", user.jwt)
            .then((response) => {
                if (response.custom === "Successful left room") {
                    alert(response.custom);
                    navigate("/")
                } else {
                    alert(response.custom)
                }
            })
    }


    return (
        <main className="chat-room">
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
                                {oldMessages !== null ? oldMessages.map((oldMessage) => (
                                    <li key={oldMessage.id}
                                        className="chat-message-row">
                                        <div
                                            className="chat-message-data">{oldMessage.username}: {oldMessage.message}</div>
                                    </li>
                                )) : (
                                    <></>
                                )}
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
                            <button
                                type="button"
                                className="left-button"
                                onClick={leftRoom}
                            >left room
                            </button>
                        </div>
                    </div>
                    :
                    <></>
                }
            </div>
            <UserOnline
                usersOnline={usersOnline}
                prop1={(user) => (
                    <div className="all-users-chat-container-items"
                         key={user.id}
                         id={user.id}
                    >
                        <button
                            id={user.id}
                            key={user.id}
                            type="button"
                            onClick={() => addUserInRoom(user.id)}
                        >
                            {user.username}
                        </button>
                    </div>
                )}/>
        </main>
    )
}

export default ChatRoomUser;