import { all } from "redux-saga/effects";
import { petSaga } from "./petSaga";

function* rootSaga() {
    yield all([
        petSaga()
    ])
}

export default rootSaga;