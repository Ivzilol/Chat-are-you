
import {useUser} from "../UserProvider/UserProvider";
import {useState} from "react";
import ajax from "../Service/FetchService";
import {Navigate} from "react-router-dom";
import baseURL from "../Components/BaseURL/BaseURL";
import Loading from "../Components/Loading/Loading";


const PrivateRoute = (props) => {
    const user = useUser();
    const [isLoading, setIsLoading] = useState(true);
    const [isValid, setIsValid] = useState(null);
    const {children} = props;


    if (user) {
        ajax(`${baseURL}api/auth/validate?token=${user.jwt}`, "GET", user.jwt)
            .then(isValid => {
                setIsValid(isValid);
                setIsLoading(false);
            });
    } else {
        return <Navigate to="/login"/>
    }

    return isLoading ? (
        <Loading/>
    ) : isValid === true ? (
        children
    ) : (
        <Navigate to="/login"/>
    );
};

export default PrivateRoute;