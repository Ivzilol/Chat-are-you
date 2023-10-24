import {useUser} from "../../UserProvider/UserProvider";
import {useState} from "react";
import ajax from "../../Service/FetchService";
import baseURL from "../BaseURL/BaseURL";


const CreateChatRoom = () => {
    const user = useUser();
    const [chatroomName, setChatroomName] = useState("");
    const [emptyNameDialog, setEmptyNameDialog] = useState(false);
    const [emptyNameChatroom, setEmptyNameChatroom] = useState("");

    function createChatRoom() {

        if (chatroomName.trim() === '') {
            setEmptyNameChatroom("Please add name ot chat room")
            setEmptyNameDialog(true);
            return;
        }

        const requestBody = {
            chatroomName: chatroomName
        }

        ajax(`${baseURL}api/chat-rooms/create`, "POST", user.jwt, requestBody)
            .then((response) => {
                if (response.custom === 'Successful create chatroom') {
                    alert(response.custom)
                } else {
                    alert(response.custom)
                }
            })
    }

    const handleInputCursor = () => {
        if (emptyNameDialog) {
            setEmptyNameDialog(false);
        }
    };

    return (
        <main className="create-chat-room">
            <section className="create-chat-room-container">
                <h3>Create Chat Room</h3>
                <div className="create-chat-room-container-items">
                    <label>Char Room name</label>
                    <input
                        type="text"
                        id="chatroomName"
                        autoComplete="off"
                        name="chatroomName"
                        placeholder="Name Chat Room"
                        value={chatroomName}
                        onChange={(e) => setChatroomName(e.target.value)}
                        onFocus={handleInputCursor}
                    />
                    {emptyNameDialog &&
                        <h5>{emptyNameChatroom}</h5>
                    }
                    <button
                        id="submit"
                        type="button"
                        onClick={() => createChatRoom()}
                    >Create Chat Room
                    </button>
                </div>
            </section>
        </main>
    )
}

export default CreateChatRoom;