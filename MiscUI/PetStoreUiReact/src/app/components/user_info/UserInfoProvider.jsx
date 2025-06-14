 "use client"
import axios from "axios";

import UserInfoContext from "./UserInfoContext";
import { GET_USER_INFO_URL, LOGIN_URL } from "../../constants";
import { checkApiError } from "../../api/common";
import { getAllPetsRequest } from "../../api/actions";

const { useState, useEffect } = require("react")
const UserInfoProvider = ({children}) => {

    const [userInfo, setUserInfo] = useState();

    useEffect(() => {
        const getUserInfo = async () => {
            try {
                const response = await axios.get(GET_USER_INFO_URL);
                console.log('UserInfo Data', response.data);
                setUserInfo(response.data);
            } catch (error) {
                checkApiError(error);
            }
        }

        getUserInfo();
    }, []);

    return (
        <UserInfoContext.Provider value={{userInfo}}>
            {children}
        </UserInfoContext.Provider>)
}

export default UserInfoProvider;