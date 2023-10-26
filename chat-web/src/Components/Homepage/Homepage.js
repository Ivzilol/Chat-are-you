import CreateChatRoom from "../ChatRoom/CreateChatRoom";
import GetAllUsers from "../ChatRoom/GetAllUsers";
import UserChatRooms from "../ChatRoom/UserChatRooms";
const Homepage = () => {
    return (
        <>Homepage
            <CreateChatRoom/>
            <GetAllUsers/>
            <UserChatRooms/>
        </>
    )
}

export default Homepage;