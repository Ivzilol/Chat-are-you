import './App.css';
import {Route, Routes} from "react-router-dom";
import Register from "./Components/Register/Register";
import ConfirmRegister from "./Components/Register/ConfirmRegister";

function App() {
    return (
        <Routes>
            <Route path="/register" element={<Register/>}/>
            <Route path="/register/verify/:verificationCode" element={<ConfirmRegister/>}/>
        </Routes>
    );
}

export default App;
