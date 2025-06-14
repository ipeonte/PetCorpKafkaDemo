"use client"

import { Provider } from "react-redux";
import PetSearch from "./components/pet/PetSearch";
import UserInfoComp from "./components/user_info/UserInfo"
import UserInfoProvider from "./components/user_info/UserInfoProvider"
import store from "../redux/store";
import 'bootstrap/dist/css/bootstrap.min.css';
import PetList from "./components/pet/PetList";

export default function Home() {
  return <UserInfoProvider>
    <UserInfoComp></UserInfoComp>
    <div className="all-pets">
      <Provider store={store}>
        <PetSearch />
        <PetList />
      </Provider>
    </div>
  </UserInfoProvider>
}
